package gateway.handler.impl.fullreq;

import gateway.common.*;
import gateway.exception.AccessedDeniedException;
import gateway.exception.NettyHandlerException;
import gateway.handler.AuthHandler;
import gateway.handler.ab.AbstractInBoundSimpleHandler;
import gateway.helper.ParamDecodeHelper;
import gateway.helper.ResponseBackHelper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

import java.util.List;

public class AuthInBoundSimpleHandlerImpl extends AbstractInBoundSimpleHandler implements AuthHandler {
    @Override
    protected boolean isLast() {
        return true;
    }

    @Override
    public boolean isAccessible(ProxiedService proxyService, String key) {
        List<String> keys = proxyService.getKeys();
        return keys.contains(key);
    }

    @Override
    protected ReturnResult businessRead(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        System.out.println("AuthInBoundSimpleHandlerImpl handler1执行...");

        //獲取key
        String key = (String) ParamDecodeHelper.getReqParam("key", fullHttpRequest);

        //用key判斷是否允許訪問該接口
        RouteInfo routeInfo = (RouteInfo) ctx.channel()
                .attr(AttributeKey.valueOf(Constant.ROUTEI_INFO)).get();

        ProxiedService proxiedService = routeInfo.getProxiedService();
        if (!isAccessible(proxiedService, key)) {
            return ReturnResult.failure(NettyCode.ACCESS_DENIED, NettyMessage.ACCESS_DENIED);
        }

        return ReturnResult.success();
    }

    @Override
    protected void businessRead2(ChannelHandlerContext ctx, FullHttpRequest msg) throws NettyHandlerException {
        System.out.println("AuthInBoundSimpleHandlerImpl handler1执行...");

        //獲取key
        String key = (String) ParamDecodeHelper.getReqParam("key", msg);

        //用key判斷是否允許訪問該接口
        RouteInfo routeInfo = (RouteInfo) ctx.channel()
                .attr(AttributeKey.valueOf(Constant.ROUTEI_INFO)).get();

        ProxiedService proxiedService = routeInfo.getProxiedService();
        if (!isAccessible(proxiedService, key)) {
            throw new AccessedDeniedException();
        }

        ctx.writeAndFlush(ResponseBackHelper.httpJsonResponse(Unpooled.copiedBuffer("404 \n resource not found", CharsetUtil.UTF_8)));
        ctx.close();

    }
}
