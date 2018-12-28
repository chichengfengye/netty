package study1.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("======================== channel Read ========================");

        ByteBuf in = (ByteBuf) msg;

        try {
            if (in.isReadable()) {
                long time = in.readUnsignedInt() * 1000L;//（1）


                System.out.println(time);
            }
            ctx.close();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
//（1）这段代码报了如下异常：
//      java.lang.IndexOutOfBoundsException: readerIndex(0) + length(4) exceeds writerIndex(1): PooledUnsafeDirectByteBuf(ridx: 0, widx: 1, cap: 1024)
