package gateway;

import gateway.common.ProxiedService;
import gateway.handler.RouteHandler;
import gateway.handler.impl.RouteHandlerImpl;
import gateway.handler.impl.RouteHandlerImpl2;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class ProxyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private RouteHandler routeHandler;

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //系统级行为
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        channelPipeline.addLast("HttpServerCodec", new HttpServerCodec());

        //扩展行为
        channelPipeline.addLast("RouteHanlder", new RouteHandlerImpl2());//路由映射

        System.out.println("add channel handler finished...");
    }

}
