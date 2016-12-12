package com.ylinor.server;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.ServerNetwork;

public class YlinorServer
{

    public static void main(String[] args)
    {
        ServerNetwork serverNetwork = new ServerNetwork(new Kryo(), "0.0.0.0", 25565);
        serverNetwork.run();
    }

}
