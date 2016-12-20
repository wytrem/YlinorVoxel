package com.ylinor.library.network.handler;

import com.ylinor.library.network.AbstractNetwork;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.Packet;


/**
 * @author pierre
 * @since 1.0.0
 */
public interface IPacketHandler<T extends Packet, U extends INetworkEntity>
{
    void handle(T packet, U networkEntity, AbstractNetwork<? super U> receiver);

    @SuppressWarnings("unchecked")
    default void castAndHandle(Packet packet, INetworkEntity entity, AbstractNetwork<? super U> receiver)
    {
        handle((T) packet, (U) entity, receiver);
    }
}
