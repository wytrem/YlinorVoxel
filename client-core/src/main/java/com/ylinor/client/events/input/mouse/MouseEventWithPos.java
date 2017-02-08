package com.ylinor.client.events.input.mouse;

/**
 * Stands for an event that provides the cursor's location.
 *
 * @author wytrem
 */
public class MouseEventWithPos extends MouseEvent {
    public final int mouseX, mouseY;

    public MouseEventWithPos(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }
}
