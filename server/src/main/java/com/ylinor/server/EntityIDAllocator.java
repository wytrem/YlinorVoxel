package com.ylinor.server;

/**
 * Created by Utilisateur on 18/04/2017.
 */
public final class EntityIDAllocator {
    private static long nextEntityID = 0L;

    public static long allocateEntityID() {
        return nextEntityID++;
    }
}
