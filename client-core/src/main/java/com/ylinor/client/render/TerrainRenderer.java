package com.ylinor.client.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.ylinor.client.events.ChunkRendererUpdatedEvent;
import com.ylinor.client.renderlib.GdxTempVars;
import com.ylinor.client.resource.Assets;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.ecs.system.EventSystem;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectProcedure;


/**
 * Utility class to render the terrain.
 *
 * @author wytrem
 */
@Singleton
public class TerrainRenderer implements RenderableProvider, Disposable {
    Terrain terrain;
    TLongObjectMap<ChunkRenderer> chunkRenderers = new TLongObjectHashMap<ChunkRenderer>();
    RenderGlobal renderGlobal;
    Material standardBlockMaterial;
    Texture texture;
    int renderChunkX = -1, renderChunkZ = -1, renderChunkSize = 8;

    @Inject
    CameraSystem cameraSystem;

    @Inject
    EventSystem eventSystem;

    @Inject
    Assets assets;

    public TerrainRenderer(Terrain world, RenderGlobal renderGlobal) {
        this.terrain = world;
        this.renderGlobal = renderGlobal;
    }

    public void init() {
        texture = new Texture(assets.blockAssets.blockAtlas.getSheet());
        standardBlockMaterial = new Material(TextureAttribute.createDiffuse(texture), new BlendingAttribute(false, 1f), FloatAttribute.createAlphaTest(0.5f));
    }

    public void update() {
        renderChunkX = ((int) (cameraSystem.getCamera().position.x) >> 4) - renderChunkSize / 2;
        renderChunkZ = ((int) (cameraSystem.getCamera().position.z) >> 4) - renderChunkSize / 2;
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        for (int i = renderChunkX; i < renderChunkX + renderChunkSize; i++) {
            for (int j = renderChunkZ; j < renderChunkZ + renderChunkSize; j++) {
                renderChunk(renderables, pool, terrain.getChunk(i, j));
            }
        }
    }

    private void renderChunk(Array<Renderable> renderables, Pool<Renderable> pool, Chunk chunk) {
        GdxTempVars gdxTempVars = GdxTempVars.get();
        getBoundingBox(chunk, gdxTempVars.bb1);

        if (!cameraSystem.getCamera().frustum.boundsInFrustum(gdxTempVars.bb1)) {
            gdxTempVars.release();
            return;
        }

        ChunkRenderer chunkRenderer = chunkRenderers.get(chunk.id);

        if (chunkRenderer == null) {
            chunkRenderer = new ChunkRenderer(this);
            chunkRenderers.put(chunk.id, chunkRenderer);
        }

        if (chunk.needsRenderUpdate) {
            chunkRenderer.update(chunk, new ChunkCache(chunk, terrain));

            chunk.needsRenderUpdate = false;
            eventSystem.dispatch(new ChunkRendererUpdatedEvent(chunkRenderer, chunk));
        }

        chunkRenderer.meshes.forEachEntry(new TObjectIntProcedure<Mesh>() {
            @Override
            public boolean execute(Mesh mesh, int numIndices) {

                Renderable renderable = pool.obtain();
                renderable.meshPart.mesh = mesh;
                renderable.meshPart.offset = 0;
                renderable.meshPart.size = numIndices;
                renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
                renderable.material = standardBlockMaterial;
                renderables.add(renderable);

                return true;
            }

        });

        gdxTempVars.release();
    }

    private void getBoundingBox(Chunk chunk, BoundingBox bb) {
        bb.min.set(chunk.x * Chunk.SIZE_X, 0, chunk.z * Chunk.SIZE_Z);
        bb.max.set((chunk.x + 1) * Chunk.SIZE_X, Chunk.SIZE_Y, (chunk.z + 1) * Chunk.SIZE_Z);
    }

    @Override
    public void dispose() {
        chunkRenderers.forEachValue(new TObjectProcedure<ChunkRenderer>() {
            @Override
            public boolean execute(ChunkRenderer chunkRenderer) {
                chunkRenderer.dispose();
                return true;
            }
        });

        texture.dispose();
    }

    public Terrain getWorld() {
        return terrain;
    }
}
