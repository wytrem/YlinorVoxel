package com.ylinor.client.resource;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.ylinor.client.render.model.Icon;

public class TextureAtlas {
    
    private static final Logger logger = LoggerFactory.getLogger(TextureAtlas.class);

    private Map<String, Icon> iconMap;
    private Pixmap atlas;


    public void loadFrom(File folder, boolean recursive, int atlasSize) {
        FileHandleResolver resolver = new FileHandleResolver() {
            @Override
            public FileHandle resolve(String fileName) {
                return Gdx.files.absolute(new File(folder, fileName).getAbsolutePath());
            }
        };

        List<String> files = new ArrayList<>();

        addFiles(folder.toPath(), folder, recursive, files);

        load(resolver, files, atlasSize);
    }

    private static final void addFiles(Path relativeTo, File folder, boolean recursive, List<String> files) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory() && recursive) {
                addFiles(relativeTo, file, recursive, files);
            } else if (file.getName().endsWith(".png")) {
                files.add(relativeTo.relativize(Paths.get(file.getAbsolutePath())).toString());
            }
        }
    }

    public void load(FileHandleResolver fileHandleResolver, List<String> textures, int atlasSize) {
        iconMap = new HashMap<>();

        Map<String, Pixmap> pixmaps = new HashMap<>(64);


        for (String texture : textures) {
            pixmaps.put(basename(texture), new Pixmap(fileHandleResolver.resolve(texture)));
        }

        Pixmap onePixmap = pixmaps.values().iterator().next();
        int spriteWidth = onePixmap.getWidth(), spriteHeight = onePixmap.getHeight();

        atlas = new Pixmap(atlasSize, atlasSize, Pixmap.Format.RGBA8888);

        Iterator<Map.Entry<String, Pixmap>> entryIterator = pixmaps.entrySet().iterator();

        int x = 0, y = 0;
        while (entryIterator.hasNext()) {
            Map.Entry<String, Pixmap> entry = entryIterator.next();

            atlas.drawPixmap(entry.getValue(), x, y);
            iconMap.put(entry.getKey(), Icon.fromPosSize(x, y, spriteWidth, spriteHeight).scale(atlas.getWidth(), atlas.getHeight()));

            if ((x += spriteWidth) >= atlas.getWidth()) {
                x = 0;
                y += spriteHeight;
            }
        }
    }

    private static final String basename(String file) {
        return file.split("\\.(?=[^\\.]+$)")[0];
    }

    public Icon getUVFor(String texture) {
        Icon icon = iconMap.get(texture);
        
        if (icon == null) {
            logger.warn("Missing '{}' texture atlas.", texture);
            return getUVFor("unknown");
        }
        return icon;
    }

    public String getTextureOf(Icon icon) {
        return null;
    }

    public Pixmap getSheet() {
        return atlas;
    }
}
