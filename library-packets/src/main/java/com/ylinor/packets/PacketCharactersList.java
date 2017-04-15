package com.ylinor.packets;

import java.util.Arrays;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public final class PacketCharactersList extends Packet {
    private List<String> charactersList;

    public PacketCharactersList() {

    }

    public PacketCharactersList(List<String> characterList) {
        this.charactersList = characterList;
    }

    @Override
    public void handle(PacketHandler handler) {

    }

    @Override
    public void write(Kryo kryo, Output output) {
        output.writeByte(charactersList.size());

        for (String name : charactersList) {
            output.writeString(name);
        }
    }

    @Override
    public void read(Kryo kryo, Input input) {
        int listSize = input.readByteUnsigned();
        String[] names = new String[listSize];

        for (int i = 0; i < listSize; i++) {
            names[i] = input.readString();
        }

        this.charactersList = Arrays.asList(names);
    }

    @Override
    public String toString() {
        return "PacketCharactersList [charactersList=" + charactersList + "]";
    }
}
