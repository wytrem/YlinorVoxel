package com.ylinor.library.network;

import java.net.SocketAddress;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.kryo.KryoDecoder;
import com.ylinor.library.network.kryo.KryoEncoder;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.Packet;
import com.ylinor.library.network.protocol.IProtocol;
import com.ylinor.library.network.util.PairPacket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
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

/**
 * @author pierre
 * @since 1.0.0
 */
public class ServerNetwork<E extends INetworkEntity> extends AbstractNetwork<E>
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

    private Function<SocketAddress, E> entitySupplier;
    private CopyOnWriteArrayList<E> clients = new CopyOnWriteArrayList<>();
    
    public ServerNetwork(Kryo kryo, String ip, int port, IProtocol<E> protocol, Function<SocketAddress, E> entitySupplier)
    {
        super(kryo, ip, port, protocol);
        this.entitySupplier = entitySupplier;
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
                    E client = entitySupplier.apply(ch.remoteAddress());
                    clients.add(client);
                    ch.pipeline().addLast(new ServerNetworkHandler(client));
                }
            });
            this.channel = bootstrap.bind(ip, port).sync().channel();
            isStarted = true;
            logger.info("Network service is ready");

            while (isStarted)
            {
                if(channels.size() > 0 && packetQueue.size() > 0)
                {
                    for(PairPacket<?, ?> pair : packetQueue)
                    {
                        for(Channel channel : channels)
                        {
                            if(channel.remoteAddress().equals(pair.getSender().getRemoteAddress()))
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
    public void sendPacket(Packet packet, INetworkEntity entity)
    {
        packetQueue.add(new PairPacket<>(packet, entity));
    }

    private class ServerNetworkHandler extends ChannelInboundHandlerAdapter
    {
        private E client;
        
        public ServerNetworkHandler(E client)
        {
            this.client = client;
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            protocol.handlePacket((Packet) msg, client, ServerNetwork.this);
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
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
        {
            cause.printStackTrace();
            ctx.close();
        }
    }
}
