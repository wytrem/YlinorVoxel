package com.ylinor.client.render;

public class VertexFormatElement {
    private final Type type;
    private final Usage usage;
    private final int index;
    private final int elementCount;

    public VertexFormatElement(int index, Type type, Usage usage, int count) {
        this.type = type;
        this.usage = usage;
        this.index = index;
        this.elementCount = count;
    }

    public final Type getType() {
        return this.type;
    }

    public final Usage getUsage() {
        return this.usage;
    }

    public final int getElementCount() {
        return this.elementCount;
    }

    public final int getIndex() {
        return this.index;
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

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        else if (other != null && this.getClass() == other.getClass()) {
            VertexFormatElement vertexformatelement = (VertexFormatElement) other;
            return this.elementCount != vertexformatelement.elementCount ? false : (this.index != vertexformatelement.index ? false : (this.type != vertexformatelement.type ? false : this.usage == vertexformatelement.usage));
        }
        else {
            return false;
        }
    }

    public int hashCode() {
        int i = this.type.hashCode();
        i = 31 * i + this.usage.hashCode();
        i = 31 * i + this.index;
        i = 31 * i + this.elementCount;
        return i;
    }

    public static enum Type {
        FLOAT(Float.BYTES, "Float", 5126),
        UBYTE(Byte.BYTES, "Unsigned Byte", 5121),
        BYTE(Byte.BYTES, "Byte", 5120),
        USHORT(Short.BYTES, "Unsigned Short", 5123),
        SHORT(Short.BYTES, "Short", 5122),
        UINT(Integer.BYTES, "Unsigned Int", 5125),
        INT(Integer.BYTES, "Int", 5124);

        private final int size;
        private final String displayName;
        private final int glConstant;

        private Type(int sizeIn, String displayNameIn, int glConstantIn) {
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

    public static enum Usage {
        POSITION("Position"),
        NORMAL("Normal"),
        COLOR("Vertex Color"),
        UV("UV"),
        MATRIX("Bone Matrix"),
        BLEND_WEIGHT("Blend Weight"),
        PADDING("Padding");

        private final String displayName;

        private Usage(String displayNameIn) {
            this.displayName = displayNameIn;
        }

        public String getDisplayName() {
            return this.displayName;
        }
    }
}
