package com.ylinor.client.render;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.ylinor.library.api.world.World;


public class WorldRenderer implements Renderer<World>
{
    private World world;

    public WorldRenderer(World world)
    {
        this.world = world;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {

    }

    @Override
    public void dispose()
    {

    }

    @Override
    public World getObject()
    {
        return world;
    }
}
