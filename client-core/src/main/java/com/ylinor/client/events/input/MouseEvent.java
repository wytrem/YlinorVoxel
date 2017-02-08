package com.ylinor.client.events.input;

import com.ylinor.library.api.ecs.CancellableEvent;


public class MouseEvent extends CancellableEvent implements InputEvent {
    public final int mouseX, mouseY, pointer;

    public MouseEvent(int mouseX, int mouseY, int pointer) {
        super();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.pointer = pointer;
    }
}
