package com.ylinor.library.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ylinor.library.util.spring.Assert;


public final class JsonUtil {
    private static final JsonFactory factory = new JsonFactory().setCodec(new ObjectMapper());

    public static ObjectNode makeTree(File file) throws IOException {
        Assert.notNull(file, "Cannot read from a null file");
        JsonParser parser = factory.createParser(file);
        return parser.readValueAsTree();
    }

    public static ObjectNode makeTree(InputStream input) throws IOException {
        Assert.notNull(input, "Cannot read from a null InputStream");
        JsonParser parser = factory.createParser(input);
        return parser.readValueAsTree();
    }

    @SuppressWarnings("deprecation")
    public static ObjectNode mergeExcluding(JsonNode mainNode, JsonNode updateNode, String... excludes) {
        if (mainNode == null) {
            return updateNode.deepCopy();
        }
        else if (updateNode == null) {
            return mainNode.deepCopy();
        }

        Assert.state(mainNode.isObject(), "mainNode must be an object node");
        Assert.state(updateNode.isObject(), "updateNode must be an object node");

        ObjectNode result = mainNode.deepCopy();
        Iterator<String> updateFieldNames = updateNode.fieldNames();
        List<String> excludesList = Arrays.asList(excludes);

        while (updateFieldNames.hasNext()) {
            String fieldName = updateFieldNames.next();

            if (excludesList.contains(fieldName)) {
                continue;
            }

            JsonNode jsonNode = mainNode.get(fieldName);

            if (jsonNode != null && jsonNode.isObject()) {
                result.set(fieldName, mergeExcluding(jsonNode, updateNode.get(fieldName)));
            }
            else {
                JsonNode value = updateNode.get(fieldName);
                result.put(fieldName, value);
            }
        }

        return result;
    }

    public static void walkTree(JsonNode tree, BiConsumer<String, JsonNode> consumer) {
        Iterator<Entry<String, JsonNode>> it = tree.fields();

        while (it.hasNext()) {
            Entry<String, JsonNode> entry = it.next();
            consumer.accept(entry.getKey(), entry.getValue());
        }
    }

    public static void walkArray(ArrayNode array, Consumer<JsonNode> consumer) {
        Iterator<JsonNode> it = array.elements();

        while (it.hasNext()) {
            JsonNode entry = it.next();
            consumer.accept(entry);
        }
    }
}
