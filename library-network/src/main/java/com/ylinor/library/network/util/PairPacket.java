package com.ylinor.library.network.util;

import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;

/**
 * @author pierre
 * @since 1.0.0
 */
public class PairPacket<T extends IPacket, U extends INetworkEntity>
{

    private IPacket packet;
    private INetworkEntity sender;

    public PairPacket(T packet, U networkEntity)
    {
        this.packet = packet;
        this.sender = networkEntity;
    }

    public IPacket getPacket()
    {
        return packet;
    }

    public void setPacket(IPacket packet)
    {
        this.packet = packet;
    }

    public INetworkEntity getSender()
    {
        return sender;
    }

    public void setSender(INetworkEntity sender)
    {
        this.sender = sender;
    }
}
