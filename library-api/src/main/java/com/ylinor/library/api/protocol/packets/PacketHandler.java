package com.ylinor.library.api.protocol.packets;

import com.ylinor.library.api.protocol.PacketSender;

public interface PacketHandler {
    void handleLogin(PacketSender sender, PacketLogin login);

    void handleMapChunk(PacketSender sender, PacketMapChunk packetMapChunk);

    void handleSpawnEntity(PacketSender sender, PacketSpawnEntity spawnEntity);

    void handleDespawnEntity(PacketSender sender, PacketDespawnEntity despawnEntity);

    void handlePositionUpdate(PacketSender sender, PacketPositionAndRotationUpdate positionUpdate);

    void handleDisconnect(PacketSender sender, PacketDisconnect disconnect);

    void handleSpawnClientPlayer(PacketSender sender, PacketSpawnClientPlayer spawnClientPlayer);

    void handleCharacterSelection(PacketSender sender, PacketCharacterSelection packetCharacterSelection);
    
    void handleCharacterList(PacketSender sender, PacketCharactersList packetCharactersList);
}
