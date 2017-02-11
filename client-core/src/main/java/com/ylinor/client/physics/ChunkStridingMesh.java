package com.ylinor.client.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Mesh;
import com.bulletphysics.collision.shapes.StridingMeshInterface;
import com.bulletphysics.collision.shapes.VertexData;
import com.ylinor.client.render.ChunkRenderer;

public class ChunkStridingMesh extends StridingMeshInterface {
    ChunkRenderer chunkRenderer;
    private final Mesh[] meshesTemp = new Mesh[16];
    private final List<MeshVertexData> subparts = new ArrayList<>(16);

    public ChunkStridingMesh(ChunkRenderer chunkRenderer) {
        this.chunkRenderer = chunkRenderer;
        update(chunkRenderer);
    }
    
    public void update(ChunkRenderer chunkRenderer) {
        subparts.clear();
        
        chunkRenderer.meshes.keys(meshesTemp);
        
        for (int i = 0; i < chunkRenderer.meshes.size(); i++) {
            subparts.add(new MeshVertexData(getMesh(i)));
        }
    }
    
    private Mesh getMesh(int i) {
        return meshesTemp[i];
    }

    @Override
    public VertexData getLockedVertexIndexBase(int subpart) {
        return subparts.get(subpart);
    }

    @Override
    public VertexData getLockedReadOnlyVertexIndexBase(int subpart) {
        return subparts.get(subpart);
    }

    @Override
    public void unLockVertexBase(int subpart) {
        
    }

    @Override
    public void unLockReadOnlyVertexBase(int subpart) {
        
    }

    @Override
    public int getNumSubParts() {
        return chunkRenderer.meshes.size();
    }

    @Override
    public void preallocateVertices(int numverts) {
        
    }

    @Override
    public void preallocateIndices(int numindices) {
        
    }
}
