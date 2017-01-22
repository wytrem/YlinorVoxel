package com.ylinor.client.renderlib;

import com.badlogic.gdx.graphics.GL20;


public enum PrimitiveType {
    FLOAT(Float.BYTES, "Float", GL20.GL_FLOAT),
    UBYTE(Byte.BYTES, "Unsigned Byte", GL20.GL_UNSIGNED_BYTE),
    BYTE(Byte.BYTES, "Byte", GL20.GL_BYTE),
    USHORT(Short.BYTES, "Unsigned Short", GL20.GL_UNSIGNED_SHORT),
    SHORT(Short.BYTES, "Short", GL20.GL_SHORT),
    UINT(Integer.BYTES, "Unsigned Int", GL20.GL_UNSIGNED_INT),
    INT(Integer.BYTES, "Int", GL20.GL_INT);

    private final int size;
    private final String displayName;
    private final int glConstant;

    private PrimitiveType(int sizeIn, String displayNameIn, int glConstantIn) {
        this.size = sizeIn;
        this.displayName = displayNameIn;
        this.glConstant = glConstantIn;
    }

    public int getSize() {
        return this.size;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getGlConstant() {
        return this.glConstant;
    }
}