package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;


public final class PacketSpawnEntity extends EntityPacket {
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

    public PacketSpawnEntity(int entityId, float initialX, float initialY, float initialZ, float initialPitch, float initialYaw) {
        super(entityId);
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
        super.write(kryo, output);
        output.writeFloat(initialX);
        output.writeFloat(initialY);
        output.writeFloat(initialZ);
        output.writeFloat(initialPitch);
        output.writeFloat(initialYaw);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        super.read(kryo, input);
        this.initialX = input.readFloat();
        this.initialY = input.readFloat();
        this.initialZ = input.readFloat();
        this.initialPitch = input.readFloat();
        this.initialYaw = input.readFloat();
    }

    public float getInitialX() {
        return initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public float getInitialZ() {
        return initialZ;
    }

    public float getInitialPitch() {
        return initialPitch;
    }

    public float getInitialYaw() {
        return initialYaw;
    }
}
