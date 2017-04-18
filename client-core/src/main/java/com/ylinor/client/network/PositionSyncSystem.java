package com.ylinor.client.network;

import com.ylinor.client.physics.components.Heading;
import com.ylinor.client.physics.components.Position;
import com.ylinor.library.api.ecs.systems.TickingIteratingSystem;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.packets.PacketPositionAndRotationUpdate;
import org.joml.Vector3f;

import javax.inject.Inject;

public final class PositionSyncSystem extends TickingIteratingSystem {

    @Inject
    protected ClientNetworkSystem networkSystem;

    public PositionSyncSystem() {
        super(Aspect.all(PositionSyncComponent.class, Position.class, Heading.class));
    }

    @Override
    protected void tickEntity(Entity i) {
        Vector3f currentPosition = i.get(Position.class).position;
        Vector3f currentHeading = i.get(Heading.class).heading;

        networkSystem.enqueuePacket(new PacketPositionAndRotationUpdate(-1, currentPosition.x, currentPosition.y, currentPosition.z, currentHeading.x, currentHeading.y));
    }
}
