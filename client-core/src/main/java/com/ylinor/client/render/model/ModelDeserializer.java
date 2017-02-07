package com.ylinor.client.render.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

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
import com.ylinor.library.api.terrain.BlockExtraData;
import com.ylinor.library.util.Facing;


public class ModelDeserializer {
    private static JsonFactory factory = new JsonFactory();
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
    }
}
