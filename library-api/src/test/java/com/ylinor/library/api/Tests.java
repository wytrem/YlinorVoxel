package com.ylinor.library.api;

import java.util.UUID;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


public class Tests {
    public static void main(String[] args) {

        TIntObjectMap<String> onlineConnections = new TIntObjectHashMap<>();

        for (int i = 0; i < 10; i++) {
            onlineConnections.put(i, UUID.randomUUID().toString());
        }

        TIntObjectIterator<String> iterator = onlineConnections.iterator();

        while (iterator.hasNext()) {
            iterator.advance();
            System.out.println("pair = " + iterator.key() + " -> " + iterator.value());
        }
    }

}
