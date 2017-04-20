package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public final class PacketPositionAndRotationUpdate extends Packet {
    private long entityID;
    private float x, y, z, pitch, yaw;

    public PacketPositionAndRotationUpdate() {

    }

    public PacketPositionAndRotationUpdate(long entityID, float x, float y, float z, float pitch, float yaw) {
        this.entityID = entityID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
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
        output.writeFloat(pitch);
        output.writeFloat(yaw);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.entityID = input.readLong();
        this.x = input.readFloat();
        this.y = input.readFloat();
        this.z = input.readFloat();
        this.pitch = input.readFloat();
        this.yaw = input.readFloat();
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

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
