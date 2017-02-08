package com.ylinor.client.events.input.keyboard;

/**
 * Called when a key is typed (pressed + released).
 *
 * @author wytrem
 */
public class KeyTypedEvent extends KeyboardEvent {
    public final char character;

    public KeyTypedEvent(char character) {
        this.character = character;
    }
}
