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
}
