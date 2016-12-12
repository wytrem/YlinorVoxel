package com.ylinor.library.network.protocol;

import com.ylinor.library.network.handler.IPacketHandler;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;

/**
 * @author pierre
 * @since 1.0.0
 */
public class HandlerProtocol implements IProtocol
{
    @Override
    public void handlePacket(IPacket packet, INetworkEntity entity)
    {

    }

    @Override
    public void registerPacket(Class<? extends IPacket> packet, IPacketHandler handler)
    {

    }
}
