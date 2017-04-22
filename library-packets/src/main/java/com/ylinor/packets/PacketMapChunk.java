package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.github.luben.zstd.Zstd;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.util.ArrayUtils;
import com.ylinor.library.util.ecs.entity.Entity;

public class PacketMapChunk extends Packet {
    
    private byte[] blocks;
    private byte[] states;

    public PacketMapChunk() {
    }
    
    public PacketMapChunk(Chunk chunk) {
        blocks = Zstd.compress(ArrayUtils.toByteArray(chunk.getBlocksArray()));
        states = Zstd.compress(chunk.getStatesArray());
    }
    
    public void populateChunk(Chunk chunk) {
        byte[] temp = new byte[(int) Zstd.decompressedSize(blocks)];
        byte[] temp2 = new byte[(int) Zstd.decompressedSize(states)];
        Zstd.decompress(temp, blocks);
        Zstd.decompress(temp2, states);
        chunk.fillChunk(ArrayUtils.toShortArray(temp), temp2);
    }
    
    @Override
    public void write(Kryo kryo, Output output) {
        output.writeInt(blocks.length);
        output.write(blocks);
        output.writeInt(states.length);
        output.write(states);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        blocks = input.readBytes(input.readInt());
        states = input.readBytes(input.readInt());
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleMapChunk(sender, this);
    }
}
