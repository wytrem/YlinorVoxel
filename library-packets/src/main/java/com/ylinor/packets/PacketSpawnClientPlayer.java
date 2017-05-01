package com.ylinor.packets;

import com.ylinor.library.util.ecs.entity.Entity;


public class PacketSpawnClientPlayer extends EntityPacket {
    public PacketSpawnClientPlayer() {
    }

    public PacketSpawnClientPlayer(int entityId) {
        super(entityId);
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleSpawnClientPlayer(sender, this);
    }
}
