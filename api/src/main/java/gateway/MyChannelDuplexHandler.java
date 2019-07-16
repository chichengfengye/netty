package gateway;

import gateway.handler.RouteHandler;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;

import java.net.SocketAddress;

public class MyChannelDuplexHandler extends ChannelDuplexHandler {
    private RouteHandler routeHandler;

    /**
     * fullHttpRequest的方式
     * @param socketChannel
     * @throws Exception
     */
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("MyChannelDuplexHandler initChannel...");
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("MyChannelDuplexHandler connect...");
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("MyChannelDuplexHandler disconnect...");
        super.disconnect(ctx, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("MyChannelDuplexHandler close...");
        super.close(ctx, promise);
    }

    @Override
    public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("MyChannelDuplexHandler deregister...");
        super.deregister(ctx, promise);
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyChannelDuplexHandler read...");
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("MyChannelDuplexHandler write...");
        super.write(ctx, msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyChannelDuplexHandler flush...");
        super.flush(ctx);
    }
}
