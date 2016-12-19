package com.ylinor.library.network;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;
import com.ylinor.library.network.protocol.IProtocol;
import com.ylinor.library.network.util.PairPacket;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author pierre
 * @since 1.0.0
 */
public abstract class AbstractNetwork extends Thread
{

    /**
     * Protocol de redirection de packet
     */
    protected IProtocol protocol;

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

    /**
     * Queue de packet a envoyer
     */
    protected Queue<PairPacket<IPacket, INetworkEntity>> packetQueue =new ArrayBlockingQueue<>(10);

    /**
     * Boolean permettant a la boucle d'envoie de packet de savoir si le service est lancé
     */
    protected boolean isStarted;


    public AbstractNetwork(Kryo kryo, String ip, int port, IProtocol protocol)
    {
        this.kryo = kryo;
        this.ip = ip;
        this.port = port;
        this.protocol = protocol;
    }


    /**
     * Appellé pour envoyer un packet
     *
     * @param packet packet a envoyé
     * @param entity entitée reseau dont le packet provient
     */
    public abstract void sendPacket(IPacket packet, INetworkEntity entity);

    public void end()
    {
        isStarted = false;
    }


}
