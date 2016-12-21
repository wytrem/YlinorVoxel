package com.ylinor.client.render;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Disposable;


public interface Renderer<T> extends RenderableProvider, Disposable
{
    T getObject();
}
