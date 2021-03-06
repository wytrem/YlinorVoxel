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

    public Icon reduce(int[] uv) {
        float baseSize = 16.0f;
        float iconSizeX = maxU - minU;
        float iconSizeY = maxV - minV;
        minU += (uv[0] / baseSize * iconSizeX);
        minV += (uv[1] / baseSize * iconSizeY);
        maxU -= ((baseSize - uv[2]) / baseSize * iconSizeX);
        maxV -= ((baseSize - uv[3]) / baseSize * iconSizeY);
        return this;
    }

    public Icon copy() {
        return new Icon(minU, minV, maxU, maxV);
    }

    @Override
    public String toString() {
        return "Icon{" + "minU=" + minU + ", minV=" + minV + ", maxU=" + maxU + ", maxV=" + maxV + '}';
    }

    public static Icon fromPosSize(float x, float y, float w, float h) {
        return new Icon(x, y, x + w, y + h);
    }
}
