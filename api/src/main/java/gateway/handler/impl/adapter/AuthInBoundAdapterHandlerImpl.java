package gateway.handler.impl.adapter;

import gateway.common.*;
import gateway.exception.AccessedDeniedException;
import gateway.exception.NettyHandlerException;
import gateway.handler.AuthHandler;
import gateway.handler.ab.AbstractInBoundAdapterHandler;
import gateway.helper.ParamDecodeHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

import java.util.List;

public class AuthInBoundAdapterHandlerImpl extends AbstractInBoundAdapterHandler implements AuthHandler {
    @Override
    public boolean isAccessible(ProxiedService proxyService, String key) {
        List<String> keys = proxyService.getKeys();
        return keys.contains(key);
    }

    @Override
    protected void businessRead2(ChannelHandlerContext ctx, Object object) throws NettyHandlerException {
        if (object instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) object;
            System.out.println("AuthInBoundSimpleHandlerImpl businessRead2...");

            //獲取key
            String key = (String) ParamDecodeHelper.getReqParam("key", fullHttpRequest);

            //用key判斷是否允許訪問該接口
            RouteInfo routeInfo = (RouteInfo) ctx.channel()
                    .attr(AttributeKey.valueOf(Constant.ROUTEI_INFO)).get();

            ProxiedService proxiedService = routeInfo.getProxiedService();
            if (!isAccessible(proxiedService, key)) {
                throw new AccessedDeniedException();
            }

            System.out.println("AuthInBoundAdapterHandlerImpl finished...");

        }
    }

}
