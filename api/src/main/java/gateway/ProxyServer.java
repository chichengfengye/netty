package gateway;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 代理，依据用户请求的uri，选择映射到不同的服务器上去
 * 此处以baidu和csdn（或者本地的一个或多个服务也行）为例子执行
 */
public class ProxyServer {

    private final int port;


    public ProxyServer(int port) {
        this.port = port;
    }


    public static void main(String[] args) {
        //TODO 初始化配置信息
        int port = 8899;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        ProxyServer proxyServer = new ProxyServer(port);

        //TODO 启动服务
        proxyServer.start();

    }

    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
//                    .handler(new MyChannelDuplexHandler())
                    .childHandler(new ProxyChannelInitializer());
//            System.out.println("start server on port: " + port);
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("server started on port: " + port);

            future.channel().closeFuture().sync();
            System.out.println("closeFuture on port: " + port);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
