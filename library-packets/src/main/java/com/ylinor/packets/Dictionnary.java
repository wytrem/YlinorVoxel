package com.ylinor.packets;

import net.mostlyoriginal.api.network.marshal.common.MarshalDictionary;

public class Dictionnary {
    private static final MarshalDictionary MARSHAL_DICTIONARY;

    static {
        MARSHAL_DICTIONARY = new MarshalDictionary();
        registerPacket(1, PacketLogin.class);
    }
    
    private static final void registerPacket(int id, Class<? extends Packet> clazz) {
        MARSHAL_DICTIONARY.register(Integer.valueOf(id), clazz);
    }
}
