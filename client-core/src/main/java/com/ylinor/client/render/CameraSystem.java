package com.ylinor.client.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.ylinor.client.YlinorClient;
import com.ylinor.library.api.ecs.components.Heading;
import com.ylinor.library.api.ecs.components.Physics;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.IteratingSystem;


/**
 * Makes the {@link CameraSystem#camera} field follows the position and heading
 * of the entity (or the last processed entity) holding the
 * {@link RenderViewEntity} component.
 * 
 * The camera field is accessible with {@link CameraSystem#getCamera()}.
 * 
 * @author wytrem
 */
@Singleton
public class CameraSystem extends IteratingSystem {

    private static final Logger logger = LoggerFactory.getLogger(CameraSystem.class);

    @Inject
    private YlinorClient client;

    private Camera camera;

    @Inject
    private Timer timer;

    public CameraSystem() {
        super(Aspect.all(RenderViewEntity.class, Position.class, Heading.class, Physics.class));
    }

    @Override
    public void initialize() {
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
    protected void process(Entity entity) {
        Position position = entity.get(Position.class);
        Heading heading = entity.get(Heading.class);

        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();

        float d0 = position.prevPosition.x + (position.position.x - position.prevPosition.x) * timer.renderPartialTicks;
        float d1 = position.prevPosition.y + (position.position.y - position.prevPosition.y) * timer.renderPartialTicks;
        float d2 = position.prevPosition.z + (position.position.z - position.prevPosition.z) * timer.renderPartialTicks;
        camera.position.set(d0, d1, d2);

        if (entity.has(EyeHeight.class)) {
            Vector3f eyePadding = entity.get(EyeHeight.class).eyePadding;
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
