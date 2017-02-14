package com.ylinor.client.render.model.block;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ylinor.client.render.Renderable;
import com.ylinor.client.render.model.ModelDeserializer;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.api.world.IBlockContainer;

public class BlockModel implements Renderable {
    @Nullable
    private String name;
    
    @NotNull
    protected List<Cube> cubes;

    public BlockModel(@NotNull List<Cube> cubes)
    {
        this(null, cubes);
    }

    public BlockModel(@Nullable String name, @NotNull List<Cube> cubes)
    {
        this.name = name;
        this.cubes = cubes;
    }

    public void render(VertexBuffer vertexBuffer) {
        cubes.forEach(cube -> cube.render(vertexBuffer));
    }
    
    public void render(VertexBuffer vertexBuffer, IBlockContainer neighbours, int x, int y, int z) {
        render(vertexBuffer);
    }
    
    public int neededIndices(){
        return cubes.size() * 36;
    }
    
    public static final BlockModel basicCube(TextureRegion region) {
        BasicBlockModel model = new BasicBlockModel();
        
        model.cubes = Collections.singletonList(Cube.from(region));
        
        return model;
    }

    public String getName()
    {
        return name;
    }

    public List<Cube> getCubes()
    {
        return cubes;
    }
}
