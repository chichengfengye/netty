package gateway.handler.impl.fullreq;

//import gateway.config.RoutesConfig;

import gateway.common.*;
import gateway.exception.NettyHandlerException;
import gateway.exception.ResourceNotFoundException;
import gateway.handler.RouteHandler;
import gateway.handler.ab.AbstractInBoundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.AttributeKey;

import java.util.Arrays;

/**
 * 路由 直接转发【利用FullHttpRequest】
 * uri_A -> uri_B
 * domain_A -> domain_B
 * ...
 */
public class RouteInBoundHandlerImpl extends AbstractInBoundHandler implements RouteHandler {
    private String fromUri1;
    private String toUri1;

    private String fromUri2;
    private String toUri2;

    public RouteInBoundHandlerImpl() {
        fromUri1 = "/api";
        toUri1 = "http://api.test.com/api/post";
        fromUri2 = "/bd/swd";
        toUri2 = "http://www.baidu.com/s";
    }

    private HttpMethod httpMethod;
    private String url;

    @Override
    protected ReturnResult businessRead(ChannelHandlerContext ctx, FullHttpRequest request) {
        System.out.println("RouteInBoundHandlerImpl handler1执行...");
        httpMethod = request.method();
        String uri = request.uri();

        if (uri.startsWith(fromUri1)) {
            url = uri.replace(fromUri1, toUri1);
        } else if (uri.startsWith(fromUri2)) {
            url = uri.replace(fromUri2, toUri2);
        } else {
            return ReturnResult.failure(NettyCode.NOT_FOUND, NettyMessage.NOT_FOUND);
            //            throw new ResourceNotFoundException();
        }

        ProxiedService proxiedService = getProxiedUri(fromUri1);

        RouteInfo info = new RouteInfo(httpMethod, url, proxiedService);
        ctx.channel().attr(AttributeKey.newInstance(Constant.ROUTEI_INFO)).set(info);

        return ReturnResult.success();
        //            result = sendRequest(httpMethod, url, request);
    }

    @Override
    protected void businessRead2(ChannelHandlerContext ctx, FullHttpRequest msg) throws NettyHandlerException {
        System.out.println("RouteInBoundHandlerImpl handler1执行...");
  /*      httpMethod = msg.method();
        String uri = msg.uri();

        if (uri.startsWith(fromUri1)) {
            url = uri.replace(fromUri1, toUri1);
        } else if (uri.startsWith(fromUri2)) {
            url = uri.replace(fromUri2, toUri2);
        } else {
            throw new ResourceNotFoundException();
        }

        ProxiedService proxiedService = getProxiedUri(fromUri1);

        RouteInfo info = new RouteInfo(httpMethod, url, proxiedService);
        ctx.channel().attr(AttributeKey.newInstance(Constant.ROUTEI_INFO)).set(info);*/
        ctx.fireChannelRead(msg);
        //            result = sendRequest(httpMethod, url, request);
    }

    @Override
    public ProxiedService getProxiedUri(String registerUri) {
        ProxiedService service = new ProxiedService();
        service.setExposeUrl(registerUri);
        service.setProxiedUrl("http://api.test.com/api/post");
        service.setKeys(Arrays.asList("111", "222", "333"));
        return service;
    }

}
