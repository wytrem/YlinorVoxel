package com.ylinor.client.render.model;

import com.ylinor.client.resource.TextureAtlas;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import java.util.stream.Stream;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylinor.client.render.model.block.BlockModel;
import com.ylinor.client.render.model.block.Cube;
import com.ylinor.client.render.model.block.UVMapping;
import com.ylinor.library.util.Facing;


public class ModelDeserializer {
    /*private static JsonFactory factory = new JsonFactory();
    private static ObjectMapper mapper = new ObjectMapper();

    public static BlockModel read(File file) throws IOException {
        JsonParser parser = factory.createParser(file);
        TreeNode tree = parser.readValueAsTree();
        String name = tree.get("name").asToken().asString();
        String parent = tree.get("extends").asToken().asString();
        TreeNode model = tree.get("model");
        TreeNode textures = tree.get("textures");
        TreeNode variants = tree.get("variants");
        List<Cube> parts = readModel(model, textures);
        return null;
    }

    public static List<Cube> readModel(TreeNode model, TreeNode textures) throws JsonProcessingException {
        List<Cube> parts = new ArrayList<>();
        Iterator<String> it = model.fieldNames();
        while (it.hasNext()) {
            String part = it.next();
            TreeNode cube = model.get(part);
            TreeNode children = model.get("children");
            List<Cube> childrenParts = null;
            Vector3f position = new Vector3f(), size = new Vector3f();
            Quaternionf rotation = new Quaternionf();
            if (children.isMissingNode()) {
                position = mapper.treeToValue(cube.get("position"), Vector3f.class);
                size = mapper.treeToValue(cube.get("size"), Vector3f.class);
                rotation = mapper.treeToValue(cube.get("rotation"), Quaternionf.class);
            }
            else {
                childrenParts = readModel(children, textures);
            }
            Map<Facing, UVMapping> mapping = new HashMap<>();
            TreeNode partTextures = textures.get(part);
            Map<String, Icon> icons = readTextures((JsonNode) textures);
            parts.add(new Cube(part, position, size, rotation, childrenParts, null));
        }
        return null;
    }

    public TreeNode getNode(TreeNode parent, TreeNode tree, String path) {
        TreeNode node = parent.path(path);
        if (node.isMissingNode()) {
            node = tree.path(path);
        }
        return node;
    }

    static Map<Predicate<BlockExtraData>, BlockModel> readVariants(String json, Function<String, BlockModel> modelSupplier) {
        Map<Predicate<BlockExtraData>, BlockModel> variants = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(json);
        }
        catch (IOException e) {
            e.printStackTrace();
        } // Lecture de la liste de textures         
        Map<String, Icon> icons = readTextures(root.at("icon"));
        BlockModel base; // lecture depuis l'attribut "model"         
        return variants;
    }

    static Map<String, Icon> readTextures(JsonNode origin) {
        Map<String, Icon> icons = new HashMap<>();
        origin.forEach(childNode -> {
        });
        return icons;
    }*/

    private static JsonFactory factory = new JsonFactory();

    private ObjectMapper mapper = new ObjectMapper();
    private TextureAtlas atlas;
    private ModelRegistry registry;

    private String name;
    private TreeNode model;
    private List<Cube> cubes;
    private List<BlockModel> variants;

    public ModelDeserializer(String name, ModelRegistry registry, TextureAtlas atlas, TreeNode tree) {
        this.name = name;
        this.registry = registry;
        this.atlas = atlas;
        this.model = tree;
    }

    public void deserialize() {
        // TODO: VARIANTS

        TreeNode parentNode = model.get("parent");

        if (parentNode.isMissingNode()) {
            this.cubes = readPart(model.get("elements"));
        } else {
            String parentName = parentNode.asToken().asString();

            if (!registry.isLoaded(parentName)) {
                try {
                    // TODO: GET FILE
                    Stream.of(ModelDeserializer.read(null, registry, atlas).getModel()).forEach((m) -> registry.addModel(m.getName(), m));
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read model", e);
                }
            }

            BlockModel parent = registry.getModel(parentName);

            this.cubes = parent.getCubes();

            List<Cube> cubes = readPart(model.get("elements"));
            cubes.forEach((c) -> {
                for (int i = 0; i < this.cubes.size(); i++) {
                    if (c.getId().equals(this.cubes.get(i).getId())) {
                        this.cubes.remove(i);
                        break;
                    }
                }

                this.cubes.add(c);
            });
        }
    }

    public List<Cube> readPart(TreeNode model) {
        List<Cube> parts = new ArrayList<>();

        walkTree(model, (part, cube) -> {
            TreeNode children = model.get("children");
            TreeNode faces = model.get("faces");

            List<Cube> childrenParts = null;

            Vector3f position = new Vector3f(), size = new Vector3f();
            Quaternionf rotation = new Quaternionf();

            if (children.isMissingNode()) {
                try {
                    position = mapper.treeToValue(cube.get("position"), Vector3f.class);
                    size = mapper.treeToValue(cube.get("size"), Vector3f.class);
                    rotation = mapper.treeToValue(cube.get("rotation"), Quaternionf.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Unable to read json model " + name, e);
                }
            }
            else {
                childrenParts = readPart(children);
            }

            Map<Facing, UVMapping> mapping = new HashMap<>();

            if (faces.isObject()) {
                walkTree(faces, (face, obj) -> {
                    if (face.equals("allfaces")) {
                        Stream.of(Facing.values()).filter((f) -> !mapping.containsKey(f)).forEach((f) -> mapping.put(f, new UVMapping(new int[] {0, 0, 32, 32}, atlas.getUVFor(obj.asToken().asString()))));
                        return;
                    }

                    Facing facing = Facing.valueOf(face);
                    UVMapping uv = UVMapping.fromJson((JsonNode) obj, (s) -> atlas.getUVFor(s));

                    mapping.put(facing, uv);
                });
            } else {
                String texture = faces.asToken().asString();
                Stream.of(Facing.values()).forEach((f) -> mapping.put(f, new UVMapping(new int[] {0, 0, 32, 32}, atlas.getUVFor(texture))));
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

    public static void walkTree(TreeNode tree, BiConsumer<String, TreeNode> consumer) {
        Iterator<String> it = tree.fieldNames();

        while (it.hasNext()) {
            String part = it.next();
            TreeNode node = tree.get(part);

            consumer.accept(part, node);
        }
    }

    public static ModelDeserializer read(File file, ModelRegistry registry, TextureAtlas atlas) throws IOException {
        JsonParser parser = factory.createParser(file);
        String name = file.getName();

        return new ModelDeserializer(name.substring(0, name.lastIndexOf(".json")), registry, atlas, parser.readValueAsTree());
    }
}
