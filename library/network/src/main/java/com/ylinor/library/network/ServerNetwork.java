package com.ylinor.library.network;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.kryo.KryoDecoder;
import com.ylinor.library.network.kryo.KryoEncoder;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;
import com.ylinor.library.network.packets.NetworkEntity;
import com.ylinor.library.network.packets.Packet0;
import com.ylinor.library.network.util.PairPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author pierre
 * @since 1.0.0
 */
public class ServerNetwork extends AbstractNetwork
{

    /**
     * Objet bootstrap netty, utilisé pour la création de la connection
     */
    private ServerBootstrap bootstrap;

    /**
     * Channel netty, utilisé pour écrire des données vers le serveur
     */
    private Channel channel;

    /**
     * Group netty qui va gérer le gros
     */
    private EventLoopGroup bossGroup;

    /**
     * Group netty qui va travailler les packets
     */
    private EventLoopGroup workerGroup;

    /**
     * ThreadPool utilisé par netty pour gérer l"arrivé de packet simultanée
     */
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * Nombre de thread maximum utilisé par netty
     */
    private static final int THREAD_LIMIT = 10;

    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Channels
     */
    private ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    private INetworkEntity entity;

    public ServerNetwork(Kryo kryo, String ip, int port)
    {
        super(kryo, ip, port);
    }

    @Override
    public void run()
    {
        logger.info("Starting network service");
        try
        {
            this.bossGroup = new NioEventLoopGroup(1);
            this.workerGroup = new NioEventLoopGroup(THREAD_LIMIT, threadPool);
            this.bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.handler(new LoggingHandler(LogLevel.INFO));
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new KryoDecoder(kryo));
                    ch.pipeline().addLast(new KryoEncoder(kryo));
                    ch.pipeline().addLast(new ServerNetworkHandler());
                }
            });
            this.channel = bootstrap.bind(ip, port).sync().channel();
            isStarted = true;
            logger.info("Network service is ready");

            while (isStarted)
            {
                if(channels.size() > 0 && packetQueue.size() > 0)
                {
                    for(PairPacket pair : packetQueue)
                    {
                        for(Channel channel : channels)
                        {
                            if(channel.remoteAddress().equals(entity.getAddress()))
                            {
                                logger.debug("Sending " + pair.getPacket().getClass().getSimpleName() + " packet");
                                channel.writeAndFlush(pair.getPacket());
                                packetQueue.remove(pair);
                            }
                        }
                    }
                }
            }

            logger.info("closing network service");
            channel.closeFuture().sync();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void sendPacket(IPacket packet, INetworkEntity entity)
    {
        packetQueue.add(new PairPacket<>(packet, entity));
    }

    private class ServerNetworkHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
        {
            super.channelReadComplete(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception
        {
            channels.add(ctx.channel());
            entity = new NetworkEntity(ctx.channel().remoteAddress());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
        {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
