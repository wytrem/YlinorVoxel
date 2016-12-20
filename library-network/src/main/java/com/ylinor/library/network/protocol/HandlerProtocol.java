package com.ylinor.library.network.protocol;

import java.util.HashMap;
import java.util.Map;

import com.ylinor.library.network.AbstractNetwork;
import com.ylinor.library.network.handler.IPacketHandler;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.Packet;

/**
 * @author pierre
 * @since 1.0.0
 */
public class HandlerProtocol<E extends INetworkEntity> implements IProtocol<E>
{
    private Map<Class<? extends Packet>, IPacketHandler<?, E>> packetMap = new HashMap<>();

    @Override
    public void handlePacket(Packet packet, E entity, AbstractNetwork<? super E> receiver)
    {
        for(Map.Entry<Class<? extends Packet>, IPacketHandler<?, E>> handlerEntry : packetMap.entrySet())
        {
            if(packet.getClass().equals(handlerEntry.getKey()))
            {
                handlerEntry.getValue().castAndHandle(packet, entity, receiver);
            }
        }
    }

    public <P extends Packet> void registerPacket(Class<P> packet, IPacketHandler<P, E> handler)
    {
        packetMap.put(packet, handler);
    }
}
