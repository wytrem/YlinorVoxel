package com.ylinor.client.network;

import javax.inject.Inject;

import org.joml.Vector3f;

import com.ylinor.library.api.ecs.components.Heading;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.packets.PacketPositionAndRotationUpdate;


public final class PositionSyncSystem extends TickingIteratingSystem {

    @Inject
    protected ClientNetworkSystem networkSystem;

    public PositionSyncSystem() {
        super(Aspect.all(PositionSyncComponent.class, Position.class, Heading.class, Rotation.class));
    }

    @Override
    protected void tickEntity(Entity i) {
        Vector3f currentPosition = i.get(Position.class).position;
        Rotation rotation = i.get(Rotation.class);
        networkSystem.send(new PacketPositionAndRotationUpdate(-1, currentPosition.x, currentPosition.y, currentPosition.z, rotation.rotationPitch, rotation.rotationYaw));
    }

    @Override
    protected boolean checkProcessing() {
        return networkSystem.loggedIn;
    }
}
