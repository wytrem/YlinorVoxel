package com.ylinor.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * An Ylinor screen.
 *
 * = {@link Stage} + {@link Screen} managing each others
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class YlinorScreen extends Stage implements Screen {

    /**
     * Automatic constructor, redirect to the other constructor by generating a ScreenViewport
     **/
    public YlinorScreen() {
        this(new ScreenViewport());
    }

    /**
     * Same as the last constructor but with a {@link Viewport} as param
     **/
    public YlinorScreen(Viewport viewport) {
        super(viewport);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        act(delta);
        draw();
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
