package com.ylinor.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.type.BlockType;

import java.io.IOException;

/**
 * @author Alexis L.
 */
public final class ChunkLoader {
    public static Chunk loadChunk(Terrain terrain, int chunkX, int chunkZ) throws IOException {
        int regionX = MathUtils.floor(chunkX / 32.0f);
        int regionZ = MathUtils.floor(chunkZ / 32.0f);
        FileHandle regionHandle = Gdx.files.local(String.format("r.%d.%d.mca", regionX, regionZ));
        Chunk chunk = new Chunk(terrain, chunkX, chunkZ);

        if (regionHandle.exists()) {
            int regionChunkX = Math.abs(chunkX % 32);
            int regionChunkZ = Math.abs(chunkZ % 32);
            RegionFile regionFile = new RegionFile(regionHandle.file());

            if (regionFile.hasChunk(regionChunkX, regionChunkZ)) {
                MinecraftChunk minecraftChunk = new MinecraftChunk(regionFile.getChunkDataInputStream(regionChunkX, regionChunkZ));

                for (int subChunkY = 0; subChunkY < 16; subChunkY++) {
                    if (minecraftChunk.isSectionPresent(subChunkY)) {
                        byte[] blocks = minecraftChunk.getSectionBlocks(subChunkY);

                        for (int y = 0; y < 16; y++) {
                            int posY = y + subChunkY * 16;

                            for (int z = 0; z < 16; z++) {
                                for (int x = 0; x < 16; x++) {
                                    BlockType blockType;

                                    if ((blockType = BlockType.REGISTRY.get(blocks[(y * 16 + z) * 16 + x])) != null) {
                                        chunk.setBlockType(x, posY, z, blockType);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return chunk;
    }
}
