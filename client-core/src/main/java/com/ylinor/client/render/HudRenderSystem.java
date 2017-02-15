package com.ylinor.client.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.physics.Physics;


public class HudRenderSystem extends IteratingSystem {
    private SpriteBatch spriteBatch;
    private BitmapFont font;

    @Wire
    private CameraSystem cameraSystem;

    @Wire
    private ComponentMapper<Physics> onGroundMapper;
    
    @Wire
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
    protected void process(int entityId) {
        font.draw(spriteBatch, "onground : " + onGroundMapper.get(entityId).onGround, 0, 100);
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
