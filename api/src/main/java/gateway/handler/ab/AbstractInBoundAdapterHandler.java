package gateway.handler.ab;

import gateway.exception.NettyHandlerException;
import gateway.helper.ResponseBackHelper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public abstract class AbstractInBoundAdapterHandler extends ChannelInboundHandlerAdapter {
    protected abstract void businessRead2(ChannelHandlerContext ctx, Object msg) throws Exception;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            businessRead2(ctx, msg);
            ctx.fireChannelRead(msg);
        } catch (NettyHandlerException e) {
            System.out.println(e.getCode() + ":" + e.getState());
            ctx.writeAndFlush(ResponseBackHelper.httpJsonResponse(Unpooled.copiedBuffer(e.getCode() + "\n" + e.getState(), CharsetUtil.UTF_8)));
            ctx.close();
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==> ["+ this.getClass().getName() +"] channelReadComplete...");
//        ctx.flush();
        super.channelReadComplete(ctx);
    }
}
