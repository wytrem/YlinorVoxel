package com.ylinor.client.render;

import com.badlogic.gdx.graphics.Mesh;


public class Uploader {
    public static void upload(VertexBuffer buffer, Mesh mesh) {
        mesh.setIndices(buffer.getIndicesBuffer().asShortBuffer().array(), 0, buffer.getIndicesCount());
        mesh.setVertices(buffer.getVertexBuffer().asFloatBuffer().array(), 0, buffer.getVertexCount());
        buffer.reset();
    }
}
