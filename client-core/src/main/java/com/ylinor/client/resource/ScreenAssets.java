package com.ylinor.client.resource;

import static com.ylinor.client.resource.Assets.FONTS_FOLDER;
import static com.ylinor.client.resource.Assets.IMAGES_FOLDER;
import static com.ylinor.client.resource.Assets.UI_IMAGES_FOLDER;
import static com.ylinor.client.resource.Assets.fromPixmap;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;


/**
 * Contains all screens's assets
 *
 * @author Litarvan
 * @since 1.0.0
 */
public class ScreenAssets implements Loadable, Preloadable {
    private final Assets assets;

    ScreenAssets(Assets assets) {
        this.assets = assets;
    }

    @Preloaded
    public final String splash = IMAGES_FOLDER + "splash.jpg";

    public final String logo = IMAGES_FOLDER + "logo.png";
    public final String augustus = FONTS_FOLDER + "AUGUSTUS.TTF";
    public final String liberation = FONTS_FOLDER + "LiberationSerif-Regular.ttf";
    public final String mainMenuBackground = UI_IMAGES_FOLDER + "mainmenu/background.jpg";
    public final String mainMenuLogo = UI_IMAGES_FOLDER + "mainmenu/logo.png";

    public final String bigTextButtonNormal = UI_IMAGES_FOLDER + "bigbutton/normal.9.png";

    public void preload() {
        assets.loadTexture(splash);
    }

    public void load() {
        //assets.loadTexture(logo);
        assets.loadFont(augustus);
        assets.loadFont(liberation);
        assets.loadTexture(mainMenuBackground);
        assets.loadPixmap(bigTextButtonNormal);
        assets.loadTexture(mainMenuLogo);
    }

    @Preloaded
    public Texture splash() {
        return assets.get(splash);
    }

    public Texture logo() {
        return assets.get(logo);
    }

    public Texture mainMenuLogo() {
        return assets.get(mainMenuLogo);
    }

    public NinePatch bigTextButtonNormal() {
        return fromPixmap((Pixmap) assets.get(bigTextButtonNormal));
    }

    public Texture mainMenuBackground() {
        return assets.get(mainMenuBackground);
    }

    public FreeTypeFontGenerator augustus() {
        return assets.get(augustus);
    }

    public FreeTypeFontGenerator liberation() {
        return assets.get(liberation);
    }
}
