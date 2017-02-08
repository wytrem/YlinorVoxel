package com.ylinor.client.events.input.mouse;

/**
 * Called when the user presses a mouse button.
 *
 * @author wytrem
 */
public class MouseTouchDownEvent extends MouseButtonEvent {
    public MouseTouchDownEvent(int mouseX, int mouseY, int pointer, int button) {
        super(mouseX, mouseY, pointer, button);
    }
}
