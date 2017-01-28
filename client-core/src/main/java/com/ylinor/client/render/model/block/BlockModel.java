package com.ylinor.client.render.model.block;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ylinor.client.render.Renderable;
import com.ylinor.client.renderlib.buffers.VertexBuffer;

public class BlockModel implements Renderable {
    @Nullable
    private String name;
    
    @NotNull
    private List<Cube> cubes;

    
    
    
    public void render(VertexBuffer vertexBuffer) {
        cubes.forEach(cube -> cube.render(vertexBuffer));
    }
    
    public int neededIndices(){
        return cubes.size() * 36;
    }
}
