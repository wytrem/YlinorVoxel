package com.ylinor.client.render;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.World;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;


public class WorldRenderer implements RenderableProvider, Disposable {
    World world;
    TLongObjectMap<ChunkRenderInfos> chunkRenderInfos = new TLongObjectHashMap<ChunkRenderInfos>();
    RenderGlobal renderGlobal;
    Material standardBlockMaterial;
    TextureRegion[][] tiles;
    Texture texture;
    int renderChunkX = -1, renderChunkZ = -1, renderChunkSize = 2;

    public WorldRenderer(World world, RenderGlobal renderGlobal) {
        this.world = world;
        this.renderGlobal = renderGlobal;
    }
    
    public void update()
    {
        renderChunkX = ((int) renderGlobal.camera.position.x) >> 4;
        renderChunkZ = ((int) renderGlobal.camera.position.z) >> 4;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        getTerrain(renderables, pool);
    }

    private void renderChunk(Array<Renderable> renderables, Pool<Renderable> pool, Chunk chunk)
    {
        if (!renderGlobal.cameraFrustum.boundsInFrustum(getBoudingBox(chunk)))
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
    
    private BoundingBox bb;
    
    private BoundingBox getBoudingBox(Chunk chunk)
    {
        bb.min.set(chunk.x * Chunk.SIZE_X, 0, chunk.z * Chunk.SIZE_Z);
        bb.max.set((chunk.x + 1) * Chunk.SIZE_X, Chunk.SIZE_Y, (chunk.z + 1) * Chunk.SIZE_Z);
        return bb;
    }

    private void getTerrain(Array<Renderable> renderables, Pool<Renderable> pool)
    {
        for (int i = renderChunkX; i < renderChunkX + renderChunkSize; i++)
        {
            for (int j = renderChunkZ; j < renderChunkZ + renderChunkSize; j++)
            {
                renderChunk(renderables, pool, world.getChunk(i, j));
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
