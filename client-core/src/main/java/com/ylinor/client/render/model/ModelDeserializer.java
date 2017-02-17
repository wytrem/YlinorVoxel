package com.ylinor.client.render.model;

import static com.ylinor.library.util.JsonUtil.makeTree;
import static com.ylinor.library.util.JsonUtil.mergeExcluding;
import static com.ylinor.library.util.JsonUtil.walkArray;
import static com.ylinor.library.util.JsonUtil.walkTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.ylinor.library.util.spring.Assert;


public class ModelDeserializer {
    private static final Logger logger = LoggerFactory.getLogger(ModelDeserializer.class);

    private ObjectMapper mapper = new ObjectMapper();
    private TextureAtlas atlas;

    private String name;
    private ObjectNode originalJson;
    private List<Cube> cubes;
    private Map<String, String> textures;
    public Map<String, BlockModel> variants;
    private FileHandleResolver modelsResolver;

    private ObjectNode bakedJson;

    public ModelDeserializer(String name, TextureAtlas atlas, ObjectNode tree, FileHandleResolver fileHandleResolver) {
        this.name = name;
        this.atlas = atlas;
        this.originalJson = tree;
        this.variants = new HashMap<>();
        this.modelsResolver = fileHandleResolver;
    }

    private ObjectNode bakeJson(ObjectNode originalJson) {
        ObjectNode bakedJson = originalJson.deepCopy();
        JsonNode parentNameNode = originalJson.get("parent");

        ObjectNode bakedParent = null;

        if (parentNameNode != null && !parentNameNode.isMissingNode() && parentNameNode.isTextual()) {
            String parentName = parentNameNode.textValue();

            try {
                ObjectNode parentOriginalNode = makeTree(modelsResolver.resolve(parentName)
                                                                       .read());

                bakedParent = bakeJson(parentOriginalNode);
            }
            catch (IOException e) {
                logger.error("Model '{}' declares unknown model '{}' as parent.", name, parentName);
                throw new ModelParseException("Unable to read and parse parent for model " + name, e);
            }
        }

        if (bakedParent != null) {
            bakedJson = mergeExcluding(bakedParent, originalJson, "variants");
        }

        return bakedJson;
    }

    public void deserialize() {

        this.bakedJson = bakeJson(originalJson);
        
        try {
            this.textures = mapper.treeToValue(bakedJson.get("textures"), Map.class);
        }
        catch (JsonProcessingException e) {
            throw new ModelParseException("Unable to read model textures of model " + name, e);
        }

        this.cubes = readPart(bakedJson.get("elements"), null);

        JsonNode variantsNode = this.bakedJson.get("variants");

        if (variantsNode != null) {
            if (variantsNode.isArray()) {
                ArrayNode variantsArray = ((ArrayNode) variantsNode);
                walkArray(variantsArray, variant -> {
                    ObjectNode model = mergeExcluding(this.bakedJson, variant.get("apply"));
                    model.remove("variants");
                    ModelDeserializer deserializer = new ModelDeserializer(name, atlas, model, this.modelsResolver);
                    this.variants.put(variant.get("when")
                                             .toString(), deserializer.getModel());
                });
            }
        }
    }
    
    private static Vector3f readArrayToVector(JsonNode node) {
        return readArrayToVector((ArrayNode) node);
    }
    
    private static Vector3f readArrayToVector(ArrayNode array) {
        return new Vector3f((float) array.get(0).asDouble(), (float) array.get(1).asDouble(), (float) array.get(2).asDouble());
    }

    public List<Cube> readPart(JsonNode model, Cube parent) {
        List<Cube> parts = new ArrayList<>();

        walkTree(model, (part, cube) -> {
            JsonNode children = cube.get("children");

            // ----- Basic infos : postition, size, rotation

            Vector3f position = new Vector3f(), size = new Vector3f();
            Quaternionf rotation = new Quaternionf();

            try {
                
                if (cube.has("position")) {
                    position = readArrayToVector(cube.get("position"));
                    position.mul(0.03125f);
                    
                    size = readArrayToVector(cube.get("size"));
                    size.mul(0.03125f);
                }
                else if (cube.has("from")) {
                    position = readArrayToVector(cube.get("from"));
                    position.mul(0.0625f);
                    
                    Vector3f to = readArrayToVector(cube.get("to"));
                    to.mul(0.0625f);
                    
                    size = to.sub(position, new Vector3f());
                }

                if (parent != null) {
                    position.add(parent.position);
                }
               
                JsonNode rot = cube.get("rotation");

                if (rot != null) {
                    rotation = mapper.treeToValue(rot, Quaternionf.class);
                }
                
                if (parent != null) {
                    rotation.mul(parent.rotation);
                }
            }
            catch (JsonProcessingException e) {
                throw new ModelParseException("Unable to read json model " + name, e);
            }

            Cube cubeObj = new Cube(part, position, size, rotation);

            // ----- Children

            List<Cube> childrenParts = null;

            if (children != null) {
                childrenParts = readPart(children, cubeObj);
                cubeObj.setChildren(childrenParts);
            }

            // ----- Textures mapping & cullfaces

            JsonNode faces = cube.get("faces");

            Map<Facing, UVMapping> texturesMapping = new HashMap<>();
            Map<Facing, Facing> cullfaces = new HashMap<>();

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

                Facing facing = getFacing(face);
                UVMapping uv = UVMapping.fromJson(mergeExcluding(voilaLeFinalTamer, obj), this::getIconFromMacro);

                texturesMapping.put(facing, uv);

                if (obj.has("cullface")) {
                    
                    cullfaces.put(facing, getFacing(obj.get("cullface")
                                                            .textValue()));
                }
            });

            cubeObj.setTextures(texturesMapping);
            cubeObj.setCullfaces(cullfaces);

            parts.add(cubeObj);
        });

        return parts;
    }
    
    private static Facing getFacing(String name) {
        return Facing.valueOf(name.toUpperCase());
    }

    private String getTextureFromMacro(String macro) {
        Assert.state(macro.startsWith("#"));

        while (macro != null && macro.startsWith("#")) {
            macro = textures.get(macro.substring(1));
        }

        return macro;
    }

    private Icon getIconFromMacro(String macro) {
        Assert.state(macro.startsWith("#"));
        return atlas.getUVFor(getTextureFromMacro(macro));
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
