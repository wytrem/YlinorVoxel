package com.ylinor.server.main;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.ecs.WorldConfiguration;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketDespawnEntity;
import com.ylinor.packets.PacketPositionAndRotationUpdate;
import com.ylinor.packets.PacketSpawnEntity;
import com.ylinor.server.CommandLineThread;
import com.ylinor.server.Player;
import com.ylinor.server.PlayerConnection;
import org.joml.Vector3f;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class YlinorServer extends YlinorApplication {
    private final World world;
    private List<Player> onlinePlayers;
    private Server server;
    private volatile boolean running;

    private YlinorServer() {
        this.world = buildWorld();
        this.onlinePlayers = new ArrayList<>();
        this.server = new Server();
        this.running = true;

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                PlayerConnection playerConnection = new PlayerConnection(connection);
                server.addListener(playerConnection.getConnectionListener());

                onlinePlayers.add(new Player(playerConnection, world.create()));
            }
        });
        server.start();
        Packet.registerToKryo(server.getKryo());

        new CommandLineThread(this).start();
    }

    @Override
    protected void configure(WorldConfiguration configuration) {
        super.configure(configuration);
    }

    private void run() throws IOException {
        final float tps = 20.0f;
        Timer timer = new Timer(tps);

        server.bind(new InetSocketAddress(18325), null);

        try {
            while (running) {
                timer.updateTimer();

                if (timer.elapsedTicks > 0) {
                    // TODO check delta time
                    world.delta = 1.0f / tps * timer.elapsedTicks;
                    world.tick();

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

        server.stop();
    }

    private void tick() {
        Iterator<Player> onlinePlayersIt = onlinePlayers.iterator();

        while (onlinePlayersIt.hasNext()) {
            Player player = onlinePlayersIt.next();
            PlayerConnection playerConnection = player.getPlayerConnection();

            if (playerConnection.shouldDisconnect()) {
                playerConnection.close();
                server.removeListener(playerConnection.getConnectionListener());

                for (Player onlinePlayer : getOnlinePlayers()) {
                    if (onlinePlayer != player) {
                        List<Player> nearbyPlayers = onlinePlayer.getNearbyPlayers();

                        if (nearbyPlayers.contains(player)) {
                            nearbyPlayers.remove(player);

                            onlinePlayer.getPlayerConnection().sendPacket(new PacketDespawnEntity(player.getEntityID()));
                        }
                    }
                }

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
        server.sendToAllExceptTCP(connectionID, new PacketPositionAndRotationUpdate(player.getEntityID(), playerPosition.x, playerPosition.y, playerPosition.z, player.getPitch(), player.getYaw()));

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
                        onlinePlayer.getPitch(),
                        onlinePlayer.getYaw()));

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

    public static YlinorServer getInstance() {
        return (YlinorServer) instance;
    }

    public static void main(String[] args) {
        instance = new YlinorServer();

        try {
            getInstance().run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getVersion() {
        return "0.1";
    }
}
