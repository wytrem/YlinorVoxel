package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;


public final class PacketSpawnEntity extends Packet {
    private int entityID;
    private float initialX;
    private float initialY;
    private float initialZ;
    private float initialPitch;
    private float initialYaw;

    public PacketSpawnEntity() {

    }

    public PacketSpawnEntity(int entityId) {
        this(entityId, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    public PacketSpawnEntity(int entityID, float initialX, float initialY, float initialZ, float initialPitch, float initialYaw) {
        this.entityID = entityID;
        this.initialX = initialX;
        this.initialY = initialY;
        this.initialZ = initialZ;
        this.initialPitch = initialPitch;
        this.initialYaw = initialYaw;
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleSpawnEntity(sender, this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(entityID);
        output.writeFloat(initialX);
        output.writeFloat(initialY);
        output.writeFloat(initialZ);
        output.writeFloat(initialPitch);
        output.writeFloat(initialYaw);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.entityID = input.readInt();
        this.initialX = input.readFloat();
        this.initialY = input.readFloat();
        this.initialZ = input.readFloat();
        this.initialPitch = input.readFloat();
        this.initialYaw = input.readFloat();
    }

    public int getEntityID() {
        return entityID;
    }

    public void setEntityID(int entityID) {
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
