package com.ylinor.client.network;

import com.ylinor.library.util.ecs.component.Component;


public final class NetworkIdentifierComponent extends Component {
    private int identifier;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
