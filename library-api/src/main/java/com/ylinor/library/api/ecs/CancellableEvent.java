package com.ylinor.library.api.ecs;

import net.mostlyoriginal.api.event.common.Event;

import net.mostlyoriginal.api.event.common.Cancellable;


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
