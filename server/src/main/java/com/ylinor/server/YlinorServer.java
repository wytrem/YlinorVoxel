package com.ylinor.server;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.ServerNetwork;
import com.ylinor.library.network.protocol.HandlerProtocol;
import com.ylinor.library.network.protocol.IProtocol;
import com.ylinor.library.packets.Packet0KeepAlive;

public class YlinorServer
{

    public static void main(String[] args)
    {
        IProtocol protocol = new HandlerProtocol();
        protocol.registerPacket(Packet0KeepAlive.class, (packet, networkEntity) -> System.out.println("bonjour"));

        ServerNetwork serverNetwork = new ServerNetwork(new Kryo(), "0.0.0.0", 25565, protocol);
        serverNetwork.start();
    }

}
