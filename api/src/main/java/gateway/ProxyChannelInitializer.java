package gateway;

import gateway.handler.RouteHandler;
import gateway.handler.impl.fullreq.AuthInBoundHandlerImpl;
import gateway.handler.impl.fullreq.RouteInBoundHandlerImpl;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class ProxyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private RouteHandler routeHandler;

    /**
     * fullHttpRequest的方式
     * @param socketChannel
     * @throws Exception
     */
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //系统级行为
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        //        channelPipeline.addLast(new HttpRequestDecoder());
//        channelPipeline.addLast(new HttpResponseEncoder());
        channelPipeline.addLast("HttpServerCodec", new HttpServerCodec());
        channelPipeline.addLast("HttpObjectAggregator", new HttpObjectAggregator(1024 * 1024));

        //扩展行为
        channelPipeline.addLast("RouteInBoundHandlerImpl", new RouteInBoundHandlerImpl());//路由映射
//        channelPipeline.addLast("RouteHandlerImpl4", new AuthInBoundHandlerImpl());//鉴权

        System.out.println("add channel handler finished...");
    }

}