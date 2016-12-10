package com.ylinor.library.math;

/**
 * Une position en deux dimensions
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class Position2D implements Positionable2D
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
     * Une position en deux dimensions
     *
     * @param x La position X de l'objet
     * @param y La position Y de l'objet
     */
    public Position2D(int x, int y)
    {
        this.x = x;
        this.y = y;
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
}
