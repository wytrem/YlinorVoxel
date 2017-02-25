package com.ylinor.client.render;

import com.artemis.World;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.client.render.model.ModelRegistry;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;


public class RenderGlobal implements Disposable {
    int renderEngineVersion = 1;

    ModelBatch modelBatch;
    Environment environment;
    TerrainRenderer terrainRenderer;
    
    SpriteBatch spriteBatch;
    BitmapFont font;
    ModelRegistry blockModels;

    @Wire
    CameraSystem cameraSystem;

    public RenderGlobal(Terrain world) {
        DefaultShader.Config shaderConfig = new Config();

        shaderConfig.defaultCullFace = GL20.GL_FRONT;

        modelBatch = new ModelBatch(new DefaultShaderProvider(shaderConfig));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        terrainRenderer = new TerrainRenderer(world, this);

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
    }
    
    public void inject(World world) {
        world.inject(terrainRenderer);
    }

    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.9f, 1.0f);
        update();

        modelBatch.begin(cameraSystem.getCamera());

        // Render terrain
        modelBatch.render(terrainRenderer, environment);

        // Render entities
        modelBatch.end();
    }

    private void update() {
        terrainRenderer.update();
    }
    
    public ChunkRenderer getChunkRenderer(Chunk chunk) {
        return terrainRenderer.chunkRenderers.get(chunk.id);
    }

    @Override
    public void dispose() {
        terrainRenderer.dispose();
    }
}
