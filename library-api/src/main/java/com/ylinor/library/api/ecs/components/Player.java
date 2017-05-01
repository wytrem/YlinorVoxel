package com.ylinor.library.api.ecs.components;

import com.ylinor.auth.client.YlinorUser;
import com.ylinor.library.util.ecs.component.Component;


public class Player extends Component {
    public YlinorUser user;

    public Player() {
    }

    public Player(YlinorUser user) {
        this.user = user;
    }
}
