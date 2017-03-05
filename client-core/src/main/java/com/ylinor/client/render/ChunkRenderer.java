package com.ylinor.client.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.renderlib.GdxTempVars;
import com.ylinor.client.renderlib.RenderConstants;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.client.renderlib.format.VertexFormats;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.IBlockContainer;
import com.ylinor.library.api.terrain.block.RenderType;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;

import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.procedure.TObjectProcedure;


public class ChunkRenderer implements Disposable {
    // Allocate 2MB ram for vertices buffering
    private static final VertexBuffer vertexBuffer = new VertexBuffer(RenderConstants.MAX_QUADS_VERTICES, RenderConstants.MAX_INDICES_PER_MESH);

    public static final int VERTEX_SIZE = VertexFormats.BLOCKS.getStride();

    TerrainRenderer renderer;

    public final TObjectIntHashMap<Mesh> meshes;

    public ChunkRenderer(TerrainRenderer renderer) {
        this.renderer = renderer;
        meshes = new TObjectIntHashMap<>();
    }

    public void update(Chunk chunk, IBlockContainer neighbours) {
        meshes.forEach(new TObjectProcedure<Mesh>() {
            @Override
            public boolean execute(Mesh mesh) {
                mesh.dispose();
                return true;
            }
        });

        meshes.clear();
        GdxTempVars gdxTempVars = GdxTempVars.get();

        gdxTempVars.vect0.set(chunk.x * Chunk.SIZE_X, 0, chunk.z * Chunk.SIZE_Z);

        vertexBuffer.begin(VertexBuffer.Mode.QUADS, VertexFormats.BLOCKS);

        BlockState state;
        BlockModel model;

        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int y = 0; y < Chunk.SIZE_Y; y++) {
                for (int z = 0; z < Chunk.SIZE_Z; z++) {
                    state = neighbours.getBlockState(x, y, z);
                    

                    if (state.getAttributes().getRenderType().equals(RenderType.BLOCKMODEL)) {
                        model = renderer.assets.blockAssets.modelsRegistry.get(chunk.getWorld(), state.getBlockType(), state);
                        
                        if (model == null) {
                            throw new RenderingException("Missing model for properties : '" + state.propertiesToString() + "'");
                        }
                        
                        vertexBuffer.offset.set(gdxTempVars.vect0.x + x, gdxTempVars.vect0.y + y, gdxTempVars.vect0.z + z);
                        if (vertexBuffer.getIndicesCount() > RenderConstants.MAX_INDICES_PER_MESH - model.neededIndices()) {
                            vertexBuffer.finishDrawing();
                            pushMesh(vertexBuffer);
                            vertexBuffer.begin(VertexBuffer.Mode.QUADS, VertexFormats.BLOCKS);
                        }
                        
                        model.render(vertexBuffer, neighbours, x, y, z);
                    }
                }
            }
        }

        vertexBuffer.finishDrawing();

        pushMesh(vertexBuffer);

        chunk.needsRenderUpdate = false;

        gdxTempVars.release();
    }

    private void pushMesh(VertexBuffer vertexBuffer) {
        Mesh mesh = RenderConstants.newBiggestQuadsMesh();

        int amount = Uploader.upload(vertexBuffer, mesh);
        meshes.put(mesh, amount);
    }

    public static int indicesIn(int numVertices) {
        return numVertices * 6 / 4;
    }
    
    public static int verticesIn(int numIndices) {
        return numIndices * 4 / 6;
    }

    public static void createTop(VertexBuffer buffer, Vector3 offset, int x, int y, int z, TextureRegion region) {
        buffer.pos(offset.x + x, offset.y + y + 1, offset.z + z)
              .normal(0, 1, 0)
              .texCoords(region.getU(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y + 1, offset.z + z)
              .normal(0, 1, 0)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y + 1, offset.z + z + 1)
              .normal(0, 1, 0)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x, offset.y + y + 1, offset.z + z + 1)
              .normal(0, 1, 0)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
    }

    public static void createBottom(VertexBuffer buffer, Vector3 offset, int x, int y, int z, TextureRegion region) {
        buffer.pos(offset.x + x, offset.y + y, offset.z + z)
              .normal(0, -1, 0)
              .texCoords(region.getU(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x, offset.y + y, offset.z + z + 1)
              .normal(0, -1, 0)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y, offset.z + z + 1)
              .normal(0, -1, 0)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y, offset.z + z)
              .normal(0, -1, 0)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
    }

    public static void createLeft(VertexBuffer buffer, Vector3 offset, int x, int y, int z, TextureRegion region) {
        buffer.pos(offset.x + x, offset.y + y, offset.z + z)
              .normal(-1, 0, 0)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x, offset.y + y + 1, offset.z + z)
              .normal(-1, 0, 0)
              .texCoords(region.getU(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x, offset.y + y + 1, offset.z + z + 1)
              .normal(-1, 0, 0)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x, offset.y + y, offset.z + z + 1)
              .normal(-1, 0, 0)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();
    }

    public static void createRight(VertexBuffer buffer, Vector3 offset, int x, int y, int z, TextureRegion region) {

        buffer.pos(offset.x + x + 1, offset.y + y, offset.z + z)
              .normal(1, 0, 0)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y, offset.z + z + 1)
              .normal(1, 0, 0)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y + 1, offset.z + z + 1)
              .normal(1, 0, 0)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y + 1, offset.z + z)
              .normal(1, 0, 0)
              .texCoords(region.getU(), region.getV())
              .endVertex();
    }

    public static void createFront(VertexBuffer buffer, Vector3 offset, int x, int y, int z, TextureRegion region) {

        buffer.pos(offset.x + x, offset.y + y, offset.z + z)
              .normal(0, 0, 1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y, offset.z + z)
              .normal(0, 0, 1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y + 1, offset.z + z)
              .normal(0, 0, 1)
              .texCoords(region.getU(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x, offset.y + y + 1, offset.z + z)
              .normal(0, 0, 1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
    }

    public static void createBack(VertexBuffer buffer, Vector3 offset, int x, int y, int z, TextureRegion region) {

        buffer.pos(offset.x + x, offset.y + y, offset.z + z + 1)
              .normal(0, 0, -1)
              .texCoords(region.getU2(), region.getV2())
              .endVertex();
        buffer.pos(offset.x + x, offset.y + y + 1, offset.z + z + 1)
              .normal(0, 0, -1)
              .texCoords(region.getU2(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y + 1, offset.z + z + 1)
              .normal(0, 0, -1)
              .texCoords(region.getU(), region.getV())
              .endVertex();
        buffer.pos(offset.x + x + 1, offset.y + y, offset.z + z + 1)
              .normal(0, 0, -1)
              .texCoords(region.getU(), region.getV2())
              .endVertex();
    }

    @Override
    public void dispose() {
        meshes.forEach(new TObjectProcedure<Mesh>() {

            @Override
            public boolean execute(Mesh mesh) {
                mesh.dispose();
                return true;
            }
        });
    }
}
