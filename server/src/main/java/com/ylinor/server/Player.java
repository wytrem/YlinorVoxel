package com.ylinor.server;

import com.ylinor.library.util.ecs.component.Component;

public class Player extends Component {
    private final PlayerConnection playerConnection;

    public Player(PlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    public PlayerConnection getPlayerConnection() {
        return playerConnection;
    }
}
