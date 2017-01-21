package com.ylinor.client.render;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.World;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;


public class WorldRenderer implements RenderableProvider, Disposable {
    private World world;
    TLongObjectMap<ChunkRenderInfos> chunkRenderInfos = new TLongObjectHashMap<ChunkRenderInfos>();
    RenderGlobal renderGlobal;
    
    public WorldRenderer(World world, RenderGlobal renderGlobal) {
        this.world = world;
        this.renderGlobal = renderGlobal;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        getTerrain(renderables, pool);
    }

    private void renderChunk(Array<Renderable> renderables, Pool<Renderable> pool, Chunk chunk)
    {
        if (!renderGlobal.cameraFrustum.boundsInFrustum(chunk.bb))
        {
            return;
        }

        ChunkRenderInfos infos = chunkRenderInfos.get(chunk.id);
        
        if (infos == null)
        {
            infos = new ChunkRenderInfos();
            chunkRenderInfos.put(chunk.id, infos);
        }

        if (chunk.needsRenderUpdate)
        {
            infos.update(chunk, new ChunkCache(chunk, world), tiles);

            chunk.needsRenderUpdate = false;
        }

        Renderable renderable = pool.obtain();
        renderable.meshPart.mesh = infos.mesh;
        renderable.meshPart.offset = 0;
        renderable.meshPart.size = infos.numIndices;
        renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
        renderable.material = standardBlockMaterial;
        renderables.add(renderable);
    }

    private void getTerrain(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for (int i = renderChunkX; i < renderChunkX + renderChunkSize; i++)
        {
            for (int j = renderChunkZ; j < renderChunkZ + renderChunkSize; j++)
            {
                renderChunk(renderables, pool, world.chunkProvider.getChunkAt(i, j));
            }
        }
    }

    @Override
    public void dispose() {

    }

    public World getWorld() {
        return world;
    }
}
