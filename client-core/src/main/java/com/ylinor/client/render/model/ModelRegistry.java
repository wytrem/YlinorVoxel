package com.ylinor.client.render.model;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.render.model.block.UniqueVariant;
import com.ylinor.client.render.model.block.Variants;
import com.ylinor.client.resource.TextureAtlas;
import com.ylinor.library.api.world.World;
import com.ylinor.library.api.world.blocks.BlockExtraData;
import com.ylinor.library.api.world.blocks.BlockType;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;
import gnu.trove.procedure.TShortObjectProcedure;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
                    register(arg0, new UniqueVariant(deserializer.getModel()));
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }

    private void registerStatesFiles() {
        register(BlockType.stone, "stone");
        register(BlockType.dirt, "anvil_undamaged");
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

    public BlockModel get(World world, BlockType type, BlockExtraData data) {
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
