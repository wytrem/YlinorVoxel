package com.ylinor.library.network;

import com.ylinor.library.network.kryo.KryoDecoder;
import com.ylinor.library.network.kryo.KryoEncoder;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author pierre
 * @since 1.0.0
 */
public class ClientNetwork extends AbstractNetwork
{

    /**
     * Objet bootstrap netty, utilisé pour la création de la connection
     */
    private Bootstrap bootstrap;

    /**
     * Channel netty, utilisé pour écrire des données vers le serveur
     */
    private Channel channel;

    /**
     * Group netty qui va gérer l'arriver de packet
     */
    private EventLoopGroup group;

    /**
     * ThreadPool utilisé par netty pour gérer l"arrivé de packet simultanée
     */
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * Nombre de thread maximum utilisé par netty
     */
    private static final int THREAD_LIMIT = 10;

    @Override
    public void start(String ip, int port)
    {
        try
        {
            this.group = new NioEventLoopGroup(THREAD_LIMIT, threadPool);
            this.bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new KryoDecoder(kryo));
                    ch.pipeline().addLast(new KryoEncoder(kryo));
                    ch.pipeline().addLast(new ClientNetworkHandler());
                }
            });
            this.channel = bootstrap.connect(ip, port).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            group.shutdownGracefully();
        }
    }

    @Override
    public void sendPacket(IPacket packet, INetworkEntity entity)
    {

    }

    private class ClientNetworkHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
        {
            super.channelReadComplete(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
        {
            super.exceptionCaught(ctx, cause);
        }
    }
}
