package com.ylinor.server.main;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketPositionUpdate;
import com.ylinor.packets.PacketSpawnEntity;
import com.ylinor.server.CommandLineThread;
import com.ylinor.server.EntityIDAllocator;
import com.ylinor.server.Player;
import com.ylinor.server.PlayerConnection;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class YlinorServer {
    private static YlinorServer instance;
    private List<Player> onlinePlayers;
    private Server server;
    private volatile boolean running;

    private YlinorServer() {
        this.onlinePlayers = new ArrayList<>();
        this.server = new Server();
        this.running = true;

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                PlayerConnection playerConnection = new PlayerConnection(connection);
                server.addListener(playerConnection.getConnectionListener());

                onlinePlayers.add(new Player(playerConnection, EntityIDAllocator.allocateEntityID()));
            }
        });
        server.start();
        Packet.registerToKryo(server.getKryo());

        new CommandLineThread(this).start();
    }

    private void run() throws IOException {
        Timer timer = new Timer(20.0f);

        server.bind(25565);

        try {
            while (running) {
                timer.updateTimer();

                if (timer.elapsedTicks > 0) {
                    for (int i = 0; i < timer.elapsedTicks; i++) {
                        tick();
                    }
                } else {
                    Thread.yield();
                }
            }
        } finally {
            shutdown();
        }
    }

    private void shutdown() {
        System.out.println("Shutting down server...");

        for (Player player : onlinePlayers) {
            player.kick("Server is shutting down");
            player.getPlayerConnection().close();
        }
    }

    private void tick() {
        Iterator<Player> onlinePlayersIt = onlinePlayers.iterator();

        while (onlinePlayersIt.hasNext()) {
            Player player = onlinePlayersIt.next();
            PlayerConnection playerConnection = player.getPlayerConnection();

            if (playerConnection.shouldDisconnect()) {
                playerConnection.close();
                server.removeListener(playerConnection.getConnectionListener());

                onlinePlayersIt.remove();
            } else {
                if (playerConnection.isLogged()) {
                    handlePlayer(player, playerConnection.getConnection().getID());
                }
            }
        }
    }

    private void handlePlayer(Player player, int connectionID) {
        Vector3f playerPosition = player.getPosition();

        // TODO only nearby players should be notified about the position of this player
        server.sendToAllExceptTCP(connectionID, new PacketPositionUpdate(player.getEntityID(), playerPosition.x, playerPosition.y, playerPosition.z));

        List<Player> nearbyPlayers = player.getNearbyPlayers();

        getOnlinePlayers().forEach(onlinePlayer -> {
            // TODO distance check

            Vector3f onlinePlayerPosition = onlinePlayer.getPosition();

            if (onlinePlayer != player && !nearbyPlayers.contains(onlinePlayer)) {
                nearbyPlayers.add(onlinePlayer);
                player.getPlayerConnection().sendPacket(new PacketSpawnEntity(
                        onlinePlayer.getEntityID(),
                        onlinePlayerPosition.x,
                        onlinePlayerPosition.y,
                        onlinePlayerPosition.z,
                        0.0f,
                        0.0f));

                System.out.println("[Debug] Sending spawn entity packet to connection " + connectionID);
            }
        });
    }

    public List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>(onlinePlayers.size());

        onlinePlayers.forEach(player -> {
           if (player.getPlayerConnection().isLogged()) {
               players.add(player);
           }
        });

        return players;
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) {
        instance = new YlinorServer();

        try {
            instance.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
