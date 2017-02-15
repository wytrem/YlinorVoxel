package com.ylinor.client.render.model;

import static com.ylinor.library.util.JsonUtil.makeTree;
import static com.ylinor.library.util.JsonUtil.mergeAndExclude;
import static com.ylinor.library.util.JsonUtil.walkArray;
import static com.ylinor.library.util.JsonUtil.walkTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

    private String name;
    private JsonNode jsonSource;
    private List<Cube> cubes;
    private Map<String, String> textures;
    public Map<String, BlockModel> variants;
    private FileHandleResolver modelsResolver;

    public ModelDeserializer(String name, TextureAtlas atlas, JsonNode tree, FileHandleResolver fileHandleResolver) {
        this.name = name;
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

            try {
                parent = makeTree(parentFile.read());
            }
            catch (IOException e) {
                throw new RuntimeException("Unable to read model '" + parentFile + "' (parent of " + name + ")", e);
            }

            this.jsonSource = mergeAndExclude(parent, this.jsonSource);
        }

        try {
            this.textures = mapper.treeToValue(jsonSource.get("textures"), Map.class);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to read model textures of model " + name, e);
        }

        this.cubes = readPart(jsonSource.get("elements"), parentNode);

        JsonNode variantsNode = this.jsonSource.get("variants");

        if (variantsNode != null) {
            if (variantsNode.isArray()) {
                ArrayNode variantsArray = ((ArrayNode) variantsNode);
                walkArray(variantsArray, variant -> {
                    JsonNode model = mergeAndExclude(this.jsonSource, variant.get("apply"), "variants");
                    ModelDeserializer deserializer = new ModelDeserializer(name, atlas, model, this.modelsResolver);
                    this.variants.put(variant.get("when")
                                             .toString(), deserializer.getModel());
                });
            }
        }
    }

    public List<Cube> readPart(JsonNode model, JsonNode parent) {
        List<Cube> parts = new ArrayList<>();
        
        if (model == null) {
            model = parent.get("elements");
        }

        walkTree(model, (part, cube) -> {
            if (parent != null) {
                cube = JsonUtil.mergeAndExclude(parent, cube, "children");
            }

            JsonNode children = cube.get("children");
            JsonNode faces = cube.get("faces");

            List<Cube> childrenParts = null;

            Vector3f position = new Vector3f(), size = new Vector3f();
            Quaternionf rotation = new Quaternionf();

            if (children == null) {
                try {
                    position = mapper.treeToValue(cube.get("position"), Vector3f.class);
                    position.mul(0.03125f);
                    size = mapper.treeToValue(cube.get("size"), Vector3f.class);
                    size.mul(0.03125f);
                    JsonNode rot = cube.get("rotation");

                    if (rot != null) {
                        rotation = mapper.treeToValue(rot, Quaternionf.class);
                    }
                }
                catch (JsonProcessingException e) {
                    throw new RuntimeException("Unable to read json model " + name, e);
                }
            }
            else {
                childrenParts = readPart(children, cube);
            }

            Map<Facing, UVMapping> mapping = new HashMap<>();

            JsonNode defNode = faces.get("allfaces");

            if (defNode == null) {
                defNode = new ObjectNode(JsonNodeFactory.instance);
            }
            else if (defNode.isTextual()) {
                String defTexture = defNode.textValue();
                defNode = new ObjectNode(JsonNodeFactory.instance);
                ((ObjectNode) defNode).put("texture", defTexture);
            }

            if (!defNode.has("uv")) {
                ArrayNode uv = new ArrayNode(JsonNodeFactory.instance);
                uv.add(0).add(0).add(32).add(32);
                ((ObjectNode) defNode).set("uv", uv);
            }

            if (!defNode.has("rotation")) {
                ((ObjectNode) defNode).put("rotation", 0);
            }

            final JsonNode voilaLeFinalTamer = defNode;

            walkTree(faces, (face, obj) -> {
                if (face.equals("allfaces")) {
                    return;
                }

                Facing facing = Facing.valueOf(face);
                UVMapping uv = UVMapping.fromJson(mergeAndExclude(voilaLeFinalTamer, obj), this::getIconFromMacro);

                mapping.put(facing, uv);
            });

            parts.add(new Cube(part, position, size, rotation, childrenParts, mapping));
        });

        return parts;
    }

    private Icon getIconFromMacro(String macro) {
        Assert.state(macro.startsWith("#"));
        return atlas.getUVFor(textures.get(macro.substring(1)));
    }

    public BlockModel getModel() {
        if (cubes == null) {
            deserialize();
        }
        return new BlockModel(name, cubes);
    }

    public static ModelDeserializer read(FileHandle file, TextureAtlas atlas, FileHandleResolver resolver) throws IOException {
        return new ModelDeserializer(file.nameWithoutExtension(), atlas, makeTree(file.read()), resolver);
    }
}
