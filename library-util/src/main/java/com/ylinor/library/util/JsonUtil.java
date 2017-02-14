package com.ylinor.library.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public final class JsonUtil
{
    private static final JsonFactory factory = new JsonFactory().setCodec(new ObjectMapper());

    public static JsonNode makeTree(File file) throws IOException {
        JsonParser parser = factory.createParser(file);
        return parser.readValueAsTree();
    }

    public static JsonNode merge(JsonNode mainNode, JsonNode updateNode, String... excludes) {
        Iterator<String> fieldNames = updateNode.fieldNames();
        List<String> excludesList = Arrays.asList(excludes);

        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();

            if (excludesList.contains(fieldName)) {
                continue;
            }

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

    public static void walkTree(JsonNode tree, BiConsumer<String, JsonNode> consumer) {
        Iterator<Entry<String, JsonNode>> it = tree.fields();

        while (it.hasNext()) {
            Entry<String, JsonNode> entry = it.next();
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }
}
