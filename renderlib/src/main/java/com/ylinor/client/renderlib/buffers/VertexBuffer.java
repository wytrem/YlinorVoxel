package com.ylinor.client.renderlib.buffers;

import java.nio.ByteBuffer;

import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;
import com.ylinor.client.renderlib.PrimitiveType;
import com.ylinor.client.renderlib.format.VertexFormat;


/**
 * <p>
 * This class is a buffer for vertexes. It is NOT related to any OpenGL VBO, it
 * just stores vertexes data into the ram and counts them.
 * 
 * <p>
 * Some methods do the same thing under different names, to allows us a clearer
 * code. This:
 * 
 * <pre>
 * vertexBuffer.pos(9.9f, 1.0f, 1.0f).color(0.0f, 0.0f, 1.0f).endVertex();
 * </pre>
 * 
 * is much easier to understand than this:
 * 
 * <pre>
 * vertexBuffer.putFloat(9.9f)
 *             .putFloat(1.0f)
 *             .putFloat(1.0f)
 *             .putFloat(0.0f)
 *             .putFloat(0.0f)
 *             .putFloat(1.0f)
 *             .endVertex();
 * </pre>
 * 
 * @author Wytrem
 */
public class VertexBuffer {
    private ByteBuffer vertexBuffer;
    private ByteBuffer indicesBuffer;

    private int vertexCount;
    private Mode drawMode;
    private VertexFormat vertexFormat;
    private PrimitiveType indicesType;
    private int indicesCount;
    public final Vector3f offset;

    public VertexBuffer(int capacity) {
        this(capacity, capacity);
    }

    public VertexBuffer(int verticesSize, int indicesSize) {
        vertexBuffer = BufferUtils.newByteBuffer(verticesSize);
        indicesBuffer = BufferUtils.newByteBuffer(indicesSize);
        indicesType = PrimitiveType.USHORT;
        reset();
        offset = new Vector3f();
    }

    /**
     * Puts four floats in that buffer.
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     * @param a The alpha component
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer color(float r, float g, float b, float a) {
        vertexBuffer.putFloat(r).putFloat(g).putFloat(b).putFloat(a);
        return this;
    }

    /**
     * Puts three floats in that buffer.
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer color(float r, float g, float b) {
        vertexBuffer.putFloat(r).putFloat(g).putFloat(b);
        return this;
    }

    /**
     * Puts three floats in that buffer.
     * 
     * @param r The red component
     * @param g The green component
     * @param b The blue component
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer color(int rgb) {
        return this.color((float) (rgb >> 16 & 255) / 255.0F, (float) (rgb >> 8 & 255) / 255.0F, (float) (rgb & 255) / 255.0F, 1.0f);
    }

    /**
     * Puts two floats in that buffer.
     * 
     * @param x The X coordinate of the vertex
     * @param y The Y coordinate of the vertex
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer pos(float x, float y) {
        vertexBuffer.putFloat(x + offset.x).putFloat(y + offset.y);
        return this;
    }

    /**
     * Puts three floats in that buffer.
     * 
     * @param x The X coordinate of the vertex
     * @param y The Y coordinate of the vertex
     * @param z The Z coordinate of the vertex
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer pos(float x, float y, float z) {
        vertexBuffer.putFloat(x + offset.x)
                    .putFloat(y + offset.y)
                    .putFloat(z + offset.z);
        return this;
    }

    /**
     * Puts three floats in that buffer.
     * 
     * @param x The X normal
     * @param y The Y normal
     * @param z The Z normal
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer normal(float x, float y, float z) {
        vertexBuffer.putFloat(x).putFloat(y).putFloat(z);
        return this;
    }

    /**
     * Puts three floats in that buffer.
     * 
     * @param x The X coordinate of the vertex
     * @param y The Y coordinate of the vertex
     * @param z The Z coordinate of the vertex
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer pos(Vector3fc pos) {
        return this.pos(pos.x(), pos.y(), pos.z());
    }

    /**
     * Puts three floats in that buffer.
     * 
     * @param x The X normal
     * @param y The Y normal
     * @param z The Z normal
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer normal(Vector3fc normal) {
        return this.normal(normal.x(), normal.y(), normal.z());
    }

    /**
     * Puts two floats in that buffer.
     * 
     * @param s The S coordinate on the texture
     * @param t The T coordinate on the texture
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer texCoords(float s, float t) {
        vertexBuffer.putFloat(s).putFloat(t);
        return this;
    }

    /**
     * Puts two floats in that buffer.
     * 
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer texCoords(Vector2fc pos) {
        return this.texCoords(pos.x(), pos.y());
    }

    /**
     * Retrieves the data buffer.
     * 
     * @return The data buffer
     */
    public ByteBuffer getVertexBuffer() {
        return vertexBuffer;
    }

    public ByteBuffer getIndicesBuffer() {
        return indicesBuffer;
    }

    public void finishDrawing() {
        vertexBuffer.flip();
        indicesBuffer.flip();
    }

    public void reset() {
        vertexBuffer.position(0);
        vertexBuffer.limit(vertexBuffer.capacity());
        indicesBuffer.position(0);
        indicesBuffer.limit(indicesBuffer.capacity());
        vertexCount = 0;
        indicesCount = 0;
        vertexFormat = null;
        drawMode = null;
    }

    public void begin(Mode mode, VertexFormat format) {
        reset();
        this.drawMode = mode;
        this.vertexFormat = format;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Mode getDrawMode() {
        return drawMode;
    }

    public VertexFormat getVertexFormat() {
        return vertexFormat;
    }

    public PrimitiveType getIndicesType() {
        return indicesType;
    }

    public int getIndicesCount() {
        return indicesCount;
    }

    /**
     * Increments the vertex count.
     */
    public void endVertex() {
        vertexCount++;

        if (vertexBuffer.remaining() < vertexFormat.getStride()) {
            growVertexBuffer(1024);
        }

        if (indicesBuffer.remaining() < 16) {
            growIndicesBuffer(1024);
        }

        if (this.drawMode == Mode.QUADS) {
            if (vertexCount % 4 == 0) {
                putIndice(vertexCount - 4);
                putIndice(vertexCount - 3);
                putIndice(vertexCount - 2);
                putIndice(vertexCount - 4);
                putIndice(vertexCount - 2);
                putIndice(vertexCount - 1);
            }
        }
    }

    private void growVertexBuffer(int addedCapacity) {
        int oldCapacity = this.vertexBuffer.capacity();
        int j = oldCapacity + addedCapacity;
        int pos = this.vertexBuffer.position();
        ByteBuffer bytebuffer = BufferUtils.newByteBuffer(j);
        this.vertexBuffer.position(0);
        bytebuffer.put(this.vertexBuffer);
        bytebuffer.rewind();
        this.vertexBuffer = bytebuffer;
        this.vertexBuffer.position(pos);
    }

    private void growIndicesBuffer(int addedCapacity) {
        int oldCapacity = this.indicesBuffer.capacity();
        int j = oldCapacity + addedCapacity;
        int pos = this.indicesBuffer.position();
        ByteBuffer bytebuffer = BufferUtils.newByteBuffer(j);
        this.indicesBuffer.position(0);
        bytebuffer.put(this.indicesBuffer);
        bytebuffer.rewind();
        this.indicesBuffer = bytebuffer;
        this.indicesBuffer.position(pos);
    }

    private void putIndice(int indice) {
        if (indicesType == PrimitiveType.UINT) {
            indicesBuffer.putInt(indice);
        }
        else if (indicesType == PrimitiveType.USHORT) {
            indicesBuffer.putShort((short) indice);
        }
        else if (indicesType == PrimitiveType.UBYTE) {
            indicesBuffer.put((byte) indice);
        }

        indicesCount++;
    }

    public static enum Mode {
        TRIANGLES(GL20.GL_TRIANGLES, false),
        TRIANGLES_FAN(GL20.GL_TRIANGLE_FAN, false),
        TRIANGLES_STRIP(GL20.GL_TRIANGLE_STRIP, false),
        QUADS(GL20.GL_TRIANGLES, true);

        private int glConstant;
        private boolean indices;

        private Mode(int glConstant, boolean indices) {
            this.glConstant = glConstant;
            this.indices = indices;
        }

        public boolean doesUseIndices() {
            return indices;
        }

        public int getGlConstant() {
            return glConstant;
        }
    }
}
