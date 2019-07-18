package gateway.handler.ab;

import gateway.common.NettyCode;
import gateway.common.ReturnResult;
import gateway.exception.NettyHandlerException;
import gateway.helper.ResponseBackHelper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public abstract class AbstractInBoundAdapterHandler extends ChannelInboundHandlerAdapter {
    protected abstract ReturnResult businessRead(ChannelHandlerContext ctx, Object msg) throws Exception;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ReturnResult returnResult = businessRead(ctx, msg);
            if (ReturnResult.isSuccess(returnResult)) {
                ctx.fireChannelRead(msg);
            } else {
                responseError2(ctx, returnResult.getCode(), returnResult.getMsg());
            }
        } catch (NettyHandlerException e) {
            e.printStackTrace();
            responseError(ctx);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==> ["+ this.getClass().getName() +"] channelReadComplete...");
//        ctx.flush();
        super.channelReadComplete(ctx);
    }

    private void responseError(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(ResponseBackHelper.httpJsonResponse(Unpooled.copiedBuffer(NettyCode.ERROR + "", CharsetUtil.UTF_8)));
        ctx.close();
    }

    private void responseError2(ChannelHandlerContext ctx, int code, String msg) {
        ctx.writeAndFlush(ResponseBackHelper.httpJsonResponse(Unpooled.copiedBuffer(code + ":" + msg, CharsetUtil.UTF_8)));
        ctx.close();
    }
}
