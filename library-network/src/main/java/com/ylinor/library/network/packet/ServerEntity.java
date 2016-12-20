package com.ylinor.library.network.packet;

import java.net.SocketAddress;

public class ServerEntity implements INetworkEntity
{
    private SocketAddress address;
    
    public ServerEntity(SocketAddress address)
    {
        this.address = address;
    }

    @Override
    public SocketAddress getRemoteAddress()
    {
        return address;
    }
}
