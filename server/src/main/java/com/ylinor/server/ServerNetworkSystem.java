package com.ylinor.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.ylinor.library.api.ecs.systems.TickingSystem;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketDespawnEntity;
import com.ylinor.packets.PacketPositionAndRotationUpdate;
import com.ylinor.packets.PacketSpawnEntity;

public class ServerNetworkSystem extends TickingSystem {
    
    private static final Logger logger = LoggerFactory.getLogger(ServerNetworkSystem.class);

    private List<Player> onlinePlayers;
    private Server server;
    
    @Override
    public void initialize() {
        this.onlinePlayers = new ArrayList<>();
        this.server = new Server();
        
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

        try {
            server.bind(new InetSocketAddress(25565), null);
        }
        catch (IOException e) {
            logger.error("Failed to bind server network :", e);
        }
    }
    
    @Override
    protected void tick() {
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

                
                logger.trace("Sending spawn entity packet to connection {}", connectionID);
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
    
    
    
    public void sendToAllTCP(Object object) {
        server.sendToAllTCP(object);
    }

    public void sendToAllExceptTCP(int connectionID, Object object) {
        server.sendToAllExceptTCP(connectionID, object);
    }

    public void sendToTCP(int connectionID, Object object) {
        server.sendToTCP(connectionID, object);
    }

    @Override
    public void dispose() {
        logger.info("Closing connections");
        
        for (Player player : onlinePlayers) {
            player.kick("Server is shutting down");
            player.getPlayerConnection().close();
        }

        server.stop();
    }
}
