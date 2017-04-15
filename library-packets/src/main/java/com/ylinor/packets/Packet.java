package com.ylinor.packets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public abstract class Packet implements KryoSerializable {
    private static final List<Class<? extends Packet>> packetsList;

    static {
        packetsList = new ArrayList<>();

        registerPacket(PacketLogin.class);
    }

    private static void registerPacket(Class<? extends Packet> packetClass) {
        packetsList.add(packetClass);
    }

    public static void registerToKryo(Kryo kryo) {
        packetsList.forEach(packetClass -> kryo.register(packetClass));
    }

    protected UUID readUUID(Input input) {
        return new UUID(input.readLong(), input.readLong());
    }

    protected void writeUUID(Output output, UUID uuid) {
        output.writeLong(uuid.getMostSignificantBits());
        output.writeLong(uuid.getLeastSignificantBits());
    }

    public abstract void handle(PacketHandler handler);
}
