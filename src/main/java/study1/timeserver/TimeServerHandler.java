package study1.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    //the channelActive() method will be invoked when a connection is established and ready to generate traffic.
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        System.out.println(" ========================= channel Active... =========================");
        ByteBuf time = ctx.alloc().buffer(4);
        time.writeByte((int) (System.currentTimeMillis() / 1000L));
        ChannelFuture future = ctx.writeAndFlush(time);
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                ctx.close();
            }
        });

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
