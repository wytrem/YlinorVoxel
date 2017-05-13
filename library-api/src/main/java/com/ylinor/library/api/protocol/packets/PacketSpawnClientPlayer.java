package com.ylinor.library.api.protocol.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.api.protocol.PacketSender;


public class PacketSpawnClientPlayer extends EntityPacket {
    
    private float x, y, z;
    
    public PacketSpawnClientPlayer() {
    }
    
    public PacketSpawnClientPlayer(int entityId, float x, float y, float z) {
        super(entityId);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }
    
    public float getY() {
        return y;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        super.write(kryo, output);
        output.writeFloat(x);
        output.writeFloat(y);
        output.writeFloat(z);
    }
    
    @Override
    public void read(Kryo kryo, Input input) {
        super.read(kryo, input);
        x = input.readFloat();
        y = input.readFloat();
        z = input.readFloat();
    }
    
    @Override
    public void handle(PacketSender sender, PacketHandler handler) {
        handler.handleSpawnClientPlayer(sender, this);
    }
}
