package com.ylinor.client.events.input.mouse;

/**
 * A mouse event that holds a pointer field (?) and a button.
 *
 * @author wytrem
 */
abstract class MouseButtonEvent extends MouseEventWithPos {

    public final int pointer, button;

    public MouseButtonEvent(int mouseX, int mouseY, int pointer, int button) {
        super(mouseX, mouseY);
        this.pointer = pointer;
        this.button = button;
    }
}
