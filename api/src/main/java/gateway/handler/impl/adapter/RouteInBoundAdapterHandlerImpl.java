package gateway.handler.impl.adapter;

import gateway.common.*;
import gateway.exception.NettyHandlerException;
import gateway.exception.ResourceNotFoundException;
import gateway.handler.RouteHandler;
import gateway.handler.ab.AbstractInBoundAdapterHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.AttributeKey;

import java.util.Arrays;

public class RouteInBoundAdapterHandlerImpl extends AbstractInBoundAdapterHandler implements RouteHandler {
    private String fromUri1;
    private String toUri1;

    private String fromUri2;
    private String toUri2;

    public RouteInBoundAdapterHandlerImpl() {
        fromUri1 = "/api";
        toUri1 = "http://api.test.com/api/post";
        fromUri2 = "/bd/swd";
        toUri2 = "http://www.baidu.com/s";
    }

    private HttpMethod httpMethod;
    private String url;

    @Override
    protected void businessRead2(ChannelHandlerContext ctx, Object msg) throws NettyHandlerException {
        System.out.println("RouteInBoundAdapterHandlerImpl businessRead2...");
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            httpMethod = request.method();
            String uri = request.uri();

            if (uri.startsWith(fromUri1)) {
                url = uri.replace(fromUri1, toUri1);
            } else if (uri.startsWith(fromUri2)) {
                url = uri.replace(fromUri2, toUri2);
            } else {
                throw new ResourceNotFoundException();
            }

            ProxiedService proxiedService = getProxiedUri(fromUri1);
            RouteInfo info = new RouteInfo(httpMethod, url, proxiedService);
            ctx.channel().attr(AttributeKey.valueOf(Constant.ROUTEI_INFO)).set(info);
        }

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
