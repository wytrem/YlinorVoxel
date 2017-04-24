package com.ylinor.library.api.ecs.components;

import com.ylinor.library.util.ecs.component.Component;


public class Player extends Component {
    public String name;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }
}
