package com.ylinor.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Un Screen Ylinor.
 *
 * = {@link Stage} + {@link Screen} qui se gèrent les uns les autres (update du
 * screen, définition de l'input processor)
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class YlinorScreen extends Stage implements Screen {

    /*
     * Constructeur auto, redirige vers l'autre constructeur en générant un
     * ScreenViewport
     */
    public YlinorScreen() {
        this(new ScreenViewport());
    }

    /*
     * Constructeur appelant simplement le constructeur super de Stage demandant
     * aussi un viewport
     */
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
