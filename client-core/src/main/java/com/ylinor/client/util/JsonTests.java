package com.ylinor.client.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonTests {
    public static void main(String[] args) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        String json = "{\"name\":\"Bob\", \"age\":{\"toto\":3,\"titi\":\"aze\"}}";
        System.out.println(pointers(mapper.readTree(json)));
    }

    static List<String> pointers(JsonNode node) {
        List<String> list = new ArrayList<>();

        pointers(node, list, "");

        return list;
    }

    static void pointers(JsonNode node, List<String> pointers, String prefix) {
        Iterator<Entry<String, JsonNode>> fields = node.fields();

        while (fields.hasNext()) {
            Entry<String, JsonNode> field = fields.next();

            if (field.getValue().isObject()) {
                pointers(field.getValue(), pointers, field.getKey() + "/");
            }
            else {
                pointers.add(prefix + field.getKey());
            }
        }
    }
}
