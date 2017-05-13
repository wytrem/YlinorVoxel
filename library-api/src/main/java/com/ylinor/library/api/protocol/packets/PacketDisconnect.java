package com.ylinor.library.api.protocol.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.api.protocol.PacketSender;


public final class PacketDisconnect extends Packet {
    private String reason;

    public PacketDisconnect() {

    }

    public PacketDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void handle(PacketSender sender, PacketHandler handler) {
        handler.handleDisconnect(sender, this);
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(reason);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.reason = input.readString();
    }

    public String getReason() {
        return reason;
    }
}
