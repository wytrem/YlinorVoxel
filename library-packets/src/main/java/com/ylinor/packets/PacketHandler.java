package com.ylinor.packets;

public interface PacketHandler {
    void handleLogin(PacketLogin login);
    
    void handleMapChunk(PacketMapChunk packetMapChunk);
}
