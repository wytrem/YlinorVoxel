package com.ylinor.client.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.client.render.model.block.Cube;
import com.ylinor.client.renderlib.GdxTempVars;
import com.ylinor.client.renderlib.RenderConstants;
import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.client.renderlib.format.VertexFormats;
import com.ylinor.library.api.world.BlockType;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IBlockContainer;
import com.ylinor.library.util.TempVars;

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

        BlockType tile;
        TextureRegion region;

        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int y = 0; y < Chunk.SIZE_Y; y++) {
                for (int z = 0; z < Chunk.SIZE_Z; z++) {
                    tile = neighbours.getBlockType(x, y, z);

                    if (tile != BlockType.air) {
                        region = renderer.tiles[tile.getTextureId() / 16][tile.getTextureId() % 16];

                        if (vertexBuffer.getIndicesCount() > RenderConstants.MAX_INDICES_PER_MESH - 36) {
                            vertexBuffer.finishDrawing();
                            pushMesh(vertexBuffer);
                            vertexBuffer.begin(VertexBuffer.Mode.QUADS, VertexFormats.BLOCKS);
                        }

                        renderCube(neighbours, gdxTempVars.vect0, x, y, z, region);
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

    private Cube cube = new Cube();

    private void renderCube(IBlockContainer neighbours, Vector3 offset, int x, int y, int z, TextureRegion region) {

        TempVars tempVars = TempVars.get();

        cube.toto(tempVars);

        vertexBuffer.offset.set(offset.x + x, offset.y + y, offset.z + z);

        if (y == Chunk.SIZE_Y - 1 || !neighbours.getBlockType(x, y + 1, z)
                                                .isOpaque()) {
            cube.createTop(vertexBuffer, region, tempVars);
        }

        if (y == 0 || !neighbours.getBlockType(x, y - 1, z).isOpaque()) {
            cube.createBottom(vertexBuffer, region, tempVars);
        }

        if (!neighbours.getBlockType(x + 1, y, z).isOpaque()) {
            cube.createRight(vertexBuffer, region, tempVars);
        }

        if (!neighbours.getBlockType(x - 1, y, z).isOpaque()) {
            cube.createLeft(vertexBuffer, region, tempVars);
        }

        if (!neighbours.getBlockType(x, y, z + 1).isOpaque()) {
            cube.createBack(vertexBuffer, region, tempVars);
        }

        if (!neighbours.getBlockType(x, y, z - 1).isOpaque()) {
            cube.createFront(vertexBuffer, region, tempVars);
        }

        vertexBuffer.offset.zero();

        tempVars.release();
    }

    public static int indicesIn(int numComponents) {
        return numComponents * 6 / 4;
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
