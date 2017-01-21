package com.ylinor.client.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.client.render.camera.FirstPersonCameraController;

import gnu.trove.map.TLongObjectMap;
import gnu.trove.map.hash.TLongObjectHashMap;


public class RenderGlobal implements Disposable {
    int renderEngineVersion = 1;

    ModelBatch modelBatch;
    Camera camera;
    FirstPersonCameraController cameraController;
    Frustum cameraFrustum;

    public RenderGlobal() {
        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0, 0, 0);
        camera.near = 0.1f;
        camera.far = 45f;
        camera.update();
        cameraController = new FirstPersonCameraController(camera);
    }

    public void render() {
        update();

        modelBatch.begin(camera);

        // Render terrain

        // Render entities

        modelBatch.end();
    }

    private void onChunkChanged() {
        
    }

    private void update() {
        cameraController.update();
        camera.update();
        cameraFrustum.update(camera.invProjectionView);
    }

    public InputAdapter getCameraController() {
        return cameraController;
    }

    @Override
    public void dispose() {

    }
}
