package com.ylinor.client.input;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.google.common.eventbus.Subscribe;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.events.input.keyboard.KeyUpEvent;
import com.ylinor.client.events.input.mouse.MouseMovedEvent;
import com.ylinor.client.physics.components.Heading;
import com.ylinor.client.physics.components.InputControlledEntity;
import com.ylinor.client.physics.components.Motion;
import com.ylinor.client.physics.components.Physics;
import com.ylinor.library.util.ecs.Aspect;
import com.ylinor.library.util.ecs.Entity;
import com.ylinor.library.util.ecs.IteratingSystem;


/**
 * Listens to the input and applies it to all the {@link InputControlledEntity}.
 * This affects {@link Motion} and {@link Heading}.
 *
 * @author wytrem
 */
@Singleton
public class PlayerInputSystem extends IteratingSystem {

    private int STRAFE_LEFT = Keys.S;
    private int STRAFE_RIGHT = Keys.F;
    private int FORWARD = Keys.E;
    private int BACKWARD = Keys.D;
    private int UP = Keys.SPACE;
    //    private int DOWN = Keys.SHIFT_LEFT;
    private int mouseX = 0;
    private int mouseY = 0;
    private float rotSpeed = 0.2f;

    @Inject
    private YlinorClient client;

    /**
     * This should stay PRIVATE with NO getter.
     */
    private Camera camera;

    @Inject
    private GdxInputDispatcherSystem inputDispatcherSystem;

    public PlayerInputSystem() {
        super(Aspect.all(InputControlledEntity.class, Heading.class));
    }

    @Override
    protected void initialize() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.lookAt(0, 0, 0);
        camera.near = 0.1f;
        camera.far = 45f;
        camera.update();
    }

    @Override
    protected void process(Entity entity) {
        //        Motion motion = motionMapper.get(entityId);
        Heading heading = entity.get(Heading.class);
        Physics physics = entity.get(Physics.class);

        physics.moveStrafing = 0.0F;
        physics.moveForward = 0.0F;

        float mvt = 1;

        if (inputDispatcherSystem.getPressedKeys().contains(FORWARD)) {

            physics.moveForward = mvt;
        }
        if (inputDispatcherSystem.getPressedKeys().contains(BACKWARD)) {

            physics.moveForward = -mvt;
        }
        if (inputDispatcherSystem.getPressedKeys().contains(STRAFE_LEFT)) {

            physics.moveStrafing = mvt;
        }
        if (inputDispatcherSystem.getPressedKeys().contains(STRAFE_RIGHT)) {

            physics.moveStrafing = -mvt;
        }
        physics.isJumping = inputDispatcherSystem.getPressedKeys().contains(UP);

        heading.heading.set(camera.direction.x, camera.direction.y, camera.direction.z);
        camera.update(true);
    }

    @Subscribe
    public void keyUp(KeyUpEvent event) {
        if (event.keycode == Keys.ESCAPE) {
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            event.setCancelled(true);
        }
    }

    @Subscribe
    public void mouseMoved(MouseMovedEvent event) {
        if (!Gdx.input.isCursorCatched()) {
            return;
        }

        int magX = Math.abs(mouseX - event.mouseX);
        int magY = Math.abs(mouseY - event.mouseY);

        if (mouseX > event.mouseX) {
            camera.rotate(Vector3.Y, 1 * magX * rotSpeed);
            camera.update();
        }

        if (mouseX < event.mouseX) {
            camera.rotate(Vector3.Y, -1 * magX * rotSpeed);
            camera.update();
        }

        if (mouseY < event.mouseY) {
            if (camera.direction.y > -0.965)
                camera.rotate(camera.direction.cpy()
                                              .crs(Vector3.Y), -1 * magY * rotSpeed);
            camera.update();
        }

        if (mouseY > event.mouseY) {
            if (camera.direction.y < 0.965)
                camera.rotate(camera.direction.cpy()
                                              .crs(Vector3.Y), 1 * magY * rotSpeed);
            camera.update();
        }

        mouseX = event.mouseX;
        mouseY = event.mouseY;
        event.setCancelled(true);
    }

    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
