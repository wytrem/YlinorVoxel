package com.ylinor.client.render.model.block;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ylinor.client.render.Renderable;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.api.terrain.IBlockContainer;


public class BlockModel implements Renderable {
    @Nullable
    private String name;

    @NotNull
    protected List<Cube> cubes;

    public BlockModel() {
        this.cubes = new ArrayList<>();
    }

    public BlockModel(@NotNull List<Cube> cubes) {
        this(null, cubes);
    }

    public BlockModel(@Nullable String name, @NotNull List<Cube> cubes) {
        this.name = name;
        this.cubes = cubes;
    }

    public void render(VertexBuffer vertexBuffer) {
        cubes.forEach(cube -> cube.render(vertexBuffer));
    }

    public void render(VertexBuffer vertexBuffer, IBlockContainer neighbours, int x, int y, int z) {
        cubes.forEach(cube -> cube.render(vertexBuffer, neighbours, x, y, z));
    }

    public int neededIndices() {
        return cubes.size() * 36;
    }

    public String getName() {
        return name;
    }

    public List<Cube> getCubes() {
        return cubes;
    }

    @Override
    public String toString() {
        return "BlockModel [name=" + name + ", cubes=" + cubes + "]";
    }
}
