package com.ylinor.library.network.protocol;

import com.ylinor.library.network.AbstractNetwork;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.Packet;

/**
 * @author pierre
 * @since 1.0.0
 */
public interface IProtocol<E extends INetworkEntity>
{
    void handlePacket(Packet packet, E sender, AbstractNetwork<? super E> receiver);
}
