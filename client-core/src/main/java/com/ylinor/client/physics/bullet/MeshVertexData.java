package com.ylinor.client.physics.bullet;

import javax.vecmath.Tuple3f;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.bulletphysics.collision.shapes.VertexData;


public class MeshVertexData extends VertexData {

    Mesh mesh;

    /**
     * The stride in floats.
     */
    int stride;
    float[] tempVertex;
    /**
     * Stride in bytes.
     */
    int vertexStride;
    
    int positionOffset;
    short[] tempIndex = new short[1];

    public MeshVertexData(Mesh mesh) {
        this.mesh = mesh;
        this.vertexStride = mesh.getVertexSize();
        this.stride = vertexStride / Float.BYTES;
        this.tempVertex = new float[vertexStride];
        VertexAttribute position = mesh.getVertexAttribute(VertexAttributes.Usage.Position);
        this.positionOffset = position.offset / Float.BYTES;
    }

    @Override
    public int getVertexCount() {
        return mesh.getNumVertices();
    }

    @Override
    public int getIndexCount() {
        return mesh.getNumIndices();
    }

    @Override
    public <T extends Tuple3f> T getVertex(int idx, T out) {
        idx = (idx & 0xffff);
//        System.out.println("--");
//        System.out.println("srcOffset = " + (idx * stride));
//        System.out.println("count = " + mesh.getVertexSize());
//        System.out.println("num vertices = " + mesh.getNumVertices());
//        System.out.println("max = " + (mesh.getNumVertices() * mesh.getVertexSize() / 4));
        mesh.getVertices(idx * stride, stride, tempVertex);
        out.set(tempVertex[positionOffset], tempVertex[positionOffset + 1], tempVertex[positionOffset + 2]);
        return out;
    }

    @Override
    public void setVertex(int idx, float x, float y, float z) {
        // Nope.
    }

    @Override
    public int getIndex(int idx) {
        mesh.getIndices(idx, 1, tempIndex, 0);
        return tempIndex[0];
    }
}
