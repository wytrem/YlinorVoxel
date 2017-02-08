package com.ylinor.client.renderlib.format;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ylinor.client.renderlib.PrimitiveType;


public class VertexFormatElement {
    private final PrimitiveType type;
    private final Usage usage;
    private final int elementCount;

    public VertexFormatElement(PrimitiveType type, Usage usage, int count) {
        this.type = type;
        this.usage = usage;
        this.elementCount = count;
    }

    public final PrimitiveType getType() {
        return this.type;
    }

    public final Usage getUsage() {
        return this.usage;
    }

    public final int getElementCount() {
        return this.elementCount;
    }

    public String toString() {
        return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
    }

    public final int getSize() {
        return this.type.getSize() * this.elementCount;
    }

    public final boolean isPositionElement() {
        return this.usage == Usage.POSITION;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + elementCount;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((usage == null) ? 0 : usage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VertexFormatElement other = (VertexFormatElement) obj;
        if (elementCount != other.elementCount)
            return false;
        if (type != other.type)
            return false;
        if (usage != other.usage)
            return false;
        return true;
    }

    public static enum Usage {
        POSITION("Position"),
        NORMAL("Normal"),
        COLOR("Vertex Color"),
        TEX_COORDS("Tex Coords"),
        MATRIX("Bone Matrix"),
        BLEND_WEIGHT("Blend Weight"),
        PADDING("Padding"),
        OTHER("Other");

        private final String displayName;

        private Usage(String displayNameIn) {
            this.displayName = displayNameIn;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }

    private int gdxUsage() {
        switch (this.usage) {
            case POSITION:
                return com.badlogic.gdx.graphics.VertexAttributes.Usage.Position;
            case COLOR:
                if (type == PrimitiveType.BYTE) {
                    return com.badlogic.gdx.graphics.VertexAttributes.Usage.ColorPacked;
                }
                return com.badlogic.gdx.graphics.VertexAttributes.Usage.ColorUnpacked;
            case NORMAL:
                return com.badlogic.gdx.graphics.VertexAttributes.Usage.Normal;
            case TEX_COORDS:
                return com.badlogic.gdx.graphics.VertexAttributes.Usage.TextureCoordinates;
            default:
                return 0;
        }
    }

    private String gdxAlias() {
        switch (this.usage) {
            case POSITION:
                return ShaderProgram.POSITION_ATTRIBUTE;
            case COLOR:
                return ShaderProgram.COLOR_ATTRIBUTE;
            case NORMAL:
                return ShaderProgram.NORMAL_ATTRIBUTE;
            case TEX_COORDS:
                return ShaderProgram.TEXCOORD_ATTRIBUTE + "0";
            default:
                return "unknown";
        }
    }

    public VertexAttribute toGdx() {
        try {
            return gdxAttribute.newInstance(gdxUsage(), elementCount, type.getGlConstant(), false, gdxAlias());
        }
        catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Constructor<VertexAttribute> gdxAttribute;

    static {
        try {
            gdxAttribute = VertexAttribute.class.getDeclaredConstructor(int.class, int.class, int.class, boolean.class, String.class);
            gdxAttribute.setAccessible(true);
        }
        catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
