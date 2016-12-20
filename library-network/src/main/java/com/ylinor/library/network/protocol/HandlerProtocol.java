package com.ylinor.library.network.protocol;

import com.ylinor.library.network.handler.IPacketHandler;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pierre
 * @since 1.0.0
 */
public class HandlerProtocol implements IProtocol
{
    private Map<Class<? extends IPacket>, IPacketHandler> packetMap = new HashMap<>();

    @Override
    public void handlePacket(IPacket packet, INetworkEntity entity)
    {
        for(Map.Entry<Class<? extends IPacket>, IPacketHandler> thePacket : packetMap.entrySet())
        {
            if(packet.getClass().equals(thePacket.getClass()))
            {
                thePacket.getValue().preHandler(packet, entity);
            }
        }
    }

    @Override
    public void registerPacket(Class<? extends IPacket> packet, IPacketHandler handler)
    {
        packetMap.put(packet, handler);
    }
}
