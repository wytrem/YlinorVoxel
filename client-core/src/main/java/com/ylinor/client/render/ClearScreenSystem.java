package com.ylinor.client.render;

import javax.inject.Singleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ylinor.library.util.ecs.system.BaseSystem;

@Singleton
public class ClearScreenSystem extends BaseSystem {
    @Override
    protected void processSystem() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.9f, 1.0f);
    }
}
