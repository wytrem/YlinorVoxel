package com.ylinor.library.packets;

import com.ylinor.library.network.packet.Packet;

/**
 * @author pierre
 * @since 1.0.0
 */
public class Packet0KeepAlive extends Packet
{
    private int randomID;

    /**
     * Toujours avoir un constructeur vide
     */
    public Packet0KeepAlive()
    {
    }

    public Packet0KeepAlive(int randomID)
    {
        this.randomID = randomID;
    }

    public int getRandomID()
    {
        return randomID;
    }

    public void setRandomID(int randomID)
    {
        this.randomID = randomID;
    }
}
