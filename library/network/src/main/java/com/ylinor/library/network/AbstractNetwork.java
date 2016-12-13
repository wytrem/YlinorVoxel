package com.ylinor.library.network;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;

/**
 * @author pierre
 * @since 1.0.0
 */
public abstract class AbstractNetwork extends Thread
{

    /**
     * Limite de temps de connection
     */
    public static final int CONNECT_TIMEOUT = 5000;

    /**
     * Instance de kryo (utilisé pour la sérialization)
     */
    protected Kryo kryo;

    /**
     * Ip sur lequel le service va se lancer
     */
    protected String ip;

    /**
     * Port de connection
     *
     */
    protected int port;

    public AbstractNetwork(Kryo kryo, String ip, int port)
    {
        this.kryo = kryo;
        this.ip = ip;
        this.port = port;
    }


    /**
     * Appellé pour envoyer un packet
     *
     * @param packet packet a envoyé
     * @param entity entitée reseau dont le packet provient
     */
    public abstract void sendPacket(IPacket packet, INetworkEntity entity);




}
