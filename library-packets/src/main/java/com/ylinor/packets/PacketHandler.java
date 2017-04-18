package com.ylinor.packets;

public interface PacketHandler {
    void handleLogin(PacketLogin login);
    
    void handleMapChunk(PacketMapChunk packetMapChunk);

    void handleSpawnEntity(PacketSpawnEntity spawnEntity);

    void handlePositionUpdate(PacketPositionUpdate positionUpdate);

    void handleDisconnect(PacketDisconnect disconnect);
}
