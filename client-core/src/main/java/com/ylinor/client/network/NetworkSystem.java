package com.ylinor.client.network;

import com.artemis.BaseSystem;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.packets.Packet;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Deque;

public final class NetworkSystem extends BaseSystem {
    private final Client client;
    private final Deque<Packet> packetQueue;

    public NetworkSystem() {
        this.client = new Client();
        client.start();

        Packet.registerToKryo(client.getKryo());

        this.packetQueue = new ArrayDeque<>();
    }

    @Override
    protected void processSystem() {
        Packet packet;

        while ((packet = packetQueue.poll()) != null) {
            client.sendTCP(packet);
        }
    }

    public void init(InetAddress address, int port) throws IOException {
        client.connect(10000, address, port);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {

            }
        });
    }

    public void enqueuePacket(Packet packet) {
        packetQueue.addLast(packet);
    }
}
