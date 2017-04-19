package com.ylinor.client.network;

import com.ylinor.library.util.ecs.component.Component;

public final class NetworkIdentifierComponent extends Component {
    private long identifier;

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }
}
