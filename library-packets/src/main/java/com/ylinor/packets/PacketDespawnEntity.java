package com.ylinor.packets;

import com.ylinor.library.util.ecs.entity.Entity;


public final class PacketDespawnEntity extends EntityPacket {
    public PacketDespawnEntity() {

    }

    public PacketDespawnEntity(int entityId) {
        super(entityId);
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleDespawnEntity(sender, this);
    }
}
