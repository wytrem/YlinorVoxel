package com.ylinor.client.render;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ylinor.client.YlinorClient;
import com.ylinor.library.util.ecs.Aspect;
import com.ylinor.library.util.ecs.Entity;
import com.ylinor.library.util.ecs.IteratingSystem;

@Singleton
public class HudRenderSystem extends IteratingSystem {
    private SpriteBatch spriteBatch;
    private BitmapFont font;

    @Inject
    private CameraSystem cameraSystem;

    @Inject
    private YlinorClient client;

    public HudRenderSystem() {
        super(Aspect.all(RenderViewEntity.class));
    }

    @Override
    protected void initialize() {
        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
    }

    @Override
    protected void begin() {
        spriteBatch.begin();
    }

    @Override
    protected void process(Entity entityId) {
        font.draw(spriteBatch, "dir : " + cameraSystem.getCamera().direction.toString(), 0, 80);
        font.draw(spriteBatch, "pos : " + cameraSystem.getCamera().position.toString(), 0, 60);
        font.draw(spriteBatch, "fps : " + Gdx.graphics.getFramesPerSecond(), 0, 40);
    }

    @Override
    protected void end() {
        spriteBatch.end();
    }
    
    @Override
    protected boolean checkProcessing() {
        return client.isInGame;
    }
}
