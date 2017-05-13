package com.ylinor.library.api.protocol.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.api.protocol.PacketSender;


public final class PacketPositionAndRotationUpdate extends EntityPacket {
    private float x, y, z, pitch, yaw;

    public PacketPositionAndRotationUpdate() {

    }

    public PacketPositionAndRotationUpdate(int entityId, float x, float y, float z, float pitch, float yaw) {
        super(entityId);
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    @Override
    public void handle(PacketSender sender, PacketHandler handler) {
        handler.handlePositionUpdate(sender, this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        super.write(kryo, output);
        output.writeFloat(x);
        output.writeFloat(y);
        output.writeFloat(z);
        output.writeFloat(pitch);
        output.writeFloat(yaw);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        super.read(kryo, input);
        this.x = input.readFloat();
        this.y = input.readFloat();
        this.z = input.readFloat();
        this.pitch = input.readFloat();
        this.yaw = input.readFloat();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }
}
