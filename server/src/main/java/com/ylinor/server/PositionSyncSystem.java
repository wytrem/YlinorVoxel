package com.ylinor.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joml.Vector3f;

import com.ylinor.library.api.ecs.components.Player;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.packets.PacketPositionAndRotationUpdate;


@Singleton
public class PositionSyncSystem extends TickingIteratingSystem {
    @Inject
    ServerNetworkSystem networkSystem;

    public PositionSyncSystem() {
        super(Aspect.all(Player.class, Position.class, Rotation.class, PlayerConnection.class));
    }

    @Override
    protected void tickEntity(Entity entity) {
        Vector3f position = entity.get(Position.class).position;
        Rotation rotation = entity.get(Rotation.class);
        PlayerConnection connection = entity.get(PlayerConnection.class);
        networkSystem.sendToAllExcept(connection.getConnectionId(), new PacketPositionAndRotationUpdate(entity.getEntityId(), position.x, position.y, position.z, rotation.rotationPitch, rotation.rotationYaw));
    }
}
