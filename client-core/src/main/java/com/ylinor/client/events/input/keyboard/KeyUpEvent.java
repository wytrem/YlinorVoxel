package com.ylinor.client.events.input.keyboard;

/**
 * Called when a key is released.
 *
 * @author wytrem
 */
public class KeyUpEvent extends KeyboardEvent {
    public final int keycode;

    public KeyUpEvent(int keycode) {
        this.keycode = keycode;
    }
}
