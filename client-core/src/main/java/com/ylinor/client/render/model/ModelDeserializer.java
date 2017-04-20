package com.ylinor.client.render.model;

import static com.ylinor.library.util.JsonUtil.*;
import static com.ylinor.library.util.JsonUtil.mergeExcluding;
import static com.ylinor.library.util.JsonUtil.walkTree;
import static com.ylinor.library.util.JsonUtil.walkTreeOrArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.ylinor.client.render.model.block.FaceRenderInfo;
import com.ylinor.client.resource.TextureAtlas;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.props.StateProperty;
import com.ylinor.library.util.Facing;
import com.ylinor.library.util.spring.Assert;


public class ModelDeserializer {
    private static final Logger logger = LoggerFactory.getLogger(ModelDeserializer.class);

    private static final float SCALE_ROTATION_22_5 = 1.0F / (float) Math.cos(0.39269909262657166D) - 1.0F;
    private static final float SCALE_ROTATION_GENERAL = 1.0F / (float) Math.cos((Math.PI / 4D)) - 1.0F;

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

    private ObjectNode bakeJson(ObjectNode originalJson, String name) {
        ObjectNode bakedJson = originalJson.deepCopy();
        JsonNode parentNameNode = originalJson.get("parent");

        ObjectNode bakedParent = null;

        if (parentNameNode != null && !parentNameNode.isMissingNode() && parentNameNode.isTextual()) {
            String parentName = parentNameNode.textValue();

            try {
                ObjectNode parentOriginalNode = makeTree(modelsResolver.resolve(parentName)
                                                                       .read());

                bakedParent = bakeJson(parentOriginalNode, parentName);
            }
            catch (IOException e) {
                logger.error("Model '{}' declares unknown model '{}' as parent.", name, parentName);
                throw new ModelParseException("Unable to read and parse parent for model " + name, e);
            }
        }

        if (bakedParent != null) {
            bakedJson = mergeExcluding(bakedParent, originalJson, "variants");
            if (originalJson.has("variants")) {
                bakedJson.replace("variants", originalJson.get("variants"));
            }
            else {
                bakedJson.remove("variants");
            }
        }

        logger.trace("Baked for {} is {}", name, bakedJson.toString());

        return bakedJson;
    }

    public void deserialize() {

        this.bakedJson = bakeJson(originalJson, this.name);

        try {
            logger.trace("Mapper: {}, textures node: {}", mapper, bakedJson.get("textures"));
            this.textures = mapper.treeToValue(bakedJson.get("textures"), Map.class);
        }
        catch (JsonProcessingException e) {
            throw new ModelParseException("Unable to read model textures of model " + name, e);
        }

        if (this.bakedJson.has("variants")) {
            JsonNode variantsNode = this.bakedJson.get("variants");

            if (variantsNode.isObject()) {
                ObjectNode variantsObjectNode = ((ObjectNode) variantsNode);
                walkTree(variantsObjectNode, (variant, apply) -> {
                    ObjectNode model = mergeExcluding(this.bakedJson, apply);
                    model.remove("variants");
                    logger.trace("Variant '{}' is {}", variant, model);
                    ModelDeserializer deserializer = new ModelDeserializer(name + "#" + variant, atlas, model, this.modelsResolver);
                    deserializer.deserialize();
                    this.variants.put(StateProperty.sort(variant), deserializer.getModel());
                });
            }
        }
        else {
            this.cubes = readPart(bakedJson.get("elements"), null);

            variants.put("", getModel());
        }
    }

    public BlockModel variant(BlockState state) {
        if (this.bakedJson.has("variants")) {
            JsonNode variantsNode = this.bakedJson.get("variants");

            if (variantsNode.isObject()) {
                return variants.get(state.propertiesToString());
            }
            else if (variantsNode.isArray()) {
                ArrayNode variantsArrayNode = ((ArrayNode) variantsNode);
                
                ObjectNode model = this.bakedJson;
                Iterator<JsonNode> it = variantsArrayNode.elements();

                while (it.hasNext()) {
                    JsonNode element = it.next();
                    if (element.has("when") && element.has("apply")) {
                        
                        ObjectNode when = (ObjectNode) element.get("when");
                        ObjectNode apply = (ObjectNode) element.get("apply");

                        if (matches(when, state)) {
                            model = mergeExcluding(model, apply);
                            model.remove("variants");
                        }
                    }
                }
                
                ModelDeserializer deserializer = new ModelDeserializer(name + "#" + state.propertiesToString(), atlas, model, this.modelsResolver);
                deserializer.deserialize();
                
                return deserializer.getModel();
            }
        }

        return null;
    }

    private boolean matches(ObjectNode when, BlockState state) {
        Iterator<Entry<String, JsonNode>> it = when.fields();

        while (it.hasNext()) {
            Entry<String, JsonNode> entry = it.next();
            if (!state.has(entry.getKey(), entry.getValue().textValue())) {
                return false;
            }
        }
        
        return true;
    }

    private static Vector3f readArrayToVector(JsonNode node) {
        return readArrayToVector((ArrayNode) node);
    }

    private static Vector3f readArrayToVector(ArrayNode array) {
        return new Vector3f((float) array.get(0)
                                         .asDouble(), (float) array.get(1)
                                                                   .asDouble(), (float) array.get(2)
                                                                                             .asDouble());
    }

    public List<Cube> readPart(JsonNode model, Cube parent) {
        List<Cube> parts = new ArrayList<>();

        walkTreeOrArray(model, (part, cube) -> {
            JsonNode children = cube.get("children");

            // ----- Basic infos : postition, size, rotation

            Vector3f position = new Vector3f(), size = new Vector3f(),
                            rotationOrigin = new Vector3f();
            Quaternionf rotation = new Quaternionf();

            try {

                if (cube.has("position")) {
                    position = readArrayToVector(cube.get("position"));
                    position.mul(0.0625f);

                    size = readArrayToVector(cube.get("size"));
                    size.mul(0.0625f);
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
                    if (rot.has("value")) {
                        if (rot.get("value").isArray()) {
                            rotation = new Quaternionf((float) rot.get("value")
                                                                  .get(0)
                                                                  .asDouble(), (float) rot.get("value")
                                                                                          .get(1)
                                                                                          .asDouble(), (float) rot.get("value")
                                                                                                                  .get(2)
                                                                                                                  .asDouble(), (float) rot.get("value")
                                                                                                                                          .get(2)
                                                                                                                                          .asDouble());
                            if (rot.has("origin")) {
                                rotationOrigin = readArrayToVector(rot.get("origin")).mul(0.0625f);
                            }
                        }
                        else {
                            rotation = mapper.treeToValue(rot.get("value"), Quaternionf.class);
                        }
                    }
                    else if (rot.has("axis") && rot.has("angle")) {

                        float angle = (float) rot.get("angle").asDouble();
                        angle = (float) Math.toRadians(angle);

                        if (rot.get("axis").textValue().equals("x")) {
                            rotation.rotateAxis(angle, new Vector3f(1, 0, 0));
                        }

                        if (rot.get("axis").textValue().equals("y")) {
                            rotation.rotateAxis(angle, new Vector3f(0, 1, 0));
                        }

                        if (rot.get("axis").textValue().equals("z")) {
                            rotation.rotateAxis(angle, new Vector3f(0, 0, 1));
                        }

                        if (rot.has("origin")) {
                            rotationOrigin = readArrayToVector(rot.get("origin")).mul(0.0625f);
                        }

                        if (rot.has("rescale")) {
                            if (rot.get("rescale").asBoolean()) {
                                if (Math.abs(angle) == 22.5F) {
                                    size.mul(SCALE_ROTATION_22_5);
                                }
                                else {
                                    size.mul(SCALE_ROTATION_GENERAL);
                                }
                            }
                        }
                    }
                    else {
                        rotation = mapper.treeToValue(rot, Quaternionf.class);
                    }
                }

                if (parent != null) {
                    rotation.mul(parent.rotation);
                }
            }
            catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new ModelParseException("Unable to read json model " + name, e);
            }

            Cube cubeObj = new Cube(part, position, size, rotation, rotationOrigin);

            // ----- Children

            List<Cube> childrenParts = null;

            if (children != null) {
                childrenParts = readPart(children, cubeObj);
                cubeObj.setChildren(childrenParts);
            }

            // ----- Textures mapping & cullfaces

            JsonNode faces = cube.get("faces");

            Map<Facing, FaceRenderInfo> texturesMapping = new HashMap<>();
            Map<Facing, Facing> cullfaces = new HashMap<>();

            JsonNode defNode = faces.has("allfaces") ? faces.get("allfaces") : new ObjectNode(JsonNodeFactory.instance);

            if (defNode.isTextual()) {
                String defTexture = defNode.textValue();
                defNode = new ObjectNode(JsonNodeFactory.instance);
                ((ObjectNode) defNode).put("texture", defTexture);
            }

            if (!defNode.has("texture")) {
                ((ObjectNode) defNode).put("texture", "#unknown");
            }

            if (!defNode.has("uv")) {
                ArrayNode uv = new ArrayNode(JsonNodeFactory.instance);
                uv.add(0).add(0).add(16).add(16);
                ((ObjectNode) defNode).set("uv", uv);
            }

            if (!defNode.has("rotation")) {
                ((ObjectNode) defNode).put("rotation", 0);
            }

            if (!defNode.has("useColorMultiplier")) {
                ((ObjectNode) defNode).put("useColorMultiplier", true);
            }

            final JsonNode voilaLeFinalTamer = defNode;

            if (faces.has("allfaces")) {
                Arrays.stream(Facing.values()).forEach(face -> {
                    texturesMapping.put(face, FaceRenderInfo.fromJson(voilaLeFinalTamer, this::getIconFromMacro));
                });
            }

            walkTree(faces, (face, obj) -> {
                if (face.equals("allfaces")) {
                    return;
                }

                Facing facing = getFacing(face);
                FaceRenderInfo uv = FaceRenderInfo.fromJson(mergeExcluding(voilaLeFinalTamer, obj), this::getIconFromMacro);

                texturesMapping.put(facing, uv);

                if (obj.has("cullface")) {

                    cullfaces.put(facing, getFacing(obj.get("cullface")
                                                       .textValue()));
                }
            });

            cubeObj.setFaces(texturesMapping);
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

        if (macro.equals("#unknown")) {
            return "unknown";
        }

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
            //            deserialize();
            return null;
        }
        return new BlockModel(name, cubes);
    }

    public static ModelDeserializer read(FileHandle file, TextureAtlas atlas, FileHandleResolver resolver) throws IOException {
        return new ModelDeserializer(file.nameWithoutExtension(), atlas, makeTree(file.read()), resolver);
    }
}
