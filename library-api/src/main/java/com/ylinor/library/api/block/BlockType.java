package com.ylinor.library.api.block;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private static final List<BlockType> types = new ArrayList<>(); static {
        types.add(null);
    }

    /**
     * L'id de ce BlockType
     */
    private int id;

    /**
     * Un BlockType simple
     */
    public BlockType()
    {
        this.id = types.size();
        types.add(this);
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
        return types.get(id);
    }

    /**
     * @return Les BlockType enregistrés
     */
    @NotNull
    public static BlockType[] getTypes()
    {
        return types.toArray(new BlockType[types.size()]);
    }
}
