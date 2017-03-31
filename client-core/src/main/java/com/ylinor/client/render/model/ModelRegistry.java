package com.ylinor.client.render.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.render.model.block.Variants;
import com.ylinor.client.resource.TextureAtlas;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;
import gnu.trove.procedure.TShortObjectProcedure;


public class ModelRegistry {
    private static final Logger logger = LoggerFactory.getLogger(ModelRegistry.class);
    private TShortObjectMap<String> blockStatesFiles;
    private Map<String, BlockModel> models;
    private TShortObjectMap<Variants> variantsById;

    public ModelRegistry() {
        this.models = new HashMap<>();
        this.blockStatesFiles = new TShortObjectHashMap<>();
        this.variantsById = new TShortObjectHashMap<>(BlockType.REGISTRY.size());
        registerStatesFiles();
    }

    public void loadFrom(FileHandleResolver modelFileResolver, TextureAtlas atlas) {
        blockStatesFiles.forEachEntry(new TShortObjectProcedure<String>() {
            @Override
            public boolean execute(short arg0, String arg1) {
                try {
                    logger.info("Loading block model {}", arg1);
                    ModelDeserializer deserializer = ModelDeserializer.read(modelFileResolver.resolve(arg1), atlas, modelFileResolver);
                    deserializer.deserialize();
                    register(arg0, new Variants(deserializer.variants, deserializer));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private void registerStatesFiles() {
        for (BlockType type : BlockType.REGISTRY.valueCollection()) {
            if (type == BlockType.air) {
                continue;
            }
            register(type, type.getModelName());
        }
    }

    private void register(BlockType type, String stateFile) {
        blockStatesFiles.put(type.getId(), stateFile);
    }

    public void register(BlockType type, Variants variants) {
        register(type.getId(), variants);
    }
    
    public void register(short typeId, Variants variants) {
        variantsById.put(typeId, variants);
    }

    public BlockModel get(Terrain world, BlockType type, BlockState data) {
        return variantsById.get(type.getId()).get(world, type, data);
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
