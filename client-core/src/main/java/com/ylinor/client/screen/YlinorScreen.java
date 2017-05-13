package com.ylinor.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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
     * Automatic constructor, redirect to the other constructor by generating a
     * ScreenViewport
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
        Gdx.input.setCursorCatched(false);

        Gdx.input.setInputProcessor(new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return YlinorScreen.this.keyDown(keycode);
            }

            @Override
            public boolean keyUp(int keycode) {
                return YlinorScreen.this.keyUp(keycode);
            }

            @Override
            public boolean keyTyped(char character) {
                return YlinorScreen.this.keyTyped(character);
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return YlinorScreen.this.touchDown(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return YlinorScreen.this.touchUp(screenX, screenY, pointer, button);
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return YlinorScreen.this.touchDragged(screenX, screenY, pointer);
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return YlinorScreen.this.mouseMoved(screenX, screenY);
            }

            @Override
            public boolean scrolled(int amount) {
                return YlinorScreen.this.scrolled(amount);
            }
        });
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
