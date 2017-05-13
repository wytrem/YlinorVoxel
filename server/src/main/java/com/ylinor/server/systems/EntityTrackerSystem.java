package com.ylinor.server.systems;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joml.Vector3f;

import com.ylinor.library.api.ecs.components.Player;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.api.protocol.packets.PacketPositionAndRotationUpdate;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;


@Singleton
public class EntityTrackerSystem extends TickingIteratingSystem {
    @Inject
    ServerNetworkSystem networkSystem;

    public EntityTrackerSystem() {
        super(Aspect.all(Player.class, Position.class, Rotation.class));
    }

    @Override
    protected void tickEntity(Entity entity) {
        Vector3f position = entity.get(Position.class).position;
        Rotation rotation = entity.get(Rotation.class);
        Player player = entity.get(Player.class);
        networkSystem.sendToAllExcept(player.getSenderObject(), new PacketPositionAndRotationUpdate(entity.getEntityId(), position.x, position.y, position.z, rotation.rotationPitch, rotation.rotationYaw));
    }
}
