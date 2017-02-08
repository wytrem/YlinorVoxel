package com.ylinor.client.physics;

import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;


/**
 * Listens to the input and applies it to all the {@link InputControlledEntity}.
 * This affects {@link Motion} and {@link Heading}.
 *
 * @author wytrem
 */
public class PlayerInputSystem extends IteratingSystem
                implements InputProcessor {

    private ComponentMapper<Motion> motionMapper;
    private ComponentMapper<Velocity> velocityMapper;
    private ComponentMapper<Heading> headingMapper;

    private final IntIntMap keys = new IntIntMap();
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

    public PlayerInputSystem() {
        super(Aspect.all(InputControlledEntity.class, Motion.class, Heading.class, Velocity.class));
    }

    @Override
    protected void initialize() {
        Gdx.input.setInputProcessor(this);
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

        if (keys.containsKey(FORWARD)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .normalize()
               .mul(velocity.speed);
            motion.motion.add(tmp);
        }
        if (keys.containsKey(BACKWARD)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .normalize()
               .mul(-velocity.speed);
            motion.motion.add(tmp);
        }
        if (keys.containsKey(STRAFE_LEFT)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .cross(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(-velocity.speed);
            motion.motion.add(tmp);
        }
        if (keys.containsKey(STRAFE_RIGHT)) {
            tmp.set(camera.direction.x, 0, camera.direction.z)
               .cross(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(velocity.speed);
            motion.motion.add(tmp);
        }
        if (keys.containsKey(UP)) {
            tmp.set(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(velocity.speed);
            motion.motion.add(tmp);
        }
        if (keys.containsKey(DOWN)) {
            tmp.set(camera.up.x, camera.up.y, camera.up.z)
               .normalize()
               .mul(-velocity.speed);
            motion.motion.add(tmp);
        }

        heading.heading.set(camera.direction.x, camera.direction.y, camera.direction.z);
        camera.update(true);
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.remove(keycode, 0);

        if (keycode == Keys.ESCAPE) {
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!Gdx.input.isCursorCatched()) {
            return false;
        }

        int magX = Math.abs(mouseX - screenX);
        int magY = Math.abs(mouseY - screenY);

        if (mouseX > screenX) {
            camera.rotate(Vector3.Y, 1 * magX * rotSpeed);
            camera.update();
        }

        if (mouseX < screenX) {
            camera.rotate(Vector3.Y, -1 * magX * rotSpeed);
            camera.update();
        }

        if (mouseY < screenY) {
            if (camera.direction.y > -0.965)
                camera.rotate(camera.direction.cpy()
                                              .crs(Vector3.Y), -1 * magY * rotSpeed);
            camera.update();
        }

        if (mouseY > screenY) {
            if (camera.direction.y < 0.965)
                camera.rotate(camera.direction.cpy()
                                              .crs(Vector3.Y), 1 * magY * rotSpeed);
            camera.update();
        }

        mouseX = screenX;
        mouseY = screenY;

        return false;
    }

    public boolean keyTyped(char character) {
        return false;
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
