package com.ylinor.client.render;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.Camera;

/**
 *
 */
public class RenderGlobal {

    ModelBatch modelBatch;
    Camera playerCamera;
    TerrainRenderer terrainRenderer;
    Environment environment;

    public void init() {
        modelBatch = new ModelBatch();
    }

    public void render() {
        modelBatch.begin(playerCamera);
        modelBatch.render(terrainRenderer, environment);
        modelBatch.end();
    }
}
