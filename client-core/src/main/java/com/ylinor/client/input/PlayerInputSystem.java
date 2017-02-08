package com.ylinor.client.input;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.ylinor.client.events.input.keyboard.KeyUpEvent;
import com.ylinor.client.events.input.mouse.MouseMovedEvent;
import com.ylinor.client.physics.Heading;
import com.ylinor.client.physics.InputControlledEntity;
import com.ylinor.client.physics.Motion;
import com.ylinor.client.physics.Velocity;

import net.mostlyoriginal.api.event.common.Subscribe;


/**
 * Listens to the input and applies it to all the {@link InputControlledEntity}.
 * This affects {@link Motion} and {@link Heading}.
 *
 * @author wytrem
 */
public class PlayerInputSystem extends IteratingSystem {

    private ComponentMapper<Motion> motionMapper;
    private ComponentMapper<Velocity> velocityMapper;
    private ComponentMapper<Heading> headingMapper;

    private int STRAFE_LEFT = Keys.A;
    private int STRAFE_RIGHT = Keys.D;
    private int FORWARD = Keys.W;
    private int BACKWARD = Keys.S;
    private int UP = Keys.SPACE;
    private int DOWN = Keys.SHIFT_LEFT;
    private final Vector3f tmp = new Vector3f();
    private int mouseX = 0;
    private int mouseY = 0;
    private float rotSpeed = 0.2f;

    /**
     * This should stay PRIVATE with NO getter.
     */
    private Camera camera;
    
    @Wire
    private GdxInputDispatcherSystem inputDispatcherSystem;

    public PlayerInputSystem() {
        super(Aspect.all(InputControlledEntity.class, Motion.class, Heading.class, Velocity.class));
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
    protected void process(int entityId) {
        Motion motion = motionMapper.get(entityId);
        Velocity velocity = velocityMapper.get(entityId);
        Heading heading = headingMapper.get(entityId);

        if (inputDispatcherSystem.getPressedKeys().contains(FORWARD)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .normalize()
               .mul(velocity.speed);
            motion.motion.add(tmp);
        }
        if (inputDispatcherSystem.getPressedKeys().contains(BACKWARD)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .normalize()
               .mul(-velocity.speed);
            motion.motion.add(tmp);
        }
        if (inputDispatcherSystem.getPressedKeys().contains(STRAFE_LEFT)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .cross(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(-velocity.speed);
            motion.motion.add(tmp);
        }
        if (inputDispatcherSystem.getPressedKeys().contains(STRAFE_RIGHT)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .cross(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(velocity.speed);
            motion.motion.add(tmp);
        }
        if (inputDispatcherSystem.getPressedKeys().contains(UP)) {
            tmp.set(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(velocity.speed);
            motion.motion.add(tmp);
        }
        if (inputDispatcherSystem.getPressedKeys().contains(DOWN)) {
            tmp.set(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(-velocity.speed);
            motion.motion.add(tmp);
        }

        heading.heading.set(camera.direction.x, camera.direction.y, camera.direction.z);
        camera.update(true);
    }

    @Subscribe
    public void keyUp(KeyUpEvent event) {
        System.out.println("keyup in playerinput with " + event.keycode);
        
        if (event.keycode == Keys.ESCAPE) {
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
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
    }
}
