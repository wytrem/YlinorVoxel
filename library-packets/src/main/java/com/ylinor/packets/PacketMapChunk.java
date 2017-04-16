package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.api.terrain.Chunk;

public class PacketMapChunk extends Packet {
    
    private short[] blocks;
    private byte[] states;

    public PacketMapChunk() {
    }
    
    public PacketMapChunk(Chunk chunk) {
        blocks = chunk.getBlocksArray();
        states = chunk.getStatesArray();
    }
    
    @Override
    public void write(Kryo kryo, Output output) {
        output.write(blocks.length);
        output.writeShorts(blocks);
        output.write(states.length);
        output.writeBytes(states);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        blocks = input.readShorts(input.readInt());
        states = input.readBytes(input.readInt());
    }

    @Override
    public void handle(PacketHandler handler) {
        handler.handleMapChunk(this);
    }
}
