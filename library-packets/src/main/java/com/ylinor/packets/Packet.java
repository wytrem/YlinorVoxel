package com.ylinor.packets;

import java.io.Externalizable;


public abstract class Packet implements Externalizable {

    public abstract void handle(PacketHandler handler);
}
