package com.ylinor.client.render;

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
import com.ylinor.client.network.ClientNetworkSystem;
import com.ylinor.client.physics.components.Position;
import com.ylinor.client.render.model.ModelRegistry;
import com.ylinor.client.resource.Assets;
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.ecs.entity.Entity;
import org.joml.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RenderGlobal implements Disposable {
    int renderEngineVersion = 1;

    ModelBatch terrainBatch;
    ModelBatch entitiesBatch;
    Environment environment;
    TerrainRenderer terrainRenderer;
    
    SpriteBatch spriteBatch;
    BitmapFont font;
    ModelRegistry blockModels;

    @Inject
    CameraSystem cameraSystem;
    
    @Inject
    Assets assets;

    @Inject
    ClientNetworkSystem clientNetworkSystem;

    @Inject
    Terrain terrain;
    
    AnimationController controller;
    
    DirectionalLight sun;

    private ModelInstance placeholderEntityModelInstance;

    public RenderGlobal() {

    }
    
    public void init(World world) {
        DefaultShader.Config shaderConfig = new Config();

        shaderConfig.defaultCullFace = GL20.GL_FRONT;

        terrainBatch = new ModelBatch(new DefaultShaderProvider(shaderConfig));
        entitiesBatch = new ModelBatch();

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 1f));

        sun = new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f);
        environment.add(sun);
        terrainRenderer = new TerrainRenderer(terrain, this);
        world.injector.injectMembers(terrainRenderer);
        terrainRenderer.init();

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
        
        Model player = assets.modelAssets.getModelTest();
        this.placeholderEntityModelInstance = new ModelInstance(player);
        placeholderEntityModelInstance.transform.scl(0.3f);

        controller = new AnimationController(placeholderEntityModelInstance);
        controller.setAnimation("run", -1);
    }
    
    public void render() {
        update();

        terrainBatch.begin(cameraSystem.getCamera());

        // Render terrain
        terrainBatch.render(terrainRenderer, environment);
        terrainBatch.end();

        entitiesBatch.begin(cameraSystem.getCamera());

        for (Entity entity : clientNetworkSystem.getNearbyEntities()) {
            Vector3f position = entity.get(Position.class).position;

            placeholderEntityModelInstance.transform.setTranslation(position.x, position.y, position.z);
            entitiesBatch.render(placeholderEntityModelInstance, environment);
        }

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

        terrainBatch.dispose();
        entitiesBatch.dispose();
    }
}
