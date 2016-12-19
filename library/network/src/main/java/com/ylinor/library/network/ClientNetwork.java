package com.ylinor.library.network;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.kryo.KryoDecoder;
import com.ylinor.library.network.kryo.KryoEncoder;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;
import com.ylinor.library.network.protocol.IProtocol;
import com.ylinor.library.network.util.PairPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.wytrem.logging.Logger;
import net.wytrem.logging.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
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

    /**
     * Logger
     */
    private Logger logger = LoggerFactory.getLogger(getClass());

    public ClientNetwork(Kryo kryo, String ip, int port, IProtocol protocol)
    {
        super(kryo, ip, port, protocol);
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

            isStarted = true;
            logger.info("Network service is ready, waiting for packet...");
            while (isStarted)
            {
                if(packetQueue.size() == 0)
                {
                    continue;
                }

                for(PairPacket pair : packetQueue)
                {
                    logger.debug("Sending " + pair.getPacket().getClass().getSimpleName() + " packet");
                    channel.writeAndFlush(pair.getPacket());
                    packetQueue.remove(pair);
                }
            }

            logger.info("Stopping network service");
            channel.closeFuture().sync();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            group.shutdownGracefully();
            logger.info("Network service has successfully been stopped");
        }
    }

    @Override
    public void sendPacket(IPacket packet, INetworkEntity entity)
    {
        packetQueue.add(new PairPacket<>(packet,entity));
    }

    private class ClientNetworkHandler extends ChannelInboundHandlerAdapter
    {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
        {
            protocol.handlePacket((IPacket) msg, new INetworkEntity()
            {
                @Override
                public SocketAddress getAddress()
                {
                    return ctx.channel().remoteAddress();
                }
            });
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
