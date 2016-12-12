package com.ylinor.library.network.protocol;

import com.ylinor.library.network.handler.IPacketHandler;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;

/**
 * @author pierre
 * @since 1.0.0
 */
public interface IProtocol
{

    void handlePacket(IPacket packet, INetworkEntity entity);

    void registerPacket(Class<? extends IPacket> packet, IPacketHandler handler);

}
