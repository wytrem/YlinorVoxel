package com.ylinor.client.render;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.ylinor.client.physics.BulletEntity;
import com.ylinor.client.physics.Heading;
import com.ylinor.client.physics.InputControlledEntity;
import com.ylinor.client.physics.Position;
import com.ylinor.client.physics.Velocity;


/**
 * Initializes the player entity in the world. Used only for debugging.
 * 
 * @author wytrem
 */
public class PlayerInitSystem extends BaseSystem {

    @Override
    protected void initialize() {
        Archetype playerArchetype = new ArchetypeBuilder().add(RenderViewEntity.class)
                                                          .add(InputControlledEntity.class)
                                                          .add(Heading.class)
                                                          .add(Position.class)
                                                          .add(Velocity.class)
                                                          .add(EyeHeight.class)
                                                          .add(BulletEntity.class)
                                                          .build(world);
        
        RigidBodyConstructionInfo bodyConstructionInfo = new RigidBodyConstructionInfo(100.0f, null, new BoxShape(new javax.vecmath.Vector3f(0.2f,0.8f,0.2f)));
        
        bodyConstructionInfo.startWorldTransform.setIdentity();
        bodyConstructionInfo.startWorldTransform.origin.set(0, 300, 0);
        RigidBody body = new RigidBody(bodyConstructionInfo);
        body.setCollisionFlags(body.getCollisionFlags() | CollisionFlags.CUSTOM_MATERIAL_CALLBACK);
        
        Entity player = world.createEntity(playerArchetype);
        player.getComponent(BulletEntity.class).rigidBody = body;
        player.getComponent(EyeHeight.class).eyePadding.y = 0.7f;
    }

    @Override
    protected void processSystem() {

    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }
}
