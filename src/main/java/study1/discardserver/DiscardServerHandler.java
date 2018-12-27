package study1.discardserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        try {

            System.out.println(ctx);
            System.out.println(msg);
            //查看数据
            while (in.isReadable()) {
                System.out.println((char)in.readByte());
                System.out.flush();
            }

        }finally {
//            in.release();
            ReferenceCountUtil.release(msg);//more gracefully
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();

    }
}
