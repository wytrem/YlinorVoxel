package com.ylinor.library.network.packets;

import com.ylinor.library.network.packet.INetworkEntity;

import java.net.SocketAddress;

/**
 * @author pierre
 * @since 1.0.0
 */
public class NetworkEntity implements INetworkEntity
{

    private SocketAddress socketAddress;

    public NetworkEntity(SocketAddress socketAddress)
    {
        this.socketAddress = socketAddress;
    }

    @Override
    public SocketAddress getAddress()
    {
        return socketAddress;
    }
}
