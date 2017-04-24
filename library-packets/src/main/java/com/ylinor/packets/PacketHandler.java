package com.ylinor.packets;

import com.ylinor.library.util.ecs.entity.Entity;


public interface PacketHandler {
    void handleLogin(Entity sender, PacketLogin login);

    void handleMapChunk(Entity sender, PacketMapChunk packetMapChunk);

    void handleSpawnEntity(Entity sender, PacketSpawnEntity spawnEntity);

    void handleDespawnEntity(Entity sender, PacketDespawnEntity despawnEntity);

    void handlePositionUpdate(Entity sender, PacketPositionAndRotationUpdate positionUpdate);

    void handleDisconnect(Entity sender, PacketDisconnect disconnect);

    void handleSpawnClientPlayer(Entity sender, PacketSpawnClientPlayer spawnClientPlayer);
}
