package com.ylinor.client.render;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.ylinor.library.api.world.World;
import gnu.trove.map.TShortObjectMap;


public class TerrainRenderer implements RenderableProvider, Disposable {
    private World world;

    public TerrainRenderer(World world) {
        this.world = world;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {

    }

    @Override
    public void dispose() {

    }

    public World getWorld() {
        return world;
    }
}
