package com.ylinor.packets;

import net.mostlyoriginal.api.network.marshal.common.MarshalDictionary;


public class NetworkableObjects {
    public static final MarshalDictionary MARSHAL_DICTIONARY;
    private static int nextId = 0;

    static {
        MARSHAL_DICTIONARY = new MarshalDictionary();
        {
            registerType(PacketLogin.class);
        }
    }

    private static final void registerType(Class<?> clazz) {
        MARSHAL_DICTIONARY.register(Integer.valueOf(nextId++), clazz);
    }
}
