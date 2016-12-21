package com.ylinor.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntIntMap;


/**
 * Takes a {@link Camera} instance and controls it via w,a,s,d and mouse
 * panning.
 * 
 * @author badlogic
 */
public class FirstPersonCameraController extends InputAdapter
{
    private final Camera cam;
    private final IntIntMap keys = new IntIntMap();
    private int STRAFE_LEFT = Keys.Q;
    private int STRAFE_RIGHT = Keys.D;
    private int FORWARD = Keys.Z;
    private int BACKWARD = Keys.S;
    private int UP = Keys.SPACE;
    private int DOWN = Keys.SHIFT_LEFT;
    private float velocity = 5;
    private float degreesPerPixel = 0.5f;
    private final Vector3 tmp = new Vector3();

    public FirstPersonCameraController(Camera camera)
    {
        this.cam = camera;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        keys.put(keycode, keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        keys.remove(keycode, 0);

        if (keycode == Keys.ESCAPE)
        {
            Gdx.input.setCursorCatched(!Gdx.input.isCursorCatched());
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        }

        return true;
    }

    /**
     * Sets the velocity in units per second for moving forward, backward and
     * strafing left/right.
     * 
     * @param velocity the velocity in units per second
     */
    public void setVelocity(float velocity)
    {
        this.velocity = velocity;
    }

    /**
     * Sets how many degrees to rotate per pixel the mouse moved.
     * 
     * @param degreesPerPixel
     */
    public void setDegreesPerPixel(float degreesPerPixel)
    {
        this.degreesPerPixel = degreesPerPixel;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        float deltaX = -Gdx.input.getDeltaX() * degreesPerPixel;
        float deltaY = -Gdx.input.getDeltaY() * degreesPerPixel;
        cam.direction.rotate(cam.up, deltaX);
        tmp.set(cam.direction).crs(cam.up).nor();
        cam.direction.rotate(tmp, deltaY);
        // camera.up.rotate(tmp, deltaY);
        return true;
    }

    public void update()
    {
        update(Gdx.graphics.getDeltaTime());
    }

    public void update(float deltaTime)
    {
        if (keys.containsKey(FORWARD))
        {
            tmp.set(cam.direction).nor().scl(deltaTime * velocity);
            cam.position.add(tmp);
        }
        if (keys.containsKey(BACKWARD))
        {
            tmp.set(cam.direction).nor().scl(-deltaTime * velocity);
            cam.position.add(tmp);
        }
        if (keys.containsKey(STRAFE_LEFT))
        {
            tmp.set(cam.direction).crs(cam.up).nor().scl(-deltaTime * velocity);
            cam.position.add(tmp);
        }
        if (keys.containsKey(STRAFE_RIGHT))
        {
            tmp.set(cam.direction).crs(cam.up).nor().scl(deltaTime * velocity);
            cam.position.add(tmp);
        }
        if (keys.containsKey(UP))
        {
            tmp.set(cam.up).nor().scl(deltaTime * velocity);
            cam.position.add(tmp);
        }
        if (keys.containsKey(DOWN))
        {
            tmp.set(cam.up).nor().scl(-deltaTime * velocity);
            cam.position.add(tmp);
        }
        cam.update(true);
    }

    private int mouseX = 0;
    private int mouseY = 0;
    private float rotSpeed = 0.2f;

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        if (!Gdx.input.isCursorCatched())
        {
            return false;
        }

        int magX = Math.abs(mouseX - screenX);
        int magY = Math.abs(mouseY - screenY);

        if (mouseX > screenX)
        {
            cam.rotate(Vector3.Y, 1 * magX * rotSpeed);
            cam.update();
        }

        if (mouseX < screenX)
        {
            cam.rotate(Vector3.Y, -1 * magX * rotSpeed);
            cam.update();
        }

        if (mouseY < screenY)
        {
            if (cam.direction.y > -0.965)
                cam.rotate(cam.direction.cpy().crs(Vector3.Y), -1 * magY * rotSpeed);
            cam.update();
        }

        if (mouseY > screenY)
        {

            if (cam.direction.y < 0.965)
                cam.rotate(cam.direction.cpy().crs(Vector3.Y), 1 * magY * rotSpeed);
            cam.update();
        }

        mouseX = screenX;
        mouseY = screenY;

        return false;
    }
}
