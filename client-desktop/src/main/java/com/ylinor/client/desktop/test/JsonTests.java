package com.ylinor.client.desktop.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class JsonTests {
    public static final void main(String[] args) throws IOException {
        String json = "\n" +
                "\n" +
                "{\n" +
                "  \"address\" : { \"street\" : \"2940 5th Ave\", \"zip\" : 980021 }, \n" +
                "  \"dimensions\" : [ 10.0, 20.0, 15.0 ]\n" +
                "}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(json);

        Iterator<Map.Entry<String, JsonNode>> fields = root.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();

            System.out.println(entry);
        }

    }
}
