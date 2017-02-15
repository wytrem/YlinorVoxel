package com.ylinor.client.render.model;

import com.badlogic.gdx.files.FileHandle;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.resource.TextureAtlas;
import com.ylinor.library.api.world.blocks.BlockType;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;

import java.util.HashMap;
import java.util.Map;


public class ModelRegistry {
    
    
    private TShortObjectMap<String> blockStatesFiles;
    private Map<String, BlockModel> models;

    public ModelRegistry() {
        this.models = new HashMap<>();
        this.blockStatesFiles = new TShortObjectHashMap<>();
    
        registerStatesFiles();
    }
    
    public void loadFrom(FileHandle folder, TextureAtlas atlas) {
        
    }
    
    private void registerStatesFiles() {
        blockStatesFiles.put(BlockType.dirt.getId(), "dirt");
    }

    public void addModel(String name, BlockModel model) {
        if (isLoaded(name)) {
            System.err.println("WARNING: Un model (" + name + ") a ete charge deux fois, va frapper les devs s'il te plait");
        }

        models.put(name, model);
    }

    public BlockModel getModel(String name) {
        return models.get(name);
    }

    public boolean isLoaded(String name) {
        return models.get(name) != null;
    }
}
