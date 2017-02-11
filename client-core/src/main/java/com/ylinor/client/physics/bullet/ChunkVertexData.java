package com.ylinor.client.physics.bullet;

import javax.vecmath.Tuple3f;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.bulletphysics.collision.shapes.VertexData;
import com.ylinor.client.render.ChunkRenderer;
import com.ylinor.client.renderlib.RenderConstants;


public class ChunkVertexData extends VertexData {

    ChunkRenderer chunkRenderer;
    
    public ChunkVertexData(ChunkRenderer chunkRenderer) {
        this.chunkRenderer = chunkRenderer;
    }

    @Override
    public int getVertexCount() {
        return ChunkRenderer.verticesIn(getIndexCount());
    }

    @Override
    public int getIndexCount() {
        int count = 0;

        for (int i : chunkRenderer.meshes.values()) {
            count += i;
        }

        return count;
    }

    private final Mesh[] tempMeshes = new Mesh[16];
    private final float[] vertexTemp = new float[64];
    private final short[] indiceTemp = new short[1];

    @Override
    public <T extends Tuple3f> T getVertex(int idx, T out) {
        int meshId = 0;

        while (idx >= RenderConstants.MAX_QUADS_VERTICES) {
            idx -= RenderConstants.MAX_QUADS_VERTICES;
            meshId++;
        }

        chunkRenderer.meshes.keys(tempMeshes);
        Mesh mesh = tempMeshes[meshId];
        VertexAttribute position = mesh.getVertexAttribute(VertexAttributes.Usage.Position);
        int stride = idx * mesh.getVertexSize() / Float.BYTES;
        int offset = position.offset / Float.BYTES;
        mesh.getVertices(idx * stride, stride, vertexTemp);

        out.set(vertexTemp[offset], vertexTemp[offset + 1], vertexTemp[offset + 2]);

        return out;
    }

    @Override
    public void setVertex(int idx, float x, float y, float z) {
        // Nope.
    }

    @Override
    public int getIndex(int idx) {
        int meshId = 0;

        while (idx >= RenderConstants.MAX_INDICES_PER_MESH) {
            idx -= RenderConstants.MAX_INDICES_PER_MESH;
            meshId++;
        }

        chunkRenderer.meshes.keys(tempMeshes);
        Mesh mesh = tempMeshes[meshId];
        
        mesh.getIndices(idx, 1, indiceTemp, 0);

        return indiceTemp[0];
    }

}
