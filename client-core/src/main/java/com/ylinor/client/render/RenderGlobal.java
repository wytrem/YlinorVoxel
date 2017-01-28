package com.ylinor.client.render;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.utils.Disposable;
import com.ylinor.client.render.camera.FirstPersonCameraController;
import com.ylinor.library.api.world.World;


public class RenderGlobal implements Disposable {
    int renderEngineVersion = 1;

    ModelBatch modelBatch;
    PerspectiveCamera camera;
    FirstPersonCameraController cameraController;
    Frustum cameraFrustum;
    Environment environment;
    TerrainRenderer terrainRenderer;
    Skybox skybox;
    
    SpriteBatch spriteBatch;
    BitmapFont font;

    public RenderGlobal(World world) {
        DefaultShader.Config shaderConfig = new Config();

        shaderConfig.defaultCullFace = GL20.GL_FRONT;

        modelBatch = new ModelBatch(new DefaultShaderProvider(shaderConfig));
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.lookAt(0, 0, 0);
        camera.near = 0.1f;
        camera.far = 45f;
        camera.update();
        cameraController = new FirstPersonCameraController(camera);

        camera.position.set(0, 258, 0);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.set(new ColorAttribute(ColorAttribute.Fog, 0.13f, 0.13f, 0.13f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        terrainRenderer = new TerrainRenderer(world, this);

        cameraFrustum = new Frustum();
        
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();

        Cubemap skyboxCubemap = new Cubemap(
                Gdx.files.getFileHandle("skybox/right.jpg", Files.FileType.Internal),
                Gdx.files.getFileHandle("skybox/left.jpg", Files.FileType.Internal),
                Gdx.files.getFileHandle("skybox/top.jpg", Files.FileType.Internal),
                Gdx.files.getFileHandle("skybox/bottom.jpg", Files.FileType.Internal),
                Gdx.files.getFileHandle("skybox/back.jpg", Files.FileType.Internal),
                Gdx.files.getFileHandle("skybox/front.jpg", Files.FileType.Internal));
        skybox = new Skybox(skyboxCubemap);
    }

    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.9f, 1.0f);
        update();

        modelBatch.begin(camera);
        
        // Render terrain
        modelBatch.render(terrainRenderer, environment);

        // Render entities
        modelBatch.end();

        skybox.render(camera);

        spriteBatch.begin();
        font.draw(spriteBatch, "fps : " + Gdx.graphics.getFramesPerSecond(), 0, 60);
        font.draw(spriteBatch, "pos : " + camera.position.toString(), 0, 40);
        spriteBatch.end();
    }

    private void onChunkChanged() {

    }

    private void update() {
        cameraController.update();
        cameraFrustum.update(camera.invProjectionView);
        terrainRenderer.update();
    }

    public InputAdapter getCameraController() {
        return cameraController;
    }

    @Override
    public void dispose() {
        skybox.dispose();
    }
}
