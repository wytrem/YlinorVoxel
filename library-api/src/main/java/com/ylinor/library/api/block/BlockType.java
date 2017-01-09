package com.ylinor.library.api.block;

import org.jetbrains.annotations.Nullable;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;


/**
 * Un type de block
 *
 * A compléter
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class BlockType {
    /**
     * Les BlockType enregistrés
     */
    private static final TShortObjectMap<BlockType> types = new TShortObjectHashMap<>();
    public static final BlockType air = new BlockType(0);
    public static final BlockType dirt = new BlockType(1);
    public static final BlockType stone = new BlockType(2);

    /**
     * L'id de ce BlockType
     */
    private final short id;

    /**
     * Un BlockType simple
     */
    public BlockType(int id) {
        if (types.containsKey((short) id)) {
            throw new IllegalArgumentException("Slot " + id + " already used.");
        }

        this.id = (short) id;
        types.put(this.id, this);
    }

    /**
     * @return L'id de ce BlockType
     */
    public short getId() {
        return id;
    }

    /**
     * Retourne le block type correspondant à l'id donné
     *
     * @param id L'id du block type à retourner
     *
     * @return Le block type trouvé ou null si inexistant
     */
    @Nullable
    public static BlockType getByID(int id) {
        if (!types.containsKey((short) id)) {
            return air;
        }

        return types.get((short) id);
    }
}
