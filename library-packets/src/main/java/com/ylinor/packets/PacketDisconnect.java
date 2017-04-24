package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;


public final class PacketDisconnect extends Packet {
    private String reason;

    public PacketDisconnect() {

    }

    public PacketDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
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

    public void setReason(String reason) {
        this.reason = reason;
    }
}
