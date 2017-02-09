package com.ylinor.client.physics;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.ylinor.client.YlinorClient;
import com.ylinor.library.api.events.terrain.ChunkLoadedEvent;
import com.ylinor.library.api.terrain.Chunk;

import net.mostlyoriginal.api.event.common.Subscribe;


public class BulletDynamicsProcessingSystem extends BaseSystem {
    private CollisionConfiguration collisionConfig;
    private Dispatcher dispatcher;
    private BroadphaseInterface broadphaseInterface;
    private ConstraintSolver constraintSolver;
    private DynamicsWorld dynamicsWorld;
    @Wire
    private YlinorClient client;

    @Override
    protected void initialize() {
        collisionConfig = new DefaultCollisionConfiguration();
        dispatcher = new CollisionDispatcher(collisionConfig);
        broadphaseInterface = new DbvtBroadphase();
        constraintSolver = new SequentialImpulseConstraintSolver();
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphaseInterface, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new javax.vecmath.Vector3f(0, -8.91f, 0));
    }

    @Override
    protected void processSystem() {
        dynamicsWorld.stepSimulation(world.delta, 5, 1f / 60f);
    }

    @Subscribe
    public void chunkLoaded(ChunkLoadedEvent event) {
        dynamicsWorld.addRigidBody(buildChunkBody(event.loaded));
    }

    RigidBodyConstructionInfo constructionInfo = new RigidBodyConstructionInfo(0.0f, null, new BoxShape(new javax.vecmath.Vector3f(Chunk.SIZE_X / 2, Chunk.SIZE_Y / 2, Chunk.SIZE_Z / 2)));

    private RigidBody buildChunkBody(Chunk chunk) {

        constructionInfo.startWorldTransform.setIdentity();
        constructionInfo.startWorldTransform.origin.set(chunk.x * 16 + Chunk.SIZE_X / 2, Chunk.SIZE_Y / 2, chunk.z * 16 + Chunk.SIZE_Z / 2);
        return new RigidBody(constructionInfo);
    }

    public DynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }
    
    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
