package com.ylinor.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.stream.NBTInputStream;

public final class MinecraftChunk {
    private final byte[][] sectionsBlocks;

    protected MinecraftChunk(InputStream in) throws IOException {
        this.sectionsBlocks = new byte[16][];

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

            byte[] blocks = (byte[]) sectionValues.get("Blocks").getValue();
            int y = ((Byte) sectionValues.get("Y").getValue()).intValue();

            sectionsBlocks[y] = blocks;
        }
    }

    public boolean isSectionPresent(int y) {
        return sectionsBlocks[y] != null;
    }

    public byte[] getSectionBlocks(int y) {
        return sectionsBlocks[y];
    }
}
