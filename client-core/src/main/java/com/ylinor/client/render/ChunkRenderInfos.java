package com.ylinor.client.render;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.library.api.world.BlockType;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IBlockContainer;


public class ChunkRenderInfos implements Disposable
{
    public static final int VERTEX_SIZE = /* Position */ 3 + /* Normal */ 3 + /* UV */ 2;
    static final int maxVertices = Chunk.SIZE_X * Chunk.SIZE_Y * Chunk.SIZE_Z * 6 * 4 * VERTEX_SIZE;

    static final short[] indices = new short[indicesIn(maxVertices)];
    public static final float[] vertices = new float[maxVertices];

    static
    {
        short j = 0;
        for (int i = 0; i < indices.length; i += 6, j += 4)
        {
            indices[i + 0] = (short) (j + 0);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 0);
        }
    }

    public final Mesh mesh;
    public int numIndices;

    public ChunkRenderInfos()
    {
        mesh = new Mesh(true, maxVertices, indices.length, VertexAttribute.Position(), VertexAttribute.Normal(), VertexAttribute.TexCoords(0));
        mesh.setIndices(indices);
    }

    public void update(Chunk chunk, IBlockContainer neighbours, TextureRegion[][] tiles)
    {
        Vector3 offset = new Vector3(chunk.x * Chunk.SIZE_X, 0, chunk.z * Chunk.SIZE_Z);
        int vertexOffset = 0;

        for (int x = 0; x < Chunk.SIZE_X; x++)
        {
            for (int y = 0; y < Chunk.SIZE_Y; y++)
            {
                for (int z = 0; z < Chunk.SIZE_Z; z++)
                {
                    BlockType tile = neighbours.getBlockType(x, y, z);

                    if (tile != BlockType.air)
                    {
                        TextureRegion region = tiles[tile.getTextureIndex() / 16][tile.getTextureIndex() % 16];

                        if (y == Chunk.SIZE_Y - 1 || !neighbours.getBlockType(x, y + 1, z).isOpaque())
                        {
                            vertexOffset = createTop(offset, x, y, z, vertexOffset, region);
                        }

                        if (y == 0 || !neighbours.getBlockType(x, y - 1, z).isOpaque())
                        {
                            vertexOffset = createBottom(offset, x, y, z, vertexOffset, region);
                        }

                        if (!neighbours.getBlockType(x + 1, y, z).isOpaque())
                        {
                            vertexOffset = createRight(offset, x, y, z, vertexOffset, region);
                        }
                        
                        if (!neighbours.getBlockType(x - 1, y, z).isOpaque())
                        {
                            vertexOffset = createLeft(offset, x, y, z, vertexOffset, region);
                        }
                        
                        if (!neighbours.getBlockType(x, y, z + 1).isOpaque())
                        {
                            vertexOffset = createBack(offset, x, y, z, vertexOffset, region);
                        }
                        
                        if (!neighbours.getBlockType(x, y, z - 1).isOpaque())
                        {
                            vertexOffset = createFront(offset, x, y, z, vertexOffset, region);
                        }
                    }
                }
            }
        }

        mesh.setVertices(vertices, 0, vertexOffset);
        numIndices = indicesIn(vertexOffset);

        chunk.needsRenderUpdate = false;
    }
    
    
    public static int indicesIn(int numComponents)
    {
        return numComponents * 6 / 4;
    }

    public static int createTop(Vector3 offset, int x, int y, int z, int vertexOffset, TextureRegion region)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV2();

        return vertexOffset;
    }

    public static int createBottom(Vector3 offset, int x, int y, int z, int vertexOffset, TextureRegion region)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV();
        return vertexOffset;
    }

    public static int createLeft(Vector3 offset, int x, int y, int z, int vertexOffset, TextureRegion region)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV2();

        return vertexOffset;
    }

    public static int createRight(Vector3 offset, int x, int y, int z, int vertexOffset, TextureRegion region)
    {
        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV();
        return vertexOffset;
    }

    public static int createFront(Vector3 offset, int x, int y, int z, int vertexOffset, TextureRegion region)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 1;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV();

        return vertexOffset;
    }

    public static int createBack(Vector3 offset, int x, int y, int z, int vertexOffset, TextureRegion region)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV2();

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = region.getU2();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV();

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = 0;
        vertices[vertexOffset++] = -1;
        vertices[vertexOffset++] = region.getU();
        vertices[vertexOffset++] = region.getV2();

        return vertexOffset;
    }
    
    public static int createTop(Vector3 offset, int x, int y, int z, int vertexOffset)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;

        return vertexOffset;
    }

    public static int createBottom(Vector3 offset, int x, int y, int z, int vertexOffset)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;
        return vertexOffset;
    }

    public static int createLeft(Vector3 offset, int x, int y, int z, int vertexOffset)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;

        return vertexOffset;
    }

    public static int createRight(Vector3 offset, int x, int y, int z, int vertexOffset)
    {
        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;
        return vertexOffset;
    }

    public static int createFront(Vector3 offset, int x, int y, int z, int vertexOffset)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z;

        return vertexOffset;
    }

    public static int createBack(Vector3 offset, int x, int y, int z, int vertexOffset)
    {
        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y + 1;
        vertices[vertexOffset++] = offset.z + z + 1;

        vertices[vertexOffset++] = offset.x + x + 1;
        vertices[vertexOffset++] = offset.y + y;
        vertices[vertexOffset++] = offset.z + z + 1;

        return vertexOffset;
    }

    @Override
    public void dispose()
    {
        mesh.dispose();
    }
}
