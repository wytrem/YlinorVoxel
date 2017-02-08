package com.ylinor.client.events.input.keyboard;

/**
 * Called when a key is pressed.
 *
 * @author wytrem
 */
public class KeyDownEvent extends KeyboardEvent {
    public final int keycode;

    public KeyDownEvent(int keycode) {
        this.keycode = keycode;
    }
}
