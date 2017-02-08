package com.ylinor.client.events.input.mouse;

/**
 * Called when the users scrolls.
 *
 * @author wytrem
 */
public class MouseScrolledEvent extends MouseEvent {
    public final int amount;

    public MouseScrolledEvent(int amount) {
        this.amount = amount;
    }
}
