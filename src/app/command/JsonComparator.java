package app.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * JAVADOC
 */
public class JsonComparator {

    private final ObjectMapper objectMapper;

    /**
     * JAVADOC
     */
    public JsonComparator() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * JAVADOC
     */
    public void compareJsonFiles(final String filePath1,
                                 final String filePath2) {
        try {
            JsonNode json1 = objectMapper.readTree(new File(filePath1));
            JsonNode json2 = objectMapper.readTree(new File(filePath2));
            compareNodes(json1, json2, "");
        } catch (IOException e) {
            System.err.println("Error reading JSON files: " + e.getMessage());
        }
    }

    /**
     * JAVADOC
     */
    private void compareNodes(final JsonNode node1,
                              final JsonNode node2,
                              final String path) {
        if (node1.isObject() && node2.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields1 = node1.fields();
            Iterator<Map.Entry<String, JsonNode>> fields2 = node2.fields();

            while (fields1.hasNext()) {
                Map.Entry<String, JsonNode> field1 = fields1.next();
                JsonNode field2 = node2.get(field1.getKey());

                compareNodes(field1.getValue(), field2, path
                        + "/" + field1.getKey());
            }

            while (fields2.hasNext()) {
                Map.Entry<String, JsonNode> field2 = fields2.next();
                if (!node1.has(field2.getKey())) {
                    System.out.println("Node present in second JSON but not in first: "
                            + path + "/" + field2.getKey());
                }
            }
        } else if (node1.isArray() && node2.isArray()) {
            for (int i = 0; i < Math.max(node1.size(), node2.size()); i++) {
                JsonNode arrayNode1 = (i < node1.size()) ? node1.get(i) : null;
                JsonNode arrayNode2 = (i < node2.size()) ? node2.get(i) : null;

                compareNodes(arrayNode1, arrayNode2, path + "[" + i + "]");
            }
        } else if (!node1.equals(node2)) {
            System.out.println("Difference found at " + path + ": ");
            System.out.println("  File 1 value: " + node1);
            System.out.println("  File 2 value: " + node2);
            printTimestamp(node1);
            printTimestamp(node2);
        }
    }

    /**
     * Afișează timestamp-ul dacă există în nodul JSON
     */
    private void printTimestamp(final JsonNode node) {
        if (node.has("timestamp")) {
            System.out.println("  Timestamp: " + node.get("timestamp").asInt());
        }
    }

    /**
     * JAVADOC
     */
    public static void main(final String[] args) {
        JsonComparator comparator = new JsonComparator();
        comparator.compareJsonFiles("ref/ref_test06_playPause_error.json",
                "result/out_test06_playPause_error.json");
    }
}
