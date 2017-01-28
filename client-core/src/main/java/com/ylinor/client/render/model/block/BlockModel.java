package com.ylinor.client.render.model.block;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ylinor.client.renderlib.buffers.VertexBuffer;

public class BlockModel {
    @Nullable
    private BlockModel parent;
    
    @Nullable
    private String name;
    
    @NotNull
    private List<Cube> cubes;

    
    
    
    public void renderAt(VertexBuffer vertexBuffer, int x, int y, int z) {
        cubes.forEach(cube -> cube.renderAt(vertexBuffer, x, y, z));
    }
    
    public int neededIndices(){
        return cubes.size() * 36;
    }
}
