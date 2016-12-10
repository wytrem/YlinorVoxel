package com.ylinor.library.math;

/**
 * Une position en trois dimensions
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class Position3D implements Positionable3D
{
    /**
     * La position X de l'objet
     */
    private int x;

    /**
     * La position Y de l'objet
     */
    private int y;

    /**
     * La position Z de l'objet
     */
    private int z;

    /**
     * Une position en trois dimensions
     *
     * @param x La positoin X de l'objet
     * @param y La position Y de l'objet
     * @param z La position Z de l'objet
     */
    public Position3D(int x, int y, int z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public int getX()
    {
        return x;
    }

    /**
     * Définit la position X de l'objet
     *
     * @param x La nouvelle position X
     */
    public void setX(int x)
    {
        this.x = x;
    }

    @Override
    public int getY()
    {
        return y;
    }

    /**
     * Définit la position Y de l'objet
     *
     * @param y La nouvelle position Y
     */
    public void setY(int y)
    {
        this.y = y;
    }

    @Override
    public int getZ()
    {
        return z;
    }

    /**
     * Définit la position Z de l'objet
     *
     * @param z La nouvelle position Z
     */
    public void setZ(int z)
    {
        this.z = z;
    }
}
