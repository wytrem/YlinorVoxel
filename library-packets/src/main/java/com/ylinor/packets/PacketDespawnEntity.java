package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public final class PacketDespawnEntity extends Packet {
    private long entityID;

    public PacketDespawnEntity() {

    }

    public PacketDespawnEntity(long entityID) {
        this.entityID = entityID;
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handleDespawnEntity(this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeLong(entityID);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.entityID = input.readLong();
    }

    public long getEntityID() {
        return entityID;
    }

    public void setEntityID(long entityID) {
        this.entityID = entityID;
    }
}
