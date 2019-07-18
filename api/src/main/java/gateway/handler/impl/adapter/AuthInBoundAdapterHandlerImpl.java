package gateway.handler.impl.adapter;

import gateway.common.*;
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
    protected ReturnResult businessRead(ChannelHandlerContext ctx, Object object) throws Exception {
        if (object instanceof FullHttpRequest) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) object;
//            System.out.println("AuthInBoundSimpleHandlerImpl businessRead...");

            //獲取key
            String key = (String) ParamDecodeHelper.getReqParam("key", fullHttpRequest);

            //用key判斷是否允許訪問該接口
            RouteInfo routeInfo = (RouteInfo) ctx.channel()
                    .attr(AttributeKey.valueOf(Constant.ROUTEI_INFO)).get();

            ProxiedService proxiedService = routeInfo.getProxiedService();
            if (!isAccessible(proxiedService, key)) {
                return ReturnResult.failure(NettyCode.ACCESS_DENIED, NettyMessage.ACCESS_DENIED);
            }

//            System.out.println("AuthInBoundAdapterHandlerImpl finished...");

            return ReturnResult.success();
        }

        return ReturnResult.failure(NettyCode.COMMON_EXCEPTION, NettyMessage.COMMON_EXCEPTION);
    }

}
