package com.ylinor.client.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ylinor.client.network.PositionSyncComponent;
import com.ylinor.client.physics.components.AABB;
import com.ylinor.client.physics.components.CollisionState;
import com.ylinor.client.physics.components.Heading;
import com.ylinor.client.physics.components.InputControlledEntity;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.client.physics.components.Position;
import com.ylinor.client.physics.components.Rotation;
import com.ylinor.client.physics.components.Size;
import com.ylinor.client.physics.components.Velocity;
import com.ylinor.client.physics.systems.PhySystem;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.BaseSystem;


/**
 * Initializes the player entity in the world. Used only for debugging.
 * 
 * @author wytrem
 */
@Singleton
public class PlayerInitSystem extends BaseSystem {

    @Inject
    private PhySystem phySystem;
    private Entity player;

    @Override
    public  void initialize() {
        player = world.create();
        player.set(RenderViewEntity.class)
              .set(InputControlledEntity.class)
              .set(Heading.class)
              .set(Position.class)
              .set(Velocity.class)
              .set(EyeHeight.class)
              .set(Size.class)
              .set(AABB.class)
              .set(CollisionState.class)
              .set(Physics.class).set(Rotation.class)
              .set(PositionSyncComponent.class);
        player.get(EyeHeight.class).eyePadding.y = 1.65f;
        player.get(Size.class).setSize(0.6f, 1.8f);
    }

    @Override
    protected void processSystem() {
        if (player != null) {
            phySystem.setPosition(player, 1818, 126, 6710);
            player = null;
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
