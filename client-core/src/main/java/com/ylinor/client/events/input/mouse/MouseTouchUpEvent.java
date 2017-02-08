package com.ylinor.client.events.input.mouse;

/**
 * Called when the user releases a mouse button.
 *
 * @author wytrem
 */
public class MouseTouchUpEvent extends MouseButtonEvent {
    public MouseTouchUpEvent(int mouseX, int mouseY, int pointer, int button) {
        super(mouseX, mouseY, pointer, button);
    }
}