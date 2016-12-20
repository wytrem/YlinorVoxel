package com.ylinor.library.network.handler;

import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;

/**
 * @author pierre
 * @since 1.0.0
 */
public interface IPacketHandler<T extends IPacket, U extends INetworkEntity>
{

    void handle(T packet, U networkEntity);

    default void preHandler(IPacket packet, INetworkEntity entity)
    {
        handle((T) packet, (U) entity);
    }

}
