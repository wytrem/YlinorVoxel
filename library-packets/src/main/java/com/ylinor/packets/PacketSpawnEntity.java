package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public final class PacketSpawnEntity extends Packet {
    private long entityID;
    private float initialX;
    private float initialY;
    private float initialZ;
    private float initialPitch;
    private float initialYaw;

    public PacketSpawnEntity() {

    }

    public PacketSpawnEntity(long entityID, float initialX, float initialY, float initialZ, float initialPitch, float initialYaw) {
        this.entityID = entityID;
        this.initialX = initialX;
        this.initialY = initialY;
        this.initialZ = initialZ;
        this.initialPitch = initialPitch;
        this.initialYaw = initialYaw;
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handleSpawnEntity(this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeLong(entityID);
        output.writeFloat(initialX);
        output.writeFloat(initialY);
        output.writeFloat(initialZ);
        output.writeFloat(initialPitch);
        output.writeFloat(initialYaw);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.entityID = input.readLong();
        this.initialX = input.readFloat();
        this.initialY = input.readFloat();
        this.initialZ = input.readFloat();
        this.initialPitch = input.readFloat();
        this.initialYaw = input.readFloat();
    }

    public long getEntityID() {
        return entityID;
    }

    public void setEntityID(long entityID) {
        this.entityID = entityID;
    }

    public float getInitialX() {
        return initialX;
    }

    public void setInitialX(float initialX) {
        this.initialX = initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public void setInitialY(float initialY) {
        this.initialY = initialY;
    }

    public float getInitialZ() {
        return initialZ;
    }

    public void setInitialZ(float initialZ) {
        this.initialZ = initialZ;
    }

    public float getInitialPitch() {
        return initialPitch;
    }

    public void setInitialPitch(float initialPitch) {
        this.initialPitch = initialPitch;
    }

    public float getInitialYaw() {
        return initialYaw;
    }

    public void setInitialYaw(float initialYaw) {
        this.initialYaw = initialYaw;
    }
}
