package com.ylinor.client.render.model;

import com.ylinor.client.resource.Assets;
import com.ylinor.client.resource.TextureAtlas;
import com.ylinor.library.util.JsonUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Stream;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.render.model.block.Cube;
import com.ylinor.client.render.model.block.UVMapping;
import com.ylinor.library.util.Facing;


import static com.ylinor.library.util.JsonUtil.*;


public class ModelDeserializer {
    private ObjectMapper mapper = new ObjectMapper();
    private TextureAtlas atlas;
    private ModelRegistry registry;

    private String name;
    private JsonNode model;
    private List<Cube> cubes;
    private Map<String, String> textures;
    private List<BlockModel> variants;

    public ModelDeserializer(String name, ModelRegistry registry, TextureAtlas atlas, JsonNode tree) {
        this.name = name;
        this.registry = registry;
        this.atlas = atlas;
        this.model = tree;
        this.variants = new ArrayList<>();
    }

    public void deserialize() {
        JsonNode parentNode = model.get("parent");

        if (parentNode != null) {
            File parentFile = new File(Assets.get().getModelFolder(), parentNode.textValue() + ".json");
            JsonNode parent;

            try
            {
                parent = makeTree(parentFile);
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to read model '" + parentFile + "' (parent of " + name + ")", e);
            }

            this.model = merge(parent, this.model);
        }

        try
        {
            this.textures = mapper.treeToValue(model.get("textures"), Map.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Unable to read model textures of model " + name, e);
        }

        this.cubes = readPart(model.get("elements"), null);

        JsonNode variantsNode = this.model.get("variants");

        if (variantsNode != null) {
            walkTree(variantsNode, (name, variant) -> {
                JsonNode model = merge(this.model, variant, "variants");
                ModelDeserializer deserializer = new ModelDeserializer(name, registry, atlas, model);

                this.variants.addAll(Arrays.asList(deserializer.getModel()));
            });
        }
    }

    public List<Cube> readPart(JsonNode model, JsonNode parent) {
        List<Cube> parts = new ArrayList<>();

        walkTree(model, (part, cube) -> {
            if (parent != null) {
                cube = JsonUtil.merge(parent, cube, "children");
            }

            JsonNode children = cube.get("children");
            JsonNode faces = cube.get("faces");

            List<Cube> childrenParts = null;

            Vector3f position = new Vector3f(), size = new Vector3f();
            Quaternionf rotation = new Quaternionf();

            if (children == null) {
                try {
                    position = mapper.treeToValue(cube.get("position"), Vector3f.class);
                    size = mapper.treeToValue(cube.get("size"), Vector3f.class);

                    JsonNode rot = cube.get("rotation");

                    if (rot != null) {
                        rotation = mapper.treeToValue(rot, Quaternionf.class);
                    }
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Unable to read json model " + name, e);
                }
            }
            else {
                childrenParts = readPart(children, cube);
            }

            Map<Facing, UVMapping> mapping = new HashMap<>();

            if (faces.isObject()) {
                JsonNode defNode = faces.get("allFaces");
                String def = defNode == null ? null : defNode.textValue();

                walkTree(faces, (face, obj) -> {
                    if (face.equals("allfaces")) {
                        //Stream.of(Facing.values()).filter((f) -> !mapping.containsKey(f)).forEach((f) -> mapping.put(f, new UVMapping(new int[] {0, 0, 32, 32}, atlas.getUVFor(obj.textValue()))));
                        return;
                    }

                    Facing facing = Facing.valueOf(face);
                    UVMapping uv = UVMapping.fromJson(obj, def, (s) -> atlas.getUVFor(textures.get(s.substring(1))));

                    mapping.put(facing, uv);
                });
            } else {
                String texture = faces.textValue();
                Stream.of(Facing.values()).forEach((f) -> mapping.put(f, new UVMapping(new int[] {0, 0, 32, 32}, atlas.getUVFor(textures.get(texture.substring(1))))));
            }

            parts.add(new Cube(part, position, size, rotation, childrenParts, mapping));
        });

        return parts;
    }

    public BlockModel[] getModel() {
        if (cubes == null) {
            deserialize();
        }

        ArrayList<BlockModel> models = new ArrayList<>();
        models.add(new BlockModel(name, cubes));
        models.addAll(variants);

        return models.toArray(new BlockModel[models.size()]);
    }

    public static ModelDeserializer read(File file, ModelRegistry registry, TextureAtlas atlas) throws IOException {
        String name = file.getName();
        return new ModelDeserializer(name.substring(0, name.lastIndexOf(".json")), registry, atlas, makeTree(file));
    }
}
