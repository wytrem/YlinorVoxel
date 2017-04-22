package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;

public final class PacketDespawnEntity extends Packet {
    private int entityID;

    public PacketDespawnEntity() {

    }

    public PacketDespawnEntity(int entityID) {
        this.entityID = entityID;
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleDespawnEntity(sender, this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(entityID);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.entityID = input.readInt();
    }

    public long getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
        this.entityID = entityID;
    }
}
