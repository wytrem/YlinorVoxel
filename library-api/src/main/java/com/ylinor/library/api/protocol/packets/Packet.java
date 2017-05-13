package com.ylinor.library.api.protocol.packets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ylinor.library.api.protocol.PacketSender;


public abstract class Packet implements KryoSerializable {
    private static final List<Class<? extends Packet>> packetsList;

    static {
        packetsList = new ArrayList<>();

        registerPacket(PacketLogin.class);
        registerPacket(PacketPositionAndRotationUpdate.class);
        registerPacket(PacketSpawnEntity.class);
        registerPacket(PacketDespawnEntity.class);
        registerPacket(PacketDisconnect.class);
        registerPacket(PacketSpawnClientPlayer.class);
        registerPacket(PacketCharacterSelection.class);
        registerPacket(PacketCharactersList.class);
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

    public abstract void handle(PacketSender sender, PacketHandler handler);
}
