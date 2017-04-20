package com.ylinor.client.input;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.ylinor.client.events.input.keyboard.KeyDownEvent;
import com.ylinor.client.events.input.keyboard.KeyTypedEvent;
import com.ylinor.client.events.input.keyboard.KeyUpEvent;
import com.ylinor.client.events.input.mouse.MouseDraggedEvent;
import com.ylinor.client.events.input.mouse.MouseMovedEvent;
import com.ylinor.client.events.input.mouse.MouseScrolledEvent;
import com.ylinor.client.events.input.mouse.MouseTouchDownEvent;
import com.ylinor.client.events.input.mouse.MouseTouchUpEvent;
import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.library.util.ecs.system.EventSystem;
@Singleton
public class GdxInputDispatcherSystem extends BaseSystem implements InputProcessor{

    private List<Integer> pressedKeys = new ArrayList<>();
    
    @Inject
    private EventSystem eventSystem;
    
    @Override
    public  void initialize() {
        Gdx.input.setInputProcessor(this);
    }
    
    @Override
    protected void processSystem() {
        // Nothing.
    }
    
    @Override
    protected boolean checkProcessing() {
        return false;
    }
    
    public List<Integer> getPressedKeys() {
        return pressedKeys;
    }
    
    @Override
    public boolean keyDown(int keycode) {
        pressedKeys.add(Integer.valueOf(keycode));
        eventSystem.dispatch(new KeyDownEvent(keycode));
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        pressedKeys.remove(Integer.valueOf(keycode));
        eventSystem.dispatch(new KeyUpEvent(keycode));
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        eventSystem.dispatch(new KeyTypedEvent(character));
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        eventSystem.dispatch(new MouseTouchDownEvent(screenX, screenY, pointer, button));
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        eventSystem.dispatch(new MouseTouchUpEvent(screenX, screenY, pointer, button));
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        eventSystem.dispatch(new MouseDraggedEvent(screenX, screenY, pointer));
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        eventSystem.dispatch(new MouseMovedEvent(screenX, screenY));
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        eventSystem.dispatch(new MouseScrolledEvent(amount));
        return true;
    }
}
