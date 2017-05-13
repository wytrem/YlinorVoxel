package com.ylinor.library.api.protocol.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.api.protocol.PacketSender;

public class PacketCharacterSelection extends Packet {

    @Override
    public void write(Kryo kryo, Output output) {
        
    }

    @Override
    public void read(Kryo kryo, Input input) {
        
    }

    @Override
    public void handle(PacketSender sender, PacketHandler handler) {
        handler.handleCharacterSelection(sender, this);
    }

}
