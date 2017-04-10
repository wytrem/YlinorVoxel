package com.ylinor.packets;

import com.esotericsoftware.kryo.KryoSerializable;

public abstract class Packet implements KryoSerializable {

    public abstract void handle(PacketHandler handler);
}
