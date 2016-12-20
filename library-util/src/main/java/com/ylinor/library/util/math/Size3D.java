package com.ylinor.library.util.math;

/**
 * Une taille dans un plan en trois dimensions
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class Size3D extends Size2D implements Sizeable3D
{
    /**
     * La taille sur l'axe Z
     */
    private int sizeZ;

    /**
     * Un objet size 0, 0, 0
     */
    public Size3D()
    {
        super();

        this.sizeZ = 0;
    }

    /**
     * Un objet Size3D
     *
     * @param sizeX La taille sur l'axe X
     * @param sizeY La taille sur l'axe Y
     * @param sizeZ La taille sur l'axe Z
     */
    public Size3D(int sizeX, int sizeY, int sizeZ)
    {
        super(sizeX, sizeY);

        this.sizeZ = sizeZ;
    }

    @Override
    public int getSizeZ()
    {
        return sizeZ;
    }

    /**
     * Définit la taille de l'objet sur l'axe Z
     *
     * @param sizeZ La nouvelle taille Z
     */
    public void setSizeZ(int sizeZ)
    {
        this.sizeZ = sizeZ;
    }
}
