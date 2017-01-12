package com.ylinor.client.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.badlogic.gdx.utils.BufferUtils;


/**
 * <p>
 * This class is a buffer for vertexes. It is NOT related to any OpenGL VBO, it
 * just stores vertexes data into the ram and counts them.
 * 
 * <p>
 * Some methods do the same thing under different names, to allows us a
 * clearer code. This:
 * 
 * <pre>
 * vertexBuffer.pos(9.9f, 1.0f, 1.0f).color(0.0f, 0.0f, 1.0f).endVertex();
 * </pre>
 * 
 * is much easier to understand than this:
 * <pre>
 * vertexBuffer.put(9.9f).put(1.0f).put(1.0f).put(0.0f).put(0.0f).put(1.0f).endVertex();
 * </pre>
 * 
 * @author Wytrem
 */
public class VertexBuffer {
    private ByteBuffer byteBuffer;
    private IntBuffer intBuffer;
    private FloatBuffer floatBuffer;

    private int vertexCount;
    private int primitiveType;
    private VertexFormat format;
    
    public VertexBuffer(int size) {
        byteBuffer = BufferUtils.newByteBuffer(size);
        intBuffer = byteBuffer.asIntBuffer();
        floatBuffer = byteBuffer.asFloatBuffer();
    }
    
    public int getVertexCount() {
        return vertexCount;
    }
    
    public int getPrimitiveType() {
        return primitiveType;
    }
    
    public VertexFormat getVertexFormat() {
        return format;
    }

    /**
     * Increments the vertex count.
     */
    public void endVertex() {
        vertexCount++;
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
        floatBuffer.put(r).put(g).put(b).put(a);
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
        floatBuffer.put(r).put(g).put(b);
        return this;
    }

    /**
     * Puts two floats in that buffer.
     * 
     * @param x The X coordinate of the vertex
     * @param y The Y coordinate of the vertex
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer pos(float x, float y) {
        floatBuffer.put(x).put(y);
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
        floatBuffer.put(x).put(y).put(z);
        return this;
    }
    
    /**
     * Puts two floats in that buffer.
     * 
     * @param s The S coordinate on the texture
     * @param t The T coordinate on the texture
     * @return This {@link VertexBuffer} for chaining
     */
    public VertexBuffer texCoords(float s, float t) {
        floatBuffer.put(s).put(t);
        return this;
    }

    /**
     * Retrieves the data buffer.
     * 
     * @return The data buffer
     */
    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }
}
