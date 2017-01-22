package com.ylinor.client.render;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.badlogic.gdx.graphics.Mesh;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.util.DebugUtils;


public class Uploader {
    private static short[] indices = new short[2097152];
    private static float[] vertexes = new float[2097152];

    private static int size;

    public synchronized static int upload(VertexBuffer buffer, Mesh mesh) {
        DebugUtils.timeStart();

        {
            FloatBuffer vertexesBuffer = buffer.getVertexBuffer()
                                               .asFloatBuffer();

            size = vertexesBuffer.remaining();

            if (vertexes.length < size) {
                vertexes = new float[size];
            }

            vertexesBuffer.get(vertexes, 0, size);

            mesh.setVertices(vertexes, 0, size);
        }

        {
            ShortBuffer indicesBuffer = buffer.getIndicesBuffer()
                                              .asShortBuffer();

            size = indicesBuffer.remaining();

            if (indices.length < size) {
                indices = new short[size];
            }

            indicesBuffer.get(indices, 0, size);

            mesh.setIndices(indices, 0, size);
        }

        buffer.reset();

        DebugUtils.printTime("upload " + size + " indices");

        return size;
    }
}
