package app.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Main;

import java.io.File;
import java.io.IOException;

/**
 * JSON Comparator for finding differences based on timestamps.
 */
public class JSONComparator {

    private final ObjectMapper objectMapper;

    /**
     * Constructor.
     */
    public JSONComparator() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Compares two JSON files based on timestamps.
     */
    public void compareJsonFiles(final String filePath1, final String filePath2) {
        try {
            JsonNode json1 = objectMapper.readTree(new File(filePath1));
            JsonNode json2 = objectMapper.readTree(new File(filePath2));

            compareByTimestamp(json1, json2);
        } catch (IOException e) {
            System.err.println("Error reading JSON files: " + e.getMessage());
        }
    }

    /**
     * Compares nodes by timestamp.
     */
    private void compareByTimestamp(final JsonNode json1, final JsonNode json2) {
        if (json1.isArray()) {
            for (JsonNode node1 : json1) {
                int timestamp = node1.get("timestamp").asInt();
                JsonNode node2 = findNodeByTimestamp(json2, timestamp);

                if (node2 != null) {
                    compareNodes(node1, node2, "Timestamp: " + timestamp);
                } else {
                    System.out.println("No matching timestamp in second JSON for: " + timestamp);
                }
            }
        }
    }

    /**
     * Finds a node in json2 by timestamp.
     */
    private JsonNode findNodeByTimestamp(final JsonNode json2,
                                         final int timestamp) {
        if (json2.isArray()) {
            for (JsonNode node : json2) {
                if (node.has("timestamp") && node.get("timestamp").asInt() == timestamp) {
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * Compares two JSON nodes.
     */
    private void compareNodes(final JsonNode node1, final JsonNode node2, final String path) {
        if (!node1.equals(node2)) {
            System.out.println("Difference found at " + path + ": ");
            System.out.println("  File 1 value: " + node1);
            System.out.println("  File 2 value: " + node2);

            System.out.println();
        }
    }

    /**
     * Main method to execute the comparator.
     */
    public static void main(final String[] args) {
        try {
            Main.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();
        JSONComparator comparator = new JSONComparator();
        comparator.compareJsonFiles("ref/ref_test11_next_prev_forward_backward.json",
                "result/out_test11_next_prev_forward_backward.json");
    }
    //    test01_searchBar_songs_podcasts.json DONE
    //    test02_playPause_song.json DONE
    //    test03_like_create_addRemove.json DONE
    //    test04_like_create_addRemove_error.json DONE
    //    test05_playPause_playlist_podcast.json DONE
    //    test06_playPause_error.json DONE
    //    test07_repeat.json DONE
    //    test08_repeat_error.json DONE
    //    test09_shuffle.json DONE
    //    test10_shuffle_error.json DONE

    //    test11_next_prev_forward_backward.json
    //    test12_next_prev_forward_backward_error.json
    //    test13_searchPlaylist_follow.json
    //    test14_searchPlaylist_follow_error.json
    //    test15_statistics.json
    //    test16_complex.json
    //    test17_complex.json
}
