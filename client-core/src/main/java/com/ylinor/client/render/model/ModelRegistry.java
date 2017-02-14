package com.ylinor.client.render.model;

import com.ylinor.client.render.model.block.BlockModel;
import java.util.HashMap;
import java.util.Map;

public class ModelRegistry
{
    private Map<String, BlockModel> models;

    public ModelRegistry()
    {
        this.models = new HashMap<>();
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
