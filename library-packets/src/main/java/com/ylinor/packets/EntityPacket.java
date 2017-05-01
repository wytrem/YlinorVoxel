package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public abstract class EntityPacket extends Packet {
    private int entityId;
    
    public EntityPacket() {
    }
    
    public EntityPacket(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(entityId);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        entityId = input.readInt();
    }
    
    public final int getEntityId() {
        return entityId;
    }
}
