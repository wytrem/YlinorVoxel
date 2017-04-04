package com.ylinor.client.render;

import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.physics.components.Heading;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.client.physics.components.Position;
import com.ylinor.library.api.ecs.systems.Timer;


/**
 * Makes the {@link CameraSystem#camera} field follows the position and heading
 * of the entity (or the last processed entity) holding the
 * {@link RenderViewEntity} component.
 * 
 * The camera field is accessible with {@link CameraSystem#getCamera()}.
 * 
 * @author wytrem
 */
public class CameraSystem extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger(CameraSystem.class);

    @Wire
    private ComponentMapper<Position> positionMapper;

    @Wire
    private ComponentMapper<Heading> headingMapper;

    @Wire
    private ComponentMapper<EyeHeight> eyeHeightMapper;

    @Wire
    private YlinorClient client;
    
    @Wire
    private ComponentMapper<Physics> physicsMapper;

    private Camera camera;
    
    @Wire
    private Timer timer;

    public CameraSystem() {
        super(Aspect.all(RenderViewEntity.class, Position.class, Heading.class, Physics.class));
    }

    @Override
    protected void initialize() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.lookAt(0, 0, 0);
        camera.near = 0.1f;
        camera.far = 100.0f;
        camera.update();
    }

    public Camera getCamera() {
        return camera;
    }

    private int processedEntititesThisTick;

    @Override
    protected void begin() {
        processedEntititesThisTick = 0;
    }

    @Override
    protected void process(int entityId) {
        Position position = positionMapper.get(entityId);
        Heading heading = headingMapper.get(entityId);
        
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();

        float d0 = position.prevPosition.x + (position.position.x - position.prevPosition.x) * timer.renderPartialTicks;
        float d1 = position.prevPosition.y + (position.position.y - position.prevPosition.y) * timer.renderPartialTicks;
        float d2 = position.prevPosition.z + (position.position.z - position.prevPosition.z) * timer.renderPartialTicks;
        camera.position.set(d0, d1, d2);

        if (eyeHeightMapper.has(entityId)) {
            Vector3f eyePadding = eyeHeightMapper.get(entityId).eyePadding;
            camera.position.add(eyePadding.x, eyePadding.y, eyePadding.z);
        }

        camera.direction.set(heading.heading.x, heading.heading.y, heading.heading.z);
        camera.update(true);

        processedEntititesThisTick++;
    }

    @Override
    protected void end() {
        if (processedEntititesThisTick != 1) {
            logger.warn("{} processed this tick, this might cause bugs.", processedEntititesThisTick);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
