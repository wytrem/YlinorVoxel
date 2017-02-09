package com.ylinor.client.physics;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.bulletphysics.linearmath.Transform;
import com.ylinor.client.YlinorClient;


public class BulletEntitiesSystem extends IteratingSystem {
    final static short GROUND_FLAG = 1 << 8;
    final static short OBJECT_FLAG = 1 << 9;
    final static short ALL_FLAG = -1;
    
    @Wire
    private ComponentMapper<BulletEntity> bulletEntityMapper;

    @Wire
    private BulletDynamicsProcessingSystem dynamicsProcessingSystem;
    
    @Wire
    private ComponentMapper<Position> positionMapper;
    
    @Wire
    private YlinorClient client;

    public BulletEntitiesSystem() {
        super(Aspect.all(BulletEntity.class));
    }

    @Override
    protected void inserted(int entityId) {
        BulletEntity bulletEntity = bulletEntityMapper.get(entityId);

        if (bulletEntity.rigidBody != null) {
            dynamicsProcessingSystem.getDynamicsWorld()
                                    .addRigidBody(bulletEntity.rigidBody);
        }
    }
    
    private Transform tmp = new Transform();

    @Override
    protected void process(int entityId) {
        if (positionMapper.has(entityId)) {
            BulletEntity bulletEntity = bulletEntityMapper.get(entityId);
            Position position = positionMapper.get(entityId);
            bulletEntity.rigidBody.getWorldTransform(tmp);
            position.position.set(tmp.origin.x, tmp.origin.y, tmp.origin.z);
        }
    }

    @Override
    protected void removed(int entityId) {
        BulletEntity bulletEntity = bulletEntityMapper.get(entityId);
        
        if (bulletEntity.rigidBody != null) {
            dynamicsProcessingSystem.getDynamicsWorld()
                                    .removeRigidBody(bulletEntity.rigidBody);
        }
    }
    
    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
