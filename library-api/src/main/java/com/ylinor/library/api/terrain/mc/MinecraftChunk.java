package com.ylinor.library.api.terrain.mc;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.stream.NBTInputStream;

public final class MinecraftChunk {
    /// xxxx xxxx yyyy zzzz x = block id, y = add, z = metadata
    private final short[][] sectionsBlocks;

    protected MinecraftChunk(InputStream in) throws IOException {
        this.sectionsBlocks = new short[16][];

        try (NBTInputStream nbtInputStream = new NBTInputStream(in, false)) {
            CompoundTag rootTag = (CompoundTag) nbtInputStream.readTag();

            readChunk((CompoundTag) rootTag.getValue().get("Level"));
        }
    }

    private void readChunk(CompoundTag chunkTag) {
        ListTag<?> sectionsListTag = (ListTag<?>) chunkTag.getValue().get("Sections");
        List<?> sectionsList = sectionsListTag.getValue();

        for (int i = 0; i < sectionsList.size(); i++) {
            CompoundTag sectionTag = (CompoundTag) sectionsList.get(i);
            CompoundMap sectionValues = sectionTag.getValue();
            ByteArrayTag dataTag;

            int y = ((Byte) sectionValues.get("Y").getValue()).intValue();
            byte[] blockIDs = (byte[]) sectionValues.get("Blocks").getValue();
            byte[] blockDatas = (byte[]) sectionValues.get("Data").getValue();

            short[] blocks = new short[4096];
            sectionsBlocks[y] = blocks;

            for (int j = 0; j < 2048; j++) {
                byte metadata = blockDatas[j];
                int metadata1 = metadata & 0x0f;
                int metadata2 = (metadata >> 4) & 0x0f;
                int idx = j * 2;

                blocks[idx] = (short) (blockIDs[idx] & 0xff);
                blocks[idx + 1] = (short) (blockIDs[idx + 1] & 0xff);
                blocks[idx] |= metadata1 << 12;
                blocks[idx + 1] |= metadata2 << 12;
            }

            if ((dataTag = (ByteArrayTag) sectionValues.get("Add")) != null) {
                // work in progress
            }
        }
    }

    public boolean isSectionPresent(int y) {
        return sectionsBlocks[y] != null;
    }

    public short[] getSectionBlocks(int y) {
        return sectionsBlocks[y];
    }
}
