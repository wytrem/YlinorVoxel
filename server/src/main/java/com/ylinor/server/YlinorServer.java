package com.ylinor.server;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.ServerNetwork;
import com.ylinor.library.network.protocol.HandlerProtocol;
import com.ylinor.library.packets.MyNetworkEntity;
import com.ylinor.library.packets.Packet0KeepAlive;


public class YlinorServer {
    public static void main(String[] args) {
        HandlerProtocol<MyNetworkEntity> protocol = new HandlerProtocol<>();
        protocol.registerPacket(Packet0KeepAlive.class, (packet, sender, receiver) -> {
            System.out.println("J'ai reçu un packet " + packet.getRandomID() + " de l'ip " + sender.getRemoteAddress()
                                                                                                   .toString() + " par le manager " + receiver);
            receiver.sendPacket(new Packet0KeepAlive(3), sender);
        });

        ServerNetwork<MyNetworkEntity> serverNetwork = new ServerNetwork<>(new Kryo(), "127.0.0.1", 25565, protocol, MyNetworkEntity::new);
        serverNetwork.start();
    }
}
