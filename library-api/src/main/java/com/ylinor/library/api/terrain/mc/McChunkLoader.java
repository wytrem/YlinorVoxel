package com.ylinor.library.api.terrain.mc;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.type.BlockType;


/**
 * @author Alexis L.
 */
public final class McChunkLoader {

    private static final Logger logger = LoggerFactory.getLogger(McChunkLoader.class);

    public static Chunk loadChunk(Terrain terrain, int chunkX, int chunkZ, File regionsFolder) throws IOException {
        logger.trace("Loading chunk at (x: {}, z: {})", chunkX, chunkZ);
        int regionX = (int) Math.floor(chunkX / 32.0f);
        int regionZ = (int) Math.floor(chunkZ / 32.0f);
        File regionFolder = new File(regionsFolder, String.format("r.%d.%d.mca", regionX, regionZ));
        Chunk chunk = new Chunk(terrain, chunkX, chunkZ);

        if (regionFolder.exists()) {
            int regionChunkX = Math.floorMod(chunkX, 32);
            int regionChunkZ = Math.floorMod(chunkZ, 32);
            RegionFile regionFile = new RegionFile(regionFolder);

            if (regionFile.hasChunk(regionChunkX, regionChunkZ)) {
                MinecraftChunk minecraftChunk = new MinecraftChunk(regionFile.getChunkDataInputStream(regionChunkX, regionChunkZ));

                for (int sectionY = 0; sectionY < 16; sectionY++) {
                    if (minecraftChunk.isSectionPresent(sectionY)) {
                        short[] blocks = minecraftChunk.getSectionBlocks(sectionY);

                        for (int y = 0; y < 16; y++) {
                            int posY = y + sectionY * 16;

                            for (int z = 0; z < 16; z++) {
                                int idx = (y * 16 + z) * 16;

                                for (int x = 0; x < 16; x++) {
                                    BlockType blockType;

                                    short block = blocks[idx + x];
                                    short blockID = (short) (block & 0xfff);
                                    byte metadata = (byte) ((block >> 12) & 0x0f);

                                    if ((blockType = BlockType.REGISTRY.get(blockID)) != null) {
                                        chunk.setBlockType(x, posY, z, blockType);
                                        chunk.setBlockState(x, posY, z, blockType.getStateFromMeta(metadata));
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
