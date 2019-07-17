package gateway.handler.impl;

//import gateway.config.RoutesConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gateway.common.ProxiedService;
import gateway.handler.RouteHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 路由 直接转发【利用FullHttpRequest】
 * uri_A -> uri_B
 * domain_A -> domain_B
 * ...
 */
public class RouteHandlerImplDirectSendFullHttpRequest extends SimpleChannelInboundHandler<FullHttpRequest> implements RouteHandler {
    private static final String key = "1";

    private String fromUri1 = "/api";
    private String toUri1 = "http://api.test.com/api/post";

    private String fromUri2 = "/bd/swd";
    private String toUri2 = "http://www.baidu.com/s";

    private String testUrl = "http://api.test.com:80";

    {
//        System.out.println("RouteHandlerImplDirectSendFullHttpRequest 实例化。。。");
    }

    private HttpMethod httpMethod;
    private String url;

    public void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) throws Exception {
        boolean returnNotFound = false;
        String result = "null";

        httpMethod = request.method();
        String uri = request.uri();

        url = testUrl + uri;

     /*   if (uri.startsWith(fromUri1)) {
            url = uri.replace(fromUri1, toUri1);
        } else if (uri.startsWith(fromUri2)) {
            url = uri.replace(fromUri2, toUri2);
        } else {
            result = "404 \n resource not found";
            returnNotFound = true;
        }
*/
        if (!returnNotFound) {
            result = sendRequest(httpMethod, url, request);
        }

        writeAndFlush(channelHandlerContext, Unpooled.copiedBuffer(result, CharsetUtil.UTF_8));


    }

    private String sendRequest(HttpMethod httpMethod, String url, FullHttpRequest request) {
        String result = "just support GET|POST method";
        OkHttpClient client = new OkHttpClient();
        Request okRequest = null;
        RequestBody body;
        String reqBody = "";

        if (httpMethod.equals(HttpMethod.GET)) {
            okRequest = new Request.Builder()
                    .url(url)
//                        .get(null)
                    .build();
        } else if (httpMethod.equals(HttpMethod.POST)) {
            HttpHeaders headers = request.headers();
            MediaType mediaType;
            ByteBuf byteContent = request.content();
            if (byteContent.isReadable()) {
                reqBody = byteContent.toString(CharsetUtil.UTF_8);
            }

            String contentType = headers.get("content-type");
            mediaType = MediaType.get(contentType + "; charset=utf-8");

            body = RequestBody.create(mediaType, reqBody);
            okRequest = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

        } else {
            return "";
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
