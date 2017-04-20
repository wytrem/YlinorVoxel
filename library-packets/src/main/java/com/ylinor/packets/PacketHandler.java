package com.ylinor.packets;

public interface PacketHandler {
    void handleLogin(PacketLogin login);
    
    void handleMapChunk(PacketMapChunk packetMapChunk);

    void handleSpawnEntity(PacketSpawnEntity spawnEntity);

    void handleDespawnEntity(PacketDespawnEntity despawnEntity);

    void handlePositionUpdate(PacketPositionAndRotationUpdate positionUpdate);

    void handleDisconnect(PacketDisconnect disconnect);
}
