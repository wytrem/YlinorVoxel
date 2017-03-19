package com.ylinor.client.render;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.ylinor.client.events.GdxPauseEvent;
import com.ylinor.client.events.GdxResizeEvent;
import com.ylinor.client.events.GdxResumeEvent;
import com.ylinor.client.input.GdxInputDispatcherSystem;

import net.mostlyoriginal.api.event.common.Subscribe;


public class ScreenSystem extends BaseSystem {

    private static final Logger logger = LoggerFactory.getLogger(ScreenSystem.class);

    /**
     * Current screen.
     */
    private Screen screen;

    @Wire
    private GdxInputDispatcherSystem inputDispatcherSystem;

    @Override
    protected void processSystem() {
        // Screen updating
        if (screen != null) {
            screen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Subscribe
    public void resize(GdxResizeEvent event) {
        if (screen != null) {
            screen.resize(event.width, event.height);
        }
        logger.info("Window resized : " + event.width + "x" + event.height);
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.hide();
        }

        this.screen = screen;

        if (this.screen != null) {
            world.inject(screen);
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        else {
            Gdx.input.setCursorCatched(true);
            Gdx.input.setInputProcessor(inputDispatcherSystem);
        }

        logger.debug("Setting screen : " + (screen == null ? "null" : screen.getClass()));
    }

    public void dispose() {
        if (screen != null) {
            screen.hide();
            screen.dispose();
        }
    }

    @Subscribe
    public void pause(GdxPauseEvent event) {
        if (screen != null) {
            screen.pause();
        }
    }

    @Subscribe
    public void resume(GdxResumeEvent event) {
        if (screen != null) {
            screen.resume();
        }
    }

    /**
     * @return the currently active {@link Screen}.
     */
    public Screen getScreen() {
        return screen;
    }
}
