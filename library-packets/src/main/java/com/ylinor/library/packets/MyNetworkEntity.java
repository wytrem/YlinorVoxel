package com.ylinor.library.packets;

import java.net.SocketAddress;

import com.ylinor.library.network.packet.INetworkEntity;

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
    public SocketAddress getRemoteAddress()
    {
        return address;
    }
}
