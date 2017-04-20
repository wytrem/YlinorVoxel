package com.ylinor.library.api.ecs.events;

import com.ylinor.library.util.ecs.Cancellable;
import com.ylinor.library.util.ecs.Event;


public class CancellableEvent implements Cancellable, Event {

    private boolean isCancelled;

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean value) {
        this.isCancelled = value;
    }
}
