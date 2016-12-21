package com.ylinor.library.api.block;

import org.jetbrains.annotations.Nullable;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Un type de block
 *
 * A compléter
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class BlockType
{
    /**
     * Les BlockType enregistrés
     */
    private static final TIntObjectMap<BlockType> types = new TIntObjectHashMap<>();
    public static final BlockType air = new BlockType(0);
    public static final BlockType dirt = new BlockType(1);
    public static final BlockType stone = new BlockType(2);
    
    /**
     * L'id de ce BlockType
     */
    private final int id;

    /**
     * Un BlockType simple
     */
    public BlockType(int id)
    {
        if (types.containsKey(id))
        {
            throw new IllegalArgumentException("Slot " + id + " already used.");
        }
        
        this.id = id;
        types.put(id, this);
    }

    /**
     * @return L'id de ce BlockType
     */
    public int getId()
    {
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
    public static BlockType getByID(int id)
    {
        if (!types.containsKey(id))
        {
            return air;
        }
        
        return types.get(id);
    }
}
