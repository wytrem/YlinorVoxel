package com.ylinor.packets;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;


public final class PacketLogin extends Packet {
    private String token;

    public PacketLogin() {

    }

    public PacketLogin(String playerToken) {
        this.token = playerToken;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeString(token);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.token = input.readString();
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleLogin(sender, this);
    }

    @Override
    public String toString() {
        return "PacketLogin [authToken=" + token + "]";
    }

    public String getAuthToken() {
        return token;
    }
}
