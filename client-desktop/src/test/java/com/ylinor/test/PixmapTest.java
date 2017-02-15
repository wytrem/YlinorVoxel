/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ylinor.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ylinor.client.render.model.Icon;
import com.ylinor.client.resource.TextureAtlas;

import java.io.File;

public class PixmapTest extends GdxTest {
    Texture texture;
    SpriteBatch batch;
    TextureRegion region;
    TextureAtlas atlas;
    TextureRegion region2;

    public void create() {
        atlas = new TextureAtlas();
        atlas.loadFrom(new File("/home/victor/Ylinor/atlas"), false, 512);

        // Create a texture to contain the pixmap
        texture = new Texture(atlas.getSheet()); // Pixmap.Format.RGBA8888);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        region = new TextureRegion(texture, 0, 0, atlas.getSheet().getWidth(), atlas.getSheet().getHeight());
        batch = new SpriteBatch();


        System.out.println(atlas.getUVFor("slime"));
        region2 = fromIcon(atlas.getUVFor("slime"));
    }

    private TextureRegion fromIcon(Icon icon) {
        return new TextureRegion(texture, icon.getMinU(), icon.getMinV(), icon.getMaxU(), icon.getMaxV());
    }

    public void render() {
        Gdx.gl.glClearColor(0.6f, 0.6f, 0.6f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        batch.draw(region2, 0, 0, 32, 32);
        batch.draw(region, 0, 32);
        batch.end();
    }
}