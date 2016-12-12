package com.ylinor.library.network;

import com.esotericsoftware.kryo.Kryo;
import com.ylinor.library.network.packet.INetworkEntity;
import com.ylinor.library.network.packet.IPacket;

/**
 * @author pierre
 * @since 1.0.0
 */
public abstract class AbstractNetwork
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
     * Appellé pour lancer le service network
     *
     * @param ip ip sur laquel le serveur va écouté/ sur laquel le client va se connecter
     * @param port port de connection
     */
    public abstract void start(String ip, int port);

    /**
     * Appellé pour envoyer un packet
     *
     * @param packet packet a envoyé
     * @param entity entitée reseau dont le packet provient
     */
    public abstract void sendPacket(IPacket packet, INetworkEntity entity);


}
