package com.ylinor.client.events.input.mouse;

/**
 * Called when the users drags the mouse (moves without releasing).
 *
 * @author wytrem
 */
public class MouseDraggedEvent extends MouseEventWithPos {
    public final int pointer;

    public MouseDraggedEvent(int mouseX, int mouseY, int pointer) {
        super(mouseX, mouseY);
        this.pointer = pointer;
    }
}
