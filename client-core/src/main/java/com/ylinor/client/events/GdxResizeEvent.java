package com.ylinor.client.events;

public class GdxResizeEvent implements ClientEvent {
    public final int width, height;

    public GdxResizeEvent(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }
}
