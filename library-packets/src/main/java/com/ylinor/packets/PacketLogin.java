package com.ylinor.packets;

import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.util.ecs.entity.Entity;


public final class PacketLogin extends Packet {
    private UUID authToken;

    public PacketLogin() {

    }

    public PacketLogin(UUID playerToken) {
        this.authToken = playerToken;
    }

    @Override
    public void write(Kryo kryo, Output output) {
        writeUUID(output, authToken);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        this.authToken = readUUID(input);
    }

    @Override
    public void handle(Entity sender, PacketHandler handler) {
        handler.handleLogin(sender, this);
    }

    @Override
    public String toString() {
        return "PacketLogin [authToken=" + authToken + "]";
    }

    public UUID getAuthToken() {
        return authToken;
    }

    public void setAuthToken(UUID authToken) {
        this.authToken = authToken;
    }
}
