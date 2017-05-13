package com.ylinor.library.api.protocol.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.api.protocol.PacketSender;


public final class PacketCharactersList extends Packet {

    public PacketCharactersList() {

    }

    @Override
    public void handle(PacketSender sender, PacketHandler handler) {
        handler.handleCharacterList(sender, this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
    }

    @Override
    public void read(Kryo kryo, Input input) {
    }
}
