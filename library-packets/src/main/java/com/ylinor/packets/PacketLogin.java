package com.ylinor.packets;

import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class PacketLogin extends Packet {
    
    private int id;
    
    public PacketLogin() {
        id = UUID.randomUUID().hashCode();
    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.write(id);
    }

    @Override
    public void read(Kryo kryo, Input input) {
        id = input.read();
    }
    
    @Override
    public void handle(PacketHandler handler) {
        handler.handleLogin(this);
    }

    @Override
    public String toString() {
        return "PacketLogin [id=" + id + "]";
    }
}
