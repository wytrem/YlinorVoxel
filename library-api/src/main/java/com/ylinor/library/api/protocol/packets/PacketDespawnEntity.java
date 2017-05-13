package com.ylinor.library.api.protocol.packets;

import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.api.protocol.PacketSender;


public final class PacketDespawnEntity extends EntityPacket {
    public PacketDespawnEntity() {

    }

    public PacketDespawnEntity(int entityId) {
        super(entityId);
    }

    @Override
    public void handle(PacketSender sender, PacketHandler handler) {
        handler.handleDespawnEntity(sender, this);
    }
}
