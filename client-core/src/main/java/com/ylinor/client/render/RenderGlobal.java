package com.ylinor.client.render;

import com.artemis.World;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.client.render.model.ModelRegistry;
import com.ylinor.client.resource.Assets;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;


public class RenderGlobal implements Disposable {
    int renderEngineVersion = 1;

    ModelBatch terrainBatch;
    ModelBatch entitiesBatch;
    Environment environment;
    TerrainRenderer terrainRenderer;
    
    SpriteBatch spriteBatch;
    BitmapFont font;
    ModelRegistry blockModels;

    @Wire
    CameraSystem cameraSystem;
    
    @Wire
    Assets assets;
    
    ModelInstance test;
    
    @Wire
    Terrain world;
    AnimationController controller;
    
    DirectionalLight sun;
    

    public RenderGlobal() {

    }
    
    public void init() {
        DefaultShader.Config shaderConfig = new Config();

        shaderConfig.defaultCullFace = GL20.GL_FRONT;

        terrainBatch = new ModelBatch(new DefaultShaderProvider(shaderConfig));
        entitiesBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 1f));
        
        
        sun = new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f);
        environment.add(sun);
        terrainRenderer = new TerrainRenderer(world, this);

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        
        Model player = assets.modelAssets.getModelTest();
        test = new ModelInstance(player); 
        test.transform.setToTranslation(0, 255, 0).scl(0.3f);
        
        controller = new AnimationController(test);
        controller.setAnimation("run", -1);
    }
    
    public void inject(World world) {
        world.inject(terrainRenderer);
    }

    public void render() {
        update();

        terrainBatch.begin(cameraSystem.getCamera());

        // Render terrain
        terrainBatch.render(terrainRenderer, environment);
        
        terrainBatch.end();
        
        entitiesBatch.begin(cameraSystem.getCamera());

        // DEBUG
        entitiesBatch.render(test, environment);
        entitiesBatch.end();
    }
    
    private void update() {
        terrainRenderer.update();
        controller.update(Gdx.graphics.getDeltaTime());
    }
    
    public ChunkRenderer getChunkRenderer(Chunk chunk) {
        return terrainRenderer.chunkRenderers.get(chunk.id);
    }

    @Override
    public void dispose() {
        terrainRenderer.dispose();
    }
}
