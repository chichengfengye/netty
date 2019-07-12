package gateway.handler.impl;

//import gateway.config.RoutesConfig;

import gateway.common.ProxiedService;
import gateway.handler.RouteHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * 路由
 * uri_A -> uri_B
 * domain_A -> domain_B
 * ...
 */
public class RouteHandlerImpl extends ChannelInboundHandlerAdapter implements RouteHandler {
    private static final String key = "1";

    private String fromUri1 = "/api";
    private String toUri1 = "http://api.test.com";

    private String fromUri2 = "/bd/swd";
    private String toUri2 = "https://www.baidu.com/s?wd=";

    {
        System.out.println("RouteHandlerImpl 实例化。。。");
    }

    private HttpMethod httpMethod;
    private HttpContent httpContent;
    private String url;

    public void channelRead(ChannelHandlerContext channelHandlerContext, Object httpObject) throws Exception {
        boolean returnNotFound = false;
        String result = null;
        if (httpObject instanceof FullHttpRequest) {
            FullHttpRequest request = ((FullHttpRequest) httpObject);

            httpMethod = request.method();
            String uri = request.uri();

            if (uri.startsWith(fromUri1)) {
                url = uri.replace(fromUri1, toUri1);
            } else if (uri.startsWith(fromUri2)) {
                url = uri.replace(fromUri2, toUri2);
            } else {
                result = "404 \n resource not found";
                returnNotFound = true;
            }

            if (!returnNotFound) {
                result = sendRequest(httpMethod, url, request);
            }

            writeAndFlush(channelHandlerContext, Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));
        }

    }

    private String sendRequest(HttpMethod httpMethod, String url, FullHttpRequest request) {
        OkHttpClient client = new OkHttpClient();
        Request okRequest = null;
        String result = null;
        if (httpMethod.equals(HttpMethod.GET)) {
            okRequest = new Request.Builder()
                    .url(url)
//                        .get(null)
                    .build();
        } else if (httpMethod.equals(HttpMethod.POST)) {
            String json = "";
            ByteBuf byteBuf = request.content();
            if (byteBuf.isReadable()) {
                json = byteBuf.toString(CharsetUtil.UTF_8);
            } else {
                return null;
            }

            MediaType JSON = MediaType.get("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            okRequest = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }

        try (Response response = client.newCall(okRequest).execute()) {
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void writeAndFlush(ChannelHandlerContext channelHandlerContext, ByteBuf content) {
        channelHandlerContext.writeAndFlush(httpResponse(content));
        channelHandlerContext.close();
    }

    private HttpResponse httpResponse(ByteBuf content) {
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        return response;
    }



/*
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("route handler added...");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("route handler active...");
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("route handler inactive...");
        super.channelInactive(ctx);
    }
*/

    public Map<String, String> getRouteMap() {
        return null;
    }

    public String getProxiedUri(String registerUri) {
        return null;
    }

    public Map<String, ProxiedService> getProxiedServicesMap() {
        return null;
    }
}
