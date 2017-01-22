package com.ylinor.client.render.model.block;

import java.util.List;

import com.ylinor.client.renderlib.buffers.VertexBuffer;

public class BlockModel {
    private List<Cube> cubes;
    
    public void renderAt(VertexBuffer vertexBuffer, int x, int y, int z) {
        
    }
    
    public int neededIndices(){
        return cubes.size() * 36;
    }
}
