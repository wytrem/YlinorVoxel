package com.ylinor.client.physics.bullet;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.broadphase.Dispatcher;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.BvhTriangleMeshShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.DynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.events.ChunkRendererUpdatedEvent;
import com.ylinor.client.render.TerrainRenderSystem;
import com.ylinor.library.api.terrain.Chunk;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;
import net.mostlyoriginal.api.event.common.Subscribe;


public class BulletDynamicsProcessingSystem extends BaseSystem {
    private CollisionConfiguration collisionConfig;
    private Dispatcher dispatcher;
    private BroadphaseInterface broadphaseInterface;
    private ConstraintSolver constraintSolver;
    private DynamicsWorld dynamicsWorld;
    @Wire
    private YlinorClient client;

    @Wire
    private TerrainRenderSystem terrainRenderSystem;

    @Override
    protected void initialize() {
        collisionConfig = new DefaultCollisionConfiguration();
        dispatcher = new CollisionDispatcher(collisionConfig);
        broadphaseInterface = new DbvtBroadphase();
        constraintSolver = new SequentialImpulseConstraintSolver();
        dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphaseInterface, constraintSolver, collisionConfig);
        dynamicsWorld.setGravity(new javax.vecmath.Vector3f(0, -9.81f, 0));
    }

    @Override
    protected void processSystem() {
        dynamicsWorld.stepSimulation(world.delta, 5, 1f / 60f);
    }

    TLongObjectMap<RigidBody> chunkBodies = new TLongObjectHashMap<RigidBody>();

    @Subscribe
    public void chunkRenderUpdated(ChunkRendererUpdatedEvent event) {
        
        RigidBody body;
        
        if (!chunkBodies.containsKey(event.chunk.id)) {
            body = buildChunkBody(event.chunk);
            chunkBodies.put(event.chunk.id, body);
            dynamicsWorld.addRigidBody(body);
        }
        else {
            body = chunkBodies.get(event.chunk.id);
            dynamicsWorld.removeRigidBody(body);
            body.setCollisionShape(buildChunkCollisionShape(event.chunk));
            dynamicsWorld.addRigidBody(body);
        }
        
    }

    RigidBodyConstructionInfo constructionInfo = new RigidBodyConstructionInfo(0.0f, null, new BoxShape(new javax.vecmath.Vector3f(Chunk.SIZE_X / 2, Chunk.SIZE_Y / 2, Chunk.SIZE_Z / 2)));

    private RigidBody buildChunkBody(Chunk chunk) {

        constructionInfo.startWorldTransform.setIdentity();
        constructionInfo.startWorldTransform.origin.set(chunk.x * 16 + Chunk.SIZE_X / 2, Chunk.SIZE_Y / 2, chunk.z * 16 + Chunk.SIZE_Z / 2);

        RigidBody body = new RigidBody(0.0f, null, buildChunkCollisionShape(chunk));
        return body;
    }

    private CollisionShape buildChunkCollisionShape(Chunk chunk) {

        return new BvhTriangleMeshShape(new ChunkStridingMesh(terrainRenderSystem.getChunkRenderer(chunk)), false);
    }

    public DynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
