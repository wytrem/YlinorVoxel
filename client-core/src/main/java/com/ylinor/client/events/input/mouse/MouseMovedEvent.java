package com.ylinor.client.events.input.mouse;

/**
 * Called when the mouse moves.
 *
 * @author wytrem
 */
public class MouseMovedEvent extends MouseEventWithPos {
    public MouseMovedEvent(int mouseX, int mouseY) {
        super(mouseX, mouseY);
    }
}
