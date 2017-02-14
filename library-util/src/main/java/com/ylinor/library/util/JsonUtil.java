package com.ylinor.library.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.function.BiConsumer;

public final class JsonUtil
{
    private static final JsonFactory factory = new JsonFactory();

    public static TreeNode makeTree(File file) throws IOException {
        JsonParser parser = factory.createParser(file);
        return parser.readValueAsTree();
    }

    public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode jsonNode = mainNode.get(fieldName);

            if (jsonNode != null && jsonNode.isObject()) {
                merge(jsonNode, updateNode.get(fieldName));
            } else {
                if (mainNode instanceof ObjectNode) {
                    JsonNode value = updateNode.get(fieldName);
                    ((ObjectNode) mainNode).put(fieldName, value);
                }
            }
        }

        return mainNode;
    }

    public static void walkTree(TreeNode tree, BiConsumer<String, TreeNode> consumer) {
        Iterator<String> it = tree.fieldNames();

        while (it.hasNext()) {
            String part = it.next();
            TreeNode node = tree.get(part);

            consumer.accept(part, node);
        }
    }
}
