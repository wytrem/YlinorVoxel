package com.ylinor.library.network.handler;

import com.ylinor.library.network.AbstractNetwork;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.Packet;


/**
 * All packet handler will implement this generic interface with the packet as T
 * and the EntityNetwork (who send the packet) as U
 *
 * @author pierre
 * @since 1.0.0
 */
public interface IPacketHandler<T extends Packet, U extends INetworkEntity> {

    /**
     * Call by castAndHandle, all actions for the received packet in this method
     *
     * @param packet the received packet
     * @param networkEntity the entity who sent the packet
     * @param receiver the packet target
     */
    void handle(T packet, U networkEntity, AbstractNetwork<? super U> receiver);

    /**
     * Call by HandlerProtocol
     *
     * @param packet the packet
     * @param entity the entity who sent the message
     * @param receiver the packet target
     */
    @SuppressWarnings("unchecked")
    default void castAndHandle(Packet packet, U entity, AbstractNetwork<? super U> receiver) {
        handle((T) packet, entity, receiver);
    }
}
