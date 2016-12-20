package com.ylinor.library.packets;

import com.ylinor.library.network.packet.INetworkEntity;

import java.net.SocketAddress;

/**
 * @author pierre
 * @since 1.0.0
 */
public class MyNetworkEntity implements INetworkEntity
{

    private SocketAddress address;

    public MyNetworkEntity(SocketAddress address)
    {
        this.address = address;
    }

    @Override
    public SocketAddress getAddress()
    {
        return address;
    }
}
