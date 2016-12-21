package com.ylinor.library.api.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.ylinor.library.api.block.BlockType;
import com.ylinor.library.util.math.Positionable2D;
import com.ylinor.library.util.math.Positionable3D;
import com.ylinor.library.util.math.Size3D;
import com.ylinor.library.util.math.Sizeable3D;
import com.ylinor.library.util.spring.Assert;


/**
 * Un Chunk de monde
 *
 * Un Chunk est une partie d'un monde, c'est un pavé droit à base carée dont
 * celle-ci a les dimensions données (qui devraient être puissance de deux, et
 * il a la même hauteur que le monde lui même.
 *
 * Le monde est uniquement composé de Chunk, situés comme une grille en deux
 * dimensions (la hauteur est celle du monde) et les chunks eux contiennent les
 * blocks.
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class Chunk implements Positionable2D, Sizeable3D
{
    /**
     * La taille d'un côté d'un chunk
     */
    public static final int CHUNK_SIZE_X = 16;
    public static final int CHUNK_SIZE_Y = 256;
    public static final int CHUNK_SIZE_Z = 16;

    /**
     * Le monde où est ce Chunk
     */
    @NotNull
    private World world;

    /**
     * La position du chunk, en 2D car un chunk a toujours une position Y 0 dans
     * un plan en 3D.
     *
     * La position est renseignée en chunk, le premier chunk a 0 pour position,
     * le deuxième 1 être. La position n'est PAS renseignée en block.
     */
    @NotNull
    private Positionable2D position;

    /**
     * Les id des types de blocks du chunk
     *
     * Les trois tableaux correspondent aux trois axes blocks[1][2][3]
     * correspond au block à x: 1, y: 2, z: 3
     */
    @NotNull
    private int[][][] blocks;

    /**
     * La taille du Chunk
     */
    private Size3D size;

    /**
     * Les données des blocks du chunk, null si c'est un block simple sans
     * données particulières
     *
     * Les trois tableaux correspondent aux trois axes data[1][2][3] correspond
     * au données du block à x: 1, y: 2, z: 3
     */
    private BlockData[][][] data;

    /**
     * Un chunk (Portion d'un monde)
     *
     * @param world Le monde qui contient ce chunk
     * @param position La position du Chunk, en nombre de Chunk, sur la grille
     *        de Chunk du monde.
     */
    public Chunk(@NotNull World world, @NotNull Positionable2D position) throws IllegalArgumentException
    {
        Assert.notNull(world, "world cannot be null");
        Assert.notNull(position, "position cannot be null");

        this.world = world;
        this.position = position;
        this.size = new Size3D(CHUNK_SIZE_X, CHUNK_SIZE_Y, CHUNK_SIZE_Z);
        this.blocks = new int[size.getSizeX()][size.getSizeY()][size.getSizeZ()];
        this.data = new BlockData[size.getSizeX()][size.getSizeY()][size.getSizeZ()];
    }

    /**
     * Change les données bloc à la position donnée. Peut être null si c'est un
     * block non particulier.
     *
     * Si le type est différent de celui actuel, il sera changé.
     *
     * La position doit être exprimée en bloc, et surtout, elle doit être
     * relative au chunk et donc ni être plus basse que zéro, ni plus haute que
     * la taille du Chunk.
     *
     * @param position La position X du bloc
     * @param block Le données à définir (peut être null)
     */
    public void setBlock(@NotNull Positionable3D position, @Nullable BlockData block)
    {
        this.setBlock(position.getX(), position.getY(), position.getZ(), block);
    }

    /**
     * Change les données bloc à la position donnée. Peut être null si c'est un
     * block non particulier.
     *
     * Si le type est différent de celui actuel, il sera changé.
     *
     * La position doit être exprimée en bloc, et surtout, elle doit être
     * relative au chunk et donc ni être plus basse que zéro, ni plus haute que
     * la taille du Chunk.
     *
     * @param x La position X du bloc
     * @param y La position Y du bloc
     * @param z La position Z du block
     * @param block Le données à définir (peut être null)
     */
    public void setBlock(int x, int y, int z, @Nullable BlockData block)
    {
        data[x][y][z] = block;

        if (block != null && blocks[x][y][z] != block.getType().getId())
        {
            blocks[x][y][z] = block.getType().getId();
        }

        // TODO: BlockData change event
    }

    /**
     * Change le type de bloc à la position donnée.
     *
     * La position doit être exprimée en bloc, et surtout, elle doit être
     * relative au chunk et donc ni être plus basse que zéro, ni plus haute que
     * la taille du Chunk.
     *
     * @param position La position X du bloc
     * @param type Le type de bloc à définir
     */
    public void setBlock(@NotNull Positionable3D position, @NotNull BlockType type)
    {
        this.setBlock(position.getX(), position.getY(), position.getZ(), type);
    }

    /**
     * Change le bloc à la position donnée.
     *
     * La position doit être exprimée en bloc, et surtout, elle doit être
     * relativ&
     *
     * @param x La position X du bloc
     * @param y La position Y du bloc
     * @param z La position Z du block
     * @param type Le bloc à définir
     */
    public void setBlock(int x, int y, int z, @NotNull BlockType type)
    {
        blocks[x][y][z] = type.getId();
        // TODO: BlockType change event
    }

    /**
     * Retourne le block à la position donnée, ou null si il n'y en a pas
     *
     * La position doit être exprimée en bloc, et surtout, elle doit être
     * relative au chunk et donc ni être plus basse que zéro, ni plus haute que
     * la taille du Chunk.
     *
     * @param pos La position du block à retourner
     *
     * @return Le block à la position donnée (null si il n'y en a pas)
     */
    @Nullable
    public BlockType getBlock(@NotNull Positionable3D pos)
    {
        return BlockType.getByID(getBlockId(pos));
    }

    /**
     * Retourne l'id du block à la position donnée
     *
     * La position doit être exprimée en bloc, et surtout, elle doit être
     * relative au chunk et donc ni être plus basse que zéro, ni plus haute que
     * la taille du Chunk.
     *
     * @param pos La position du block en question
     *
     * @return L'id du type de block (0 si pas de block)
     */
    public int getBlockId(@NotNull Positionable3D pos)
    {
        return blocks[pos.getX()][pos.getY()][pos.getZ()];
    }

    /**
     * Retourne les données du block à la position donnée
     *
     * La position doit être exprimée en bloc, et surtout, elle doit être
     * relative au chunk et donc ni être plus basse que zéro, ni plus haute que
     * la taille du Chunk.
     *
     * @param pos La position du block en question
     *
     * @return Les données (null si block non partuculier)
     */
    @Nullable
    public BlockData getBlockData(@NotNull Positionable3D pos)
    {
        return data[pos.getX()][pos.getY()][pos.getZ()];
    }

    /**
     * @return Le monde qui contient ce chunk
     */
    @NotNull
    public World getWorld()
    {
        return world;
    }

    /**
     * @return Les id des types de blocks du chunk
     *
     *         Les trois tableaux correspondent aux trois axes
     *         getBlocks()[1][2][3] correspond au block à x: 1, y: 2, z: 3
     */
    @NotNull
    public int[][][] getBlocks()
    {
        return blocks;
    }

    /**
     * @return Les données des blocks du chunk, null si c'est un block simple
     *         sans données particulières
     *
     *         Les trois tableaux correspondent aux trois axes
     *         getBlocksData()[1][2][3] correspond au données du block à x: 1,
     *         y: 2, z: 3
     */
    @NotNull
    public BlockData[][][] getBlocksData()
    {
        return data;
    }

    /**
     * @return La taille du Chunk
     */
    @NotNull
    public Sizeable3D getSize()
    {
        return size;
    }

    /**
     * @return La position du Chunk
     */
    @NotNull
    public Positionable2D getPosition()
    {
        return position;
    }

    @Override
    public int getX()
    {
        return position.getX();
    }

    @Override
    public int getY()
    {
        return position.getY();
    }

    @Override
    public int getSizeX()
    {
        return getSize().getSizeX();
    }

    @Override
    public int getSizeY()
    {
        return getSize().getSizeY();
    }

    @Override
    public int getSizeZ()
    {
        return getSize().getSizeZ();
    }
}
