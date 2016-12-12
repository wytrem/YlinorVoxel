package com.ylinor.server;

import com.ylinor.library.network.ServerNetwork;

public class YlinorServer
{

    public static void main(String[] args)
    {
        ServerNetwork serverNetwork = new ServerNetwork();
        serverNetwork.start("0.0.0.0", 25565);
    }

}
