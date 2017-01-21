package com.ylinor.client.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.library.api.world.BlockType;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IBlockContainer;
import com.ylinor.library.util.DebugUtils;


public class ChunkRenderInfos implements Disposable {
    private static final VertexBuffer vertexBuffer = new VertexBuffer(2097152);

    public static final int VERTEX_SIZE = VertexFormats.BLOCKS.getStride();
    static final int MAX_VERTICES = Chunk.SIZE_X * Chunk.SIZE_Y * Chunk.SIZE_Z * 6 * 4;

    public final Mesh mesh;
    public int numIndices;

    WorldRenderer renderer;

    public ChunkRenderInfos(WorldRenderer renderer) {
        this.renderer = renderer;
        mesh = new Mesh(true, MAX_VERTICES, MAX_VERTICES * 2, VertexFormats.BLOCKS.toGdx());
    }

    public void update(Chunk chunk, IBlockContainer neighbours) {
        Vector3 offset = new Vector3(chunk.x * Chunk.SIZE_X, 0, chunk.z * Chunk.SIZE_Z);

        vertexBuffer.begin(VertexBuffer.Mode.QUADS, VertexFormats.BLOCKS);
        
        DebugUtils.timeStart();
        
        for (int x = 0; x < Chunk.SIZE_X; x++) {
            for (int y = 0; y < Chunk.SIZE_Y; y++) {
                for (int z = 0; z < Chunk.SIZE_Z; z++) {
                    BlockType tile = neighbours.getBlockType(x, y, z);

                    if (tile != BlockType.air) {
                        TextureRegion region = renderer.tiles[1][1];

                        if (y == Chunk.SIZE_Y - 1 || !neighbours.getBlockType(x, y + 1, z)
                                                                .isOpaque()) {
                            createTop(vertexBuffer, offset, x, y, z, region);
                        }

                        if (y == 0 || !neighbours.getBlockType(x, y - 1, z)
                                                 .isOpaque()) {
                            createBottom(vertexBuffer, offset, x, y, z, region);
                        }

                        if (!neighbours.getBlockType(x + 1, y, z).isOpaque()) {
                            createRight(vertexBuffer, offset, x, y, z, region);
                        }

                        if (!neighbours.getBlockType(x - 1, y, z).isOpaque()) {
                            createLeft(vertexBuffer, offset, x, y, z, region);
                        }

                        if (!neighbours.getBlockType(x, y, z + 1).isOpaque()) {
                            createBack(vertexBuffer, offset, x, y, z, region);
                        }

                        if (!neighbours.getBlockType(x, y, z - 1).isOpaque()) {
                            createFront(vertexBuffer, offset, x, y, z, region);
                        }
                    }
                }
            }
        }
        
        DebugUtils.printTime("build vertices");
        
        vertexBuffer.finishDrawing();

        numIndices = Uploader.upload(vertexBuffer, mesh);

        chunk.needsRenderUpdate = false;
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
        mesh.dispose();
    }
}
