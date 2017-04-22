package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;

public class PacketSpawnClientPlayer extends Packet {

    private int entityId;   
    
    public PacketSpawnClientPlayer() {
    }
    
    public PacketSpawnClientPlayer(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityId() {
        return entityId;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(entityId);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        entityId = input.readInt();
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleSpawnClientPlayer(sender, this);
    }
}
