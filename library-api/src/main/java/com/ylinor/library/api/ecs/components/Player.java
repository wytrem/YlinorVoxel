package com.ylinor.library.api.ecs.components;

import com.ylinor.auth.client.YlinorUser;
import com.ylinor.library.api.protocol.PacketSender;
import com.ylinor.library.util.ecs.component.Component;


public class Player extends Component {
    public PacketSender sender;

    public Player() {
    }

    public Player(PacketSender sender) {
        this.sender = sender;
    }
    
    public PacketSender getSenderObject() {
        return sender;
    }
    
    public YlinorUser getUser() {
        return sender.getUser();
    }
}
