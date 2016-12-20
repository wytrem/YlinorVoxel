package com.ylinor.server;

import java.util.Random;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.ClientNetwork;
import com.ylinor.library.network.packet.ServerEntity;
import com.ylinor.library.network.protocol.HandlerProtocol;
import com.ylinor.library.packets.Packet0KeepAlive;


public class YlinorClient
{
    public static void main(String[] args)
    {
        HandlerProtocol<ServerEntity> protocol = new HandlerProtocol<>();
        protocol.registerPacket(Packet0KeepAlive.class, (packet, sender, receiver) -> System.out.println("J'ai reçu un packet " + packet.getRandomID() + " de l'ip " + sender.getRemoteAddress().toString() + " par le manager " + receiver));

        ClientNetwork<ServerEntity> clientNetwork = new ClientNetwork<>(new Kryo(), "127.0.0.1", 25565, protocol, ServerEntity::new);
        clientNetwork.start();
        
        clientNetwork.sendPacket(new Packet0KeepAlive(new Random().nextInt(120)));
    }
}
