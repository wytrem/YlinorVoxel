package com.ylinor.client.render.model;

/**
 *
 */
public class Icon {
    private float minU, minV, maxU, maxV;

    public Icon(float minU, float minV, float maxU, float maxV) {
        this.minU = minU;
        this.minV = minV;
        this.maxU = maxU;
        this.maxV = maxV;
    }

    public float getMinU() {
        return minU;
    }

    public float getMinV() {
        return minV;
    }

    public float getMaxU() {
        return maxU;
    }

    public float getMaxV() {
        return maxV;
    }

    public Icon scale(float x, float y) {
        this.minU /= x;
        this.minV /= y;
        this.maxU /= x;
        this.maxV /= y;
        return this;
    }

    public static Icon fromPosSize(float x, float y, float w, float h) {
        return new Icon(x, y, x + w, y + h);
    }
}
