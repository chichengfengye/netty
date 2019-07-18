package gateway.handler;

import gateway.common.Constant;
import gateway.common.NettyCode;
import gateway.common.ReturnResult;
import gateway.common.RouteInfo;
import gateway.handler.ab.AbstractInBoundAdapterHandler;
import gateway.helper.ResponseBackHelper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

/**
 * 转发请求给后面的被代理服务 并 写回数据给请求者
 */
public class IOHandler extends AbstractInBoundAdapterHandler {

    @Override
    protected ReturnResult businessRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpResponse response = null;
        //接受數據
        RouteInfo routeInfo = (RouteInfo) ctx.channel().attr(AttributeKey.valueOf(Constant.ROUTEI_INFO)).get();
        if (routeInfo != null) {
            // 進行轉發
            try {
                response = ResponseBackHelper.sendRequest(routeInfo.getHttpMethod(), routeInfo.getUri(), (FullHttpRequest) msg);
            } catch (Exception e) {
                e.printStackTrace();
                ReturnResult.failure(NettyCode.COMMON_EXCEPTION, "SERVER INTERNAL EXCEPTION");
            }

        }

        //把结果返回给客户端
        //1. 直接返回数据内容包括数据类型等属性信息
        //2. 如果只能自己设定返回数据的格式，那就只能自己先分析理数类型，再设置相应头并返回
        if (response == null) {
            response = ResponseBackHelper.httpJsonResponse(Unpooled.copiedBuffer("", CharsetUtil.UTF_8));
        }

        ctx.writeAndFlush(response);
        ctx.close();

        return ReturnResult.success();
    }
}
