package com.ylinor.client.resource;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.ylinor.client.render.model.ModelRegistry;


public class BlockAssets implements Loadable {
    @SuppressWarnings("unused")
    private final Assets assets;

    /**
     * The folder where the models are
     */
    private FileHandle modelFolder;

    /**
     * Models folder
     */
    public static final String MODELS_FOLDER = "models/";

    public ModelRegistry modelsRegistry;
    public TextureAtlas blockAtlas;

    BlockAssets(Assets assets) {
        this.assets = assets;
    }

    public void load() {
        modelFolder = Gdx.files.internal(MODELS_FOLDER);

        // Models
        blockAtlas = new TextureAtlas();

        blockAtlas.loadFrom(new File(new File("./img/blocks").getAbsolutePath()), false, 512);

        modelsRegistry = new ModelRegistry();
        modelsRegistry.loadFrom(this::getModel, blockAtlas);

        //            //            BlockModel[] models = ModelDeserializer.read(new File(modelFolder, "test.json"), new ModelRegistry(), atlas).getModel();
        //            ModelDeserializer test = ModelDeserializer.read(modelFolder.child("test.json"), blockAtlas, modelName -> getModel(modelName));
        //            test.deserialize();
        //            System.out.println(test.variants);
        //
        //            System.out.println("ON A REUSSSI !!!");
    }

    public FileHandle getModelFolder() {
        return modelFolder;
    }

    public FileHandle getModel(String modelName) {
        return modelFolder.child(modelName + ".json");
    }
}
