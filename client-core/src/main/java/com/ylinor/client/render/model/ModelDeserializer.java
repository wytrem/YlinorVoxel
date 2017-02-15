package com.ylinor.client.render.model;

import static com.ylinor.library.util.JsonUtil.makeTree;
import static com.ylinor.library.util.JsonUtil.merge;
import static com.ylinor.library.util.JsonUtil.walkArray;
import static com.ylinor.library.util.JsonUtil.walkTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.render.model.block.Cube;
import com.ylinor.client.render.model.block.UVMapping;
import com.ylinor.client.resource.TextureAtlas;
import com.ylinor.library.util.Facing;
import com.ylinor.library.util.JsonUtil;
import com.ylinor.library.util.spring.Assert;


public class ModelDeserializer {
    private ObjectMapper mapper = new ObjectMapper();
    private TextureAtlas atlas;
    private ModelRegistry registry;

    private String name;
    private JsonNode jsonSource;
    private List<Cube> cubes;
    private Map<String, String> textures;
    public Map<String, BlockModel> variants;
    private FileHandleResolver modelsResolver;

    public ModelDeserializer(String name, ModelRegistry registry, TextureAtlas atlas, JsonNode tree, FileHandleResolver fileHandleResolver) {
        this.name = name;
        this.registry = registry;
        this.atlas = atlas;
        this.jsonSource = tree;
        this.variants = new HashMap<>();
        this.modelsResolver = fileHandleResolver;
    }
    
    public void deserialize() {
        JsonNode parentNode = jsonSource.get("parent");

        if (parentNode != null) {
            FileHandle parentFile = modelsResolver.resolve(parentNode.textValue());
            JsonNode parent;

            try
            {
                parent = makeTree(parentFile.read());
            }
            catch (IOException e)
            {
                throw new RuntimeException("Unable to read model '" + parentFile + "' (parent of " + name + ")", e);
            }

            this.jsonSource = merge(parent, this.jsonSource);
        }

        try
        {
            this.textures = mapper.treeToValue(jsonSource.get("textures"), Map.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Unable to read model textures of model " + name, e);
        }

        this.cubes = readPart(jsonSource.get("elements"), null);

        JsonNode variantsNode = this.jsonSource.get("variants");

        if (variantsNode != null) {
            if (variantsNode.isArray()) {
                ArrayNode variantsArray = ((ArrayNode) variantsNode);
                walkArray(variantsArray, variant -> {
                    JsonNode model = merge(this.jsonSource, variant.get("apply"), "variants");
                    ModelDeserializer deserializer = new ModelDeserializer(name, registry, atlas, model, this.modelsResolver);
                    this.variants.put(variant.get("when").toString(), deserializer.getModel()[0]);
                });
            }
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
                JsonNode defNode = faces.get("allfaces");
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
                Stream.of(Facing.values()).forEach((f) -> mapping.put(f, new UVMapping(new int[] {0, 0, 32, 32}, getIconFromMacro(texture))));
            }

            parts.add(new Cube(part, position, size, rotation, childrenParts, mapping));
        });

        return parts;
    }
    
    private Icon getIconFromMacro(String macro) {
        Assert.state(macro.startsWith("#"));
        return atlas.getUVFor(textures.get(macro.substring(1)));
    }

    public BlockModel[] getModel() {
        if (cubes == null) {
            deserialize();
        }

        ArrayList<BlockModel> models = new ArrayList<>();
        models.add(new BlockModel(name, cubes));
//        models.addAll(variants);

        return models.toArray(new BlockModel[models.size()]);
    }

    public static ModelDeserializer read(FileHandle file, ModelRegistry registry, TextureAtlas atlas, FileHandleResolver resolver) throws IOException {
        return new ModelDeserializer(file.nameWithoutExtension(), registry, atlas, makeTree(file.read()), resolver);
    }
}
