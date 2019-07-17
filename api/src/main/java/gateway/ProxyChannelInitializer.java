package gateway;

import gateway.handler.IOHandler;
import gateway.handler.RouteHandler;
import gateway.handler.impl.adapter.AuthInBoundAdapterHandlerImpl;
import gateway.handler.impl.adapter.RouteInBoundAdapterHandlerImpl;
import gateway.handler.impl.fullreq.AuthInBoundSimpleHandlerImpl;
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
        channelPipeline.addLast("RouteInBoundAdapterHandlerImpl", new RouteInBoundAdapterHandlerImpl());//路由映射
        channelPipeline.addLast("AuthInBoundSimpleHandlerImpl", new AuthInBoundAdapterHandlerImpl());//鉴权
        channelPipeline.addLast("IOHandler", new IOHandler());//代理请求的转发与反馈

//        System.out.println("add channel handler finished...");
    }

}
