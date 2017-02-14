package com.ylinor.client.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.ylinor.client.render.model.Icon;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TextureAtlas
{

    private Map<String, Icon> iconMap;
    private Pixmap atlas;



    public void loadFrom(File folder, boolean recursive) {
        iconMap = new HashMap<>();

        Map<String, Pixmap> pixmaps = new HashMap<>(64);
        readFolder(folder, recursive, pixmaps);


        Pixmap onePixmap = pixmaps.values().iterator().next();
        int spriteWidth = onePixmap.getWidth(), spriteHeight = onePixmap.getHeight();

        atlas = new Pixmap(4096, 4096, Pixmap.Format.RGBA8888);
        int rowLength = atlas.getWidth() / spriteWidth;

        Iterator<Map.Entry<String, Pixmap>> entryIterator = pixmaps.entrySet().iterator();

        int x = 0, y = 0;
        while (entryIterator.hasNext()) {
            Map.Entry<String, Pixmap> entry = entryIterator.next();

            if ((x += spriteWidth) >= atlas.getWidth()) {
                x = 0;
                y += spriteHeight;
            }

            atlas.drawPixmap(entry.getValue(), x, y);
            iconMap.put(entry.getKey(), new Icon(x, y, spriteWidth, spriteHeight).scale(atlas.getWidth(), atlas.getHeight()));
        }
    }

    private static void readFolder(File folder, boolean recursive, Map<String, Pixmap> pixmaps) {
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".png")) {
                // read
                pixmaps.put(basename(file), new Pixmap(Gdx.files.absolute(file.getAbsolutePath())));
            }
            else if (file.isDirectory() && recursive) {
                readFolder(file, recursive, pixmaps);
            }
        }
    }

    private static final String basename(File file) {
        return file.getName().split("\\.(?=[^\\.]+$)")[0];
    }

    public Icon getUVFor(String texture) {
        return iconMap.get(texture);
    }

    public String getTextureOf(Icon icon) {
        return null;
    }

    public Pixmap getSheet() {
        return atlas;
    }
}
