package gateway.handler;

import gateway.common.Constant;
import gateway.common.NettyCode;
import gateway.common.ReturnResult;
import gateway.common.RouteInfo;
import gateway.handler.ab.AbstractInBoundAdapterHandler;
import gateway.helper.ResponseBackHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;

/**
 * 转发请求给后面的被代理服务
 */
public class IOHandler extends AbstractInBoundAdapterHandler {

    @Override
    protected ReturnResult businessRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = null;
        //接受數據
        RouteInfo routeInfo = (RouteInfo) ctx.channel().attr(AttributeKey.valueOf(Constant.ROUTEI_INFO)).get();
        if (routeInfo != null) {
            // 進行轉發
            String result = null;
            try {
//                result = ResponseBackHelper.sendRequest(routeInfo.getHttpMethod(), routeInfo.getUri(), (FullHttpRequest) msg);
                HttpResponse response = ResponseBackHelper.sendRequest2(routeInfo.getHttpMethod(), routeInfo.getUri(), (FullHttpRequest) msg);
                if (response != null) {
                    byteBuf = Unpooled.copiedBuffer(result, CharsetUtil.UTF_8);
                }

            } catch (Exception e) {
                e.printStackTrace();
                ReturnResult.failure(NettyCode.COMMON_EXCEPTION, "SERVER INTERNAL EXCEPTION");
            }

        }

        //把结果返回给客户端
        //1. 直接返回数据内容包括数据类型等属性信息
        //2. 如果只能自己设定返回数据的格式，那就只能自己先分析理数类型，再设置相应头并返回
        HttpResponse httpResponse= ResponseBackHelper.httpJsonResponse(byteBuf);
        ctx.writeAndFlush(httpResponse);
        ctx.close();
        return ReturnResult.success();
    }
}
