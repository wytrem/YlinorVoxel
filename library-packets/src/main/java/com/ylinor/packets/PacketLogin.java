package com.ylinor.packets;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class PacketLogin extends Packet {
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        
    }
    
    @Override
    public void handle(PacketHandler handler) {
        handler.handleLogin(this);
    }
}
