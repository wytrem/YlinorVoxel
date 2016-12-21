package com.ylinor.library.network;

import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.kryo.KryoDecoder;
import com.ylinor.library.network.kryo.KryoEncoder;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.Packet;
import com.ylinor.library.network.packet.ServerEntity;
import com.ylinor.library.network.protocol.IProtocol;
import com.ylinor.library.network.util.PairPacket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @author pierre
 * @since 1.0.0
 */
public class ClientNetwork<S extends ServerEntity> extends AbstractNetwork<S>
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
     * ThreadPool utilisé par netty pour gérer l'arrivé de packet simultanée
     */
    private ExecutorService threadPool = Executors.newCachedThreadPool();

    /**
     * Nombre de thread maximum utilisé par netty
     */
    private static final int THREAD_LIMIT = 10;

    private S serverEntity;

    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    private Function<SocketAddress, S> entitySupplier;

    public ClientNetwork(Kryo kryo, String ip, int port, IProtocol<S> protocol, Function<SocketAddress, S> entitySupplier)
    {
        super(kryo, ip, port, protocol);
        this.entitySupplier = entitySupplier;
    }

    @Override
    public void run()
    {
        logger.info("Starting network service...");
        try
        {
            this.group = new NioEventLoopGroup(THREAD_LIMIT, threadPool);
            this.bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    ch.pipeline().addLast(new KryoDecoder(kryo));
                    ch.pipeline().addLast(new KryoEncoder(kryo));
                    ch.pipeline().addLast(new ClientNetworkHandler());
                }
            });
            this.channel = bootstrap.connect(ip, port).sync().channel();
            this.serverEntity = entitySupplier.apply(this.channel.remoteAddress());

            isStarted = true;
            logger.info("Network service is ready, waiting for packet...");
            while (isStarted)
            {
                if (packetQueue.size() == 0)
                {
                    continue;
                }

                for (PairPacket<?, ?> pair : packetQueue)
                {
                    logger.debug("Sending " + pair.getPacket().getClass().getSimpleName() + " packet");
                    channel.writeAndFlush(pair.getPacket());
                    packetQueue.remove(pair);
                }
            }

            logger.info("Stopping network service");
            channel.closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            group.shutdownGracefully();
            logger.info("Network service has successfully been stopped");
        }
    }

    public void sendPacket(Packet packet)
    {
        sendPacket(packet, serverEntity);
    }

    @Override
    public void sendPacket(Packet packet, INetworkEntity entity)
    {
        packetQueue.add(new PairPacket<>(packet, entity));
    }

    private class ClientNetworkHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            protocol.handlePacket((Packet) msg, serverEntity, ClientNetwork.this);
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
