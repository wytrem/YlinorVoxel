package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public final class PacketPositionUpdate extends Packet {
    private long entityID;
    private float x, y, z;

    public PacketPositionUpdate() {

    }

    public PacketPositionUpdate(long entityID, float x, float y, float z) {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handlePositionUpdate(this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeLong(entityID);
        output.writeFloat(x);
        output.writeFloat(y);
        output.writeFloat(z);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.entityID = input.readLong();
        this.x = input.readFloat();
        this.y = input.readFloat();
        this.z = input.readFloat();
    }

    public long getEntityID() {
        return entityID;
    }

    public void setEntityID(long entityID) {
        this.entityID = entityID;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }
}
