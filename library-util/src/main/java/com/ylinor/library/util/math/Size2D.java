package com.ylinor.library.util.math;

/**
 * Une taille dans un plan en deux dimensions;
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class Size2D implements Sizeable2D
{
    private int sizeX;
    private int sizeY;

    public Size2D()
    {
        this.sizeX = sizeY = 0;
    }

    public Size2D(int sizeX, int sizeY)
    {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    @Override
    public int getSizeX()
    {
        return sizeX;
    }

    public void setSizeX(int sizeX)
    {
        this.sizeX = sizeX;
    }

    @Override
    public int getSizeY()
    {
        return sizeY;
    }

    public void setSizeY(int sizeY)
    {
        this.sizeY = sizeY;
    }
}
