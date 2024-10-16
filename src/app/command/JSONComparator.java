package app.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Constructor.
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
     * Compares two JSON files for identity in content and order, outputting results to a file.
     */
    public void compareJsonFiles(final String filePath1,
                                 final String filePath2,
                                 final String outputFilePath) {
        int differencesCount = 0;
        try (FileWriter writer = new FileWriter(outputFilePath, true)) {
            JsonNode json1 = objectMapper.readTree(new File(filePath1));
            JsonNode json2 = objectMapper.readTree(new File(filePath2));

            differencesCount = compareJsonNodes(json1, json2, writer);
        } catch (IOException e) {
            System.err.println("Error reading JSON files: " + e.getMessage());
        }

        System.out.println("Number of differences found: " + differencesCount);
    }

    /**
     * JAVADOC
     */
    private int compareJsonNodes(final JsonNode json1,
                                 final JsonNode json2,
                                 final FileWriter writer)
            throws IOException {
        int differencesCount = 0;

        if (json1.isArray() && json2.isArray()) {
            for (int i = 0; i < json1.size(); i++) {
                JsonNode node1 = json1.get(i);
                JsonNode node2 = json2.get(i);

                if (!node1.equals(node2)) {
                    differencesCount++;
                    int timestamp = node1.has("timestamp") ? node1.get("timestamp").asInt() : -1;
                    writer.write("Diferență la timestampul " + timestamp + ":\n");
                    writer.write("  File 1: " + node1 + "\n");
                    writer.write("  File 2: " + node2 + "\n\n");
                }
            }
        } else {
            writer.write("Cel puțin unul dintre fișiere nu este un array.\n");
        }
        return differencesCount;
    }
    private void clearOutputFile(final String outputFilePath) {
        try (FileWriter writer = new FileWriter(outputFilePath, false)) {
            System.out.println("Clearing output file.");
        } catch (IOException e) {
            System.err.println("Error clearing output file: " + e.getMessage());
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
        JSONComparator comparator = new JSONComparator();
        String file1 = "test01_searchBar_songs_podcasts.json";
        String file2 = "test02_playPause_song.json";
        String file3 = "test03_like_create_addRemove.json";
        String file4 = "test04_like_create_addRemove_error.json";
        String file5 = "test05_playPause_playlist_podcast.json";
        String file6 = "test06_playPause_error.json";
        String file7 = "test07_repeat.json";
        String file8 = "test08_repeat_error.json";
        String file9 = "test09_shuffle.json";
        String file10 = "test10_shuffle_error.json";
        String file11 = "test11_next_prev_forward_backward.json";
        String file12 = "test12_next_prev_forward_backward_error.json";
        String file13 = "test13_searchPlaylist_follow.json";
        String file14 = "test14_searchPlaylist_follow_error.json";
        String file15 = "test15_statistics.json";
        String file16 = "test16_complex.json";
        String file17 = "test17_complex.json";
        String outputFile = "comparison_output.txt";

        comparator.clearOutputFile(outputFile);

        System.out.println("\nFile 16");
        comparator.compareJsonFiles("ref/ref_" + file16, "result/out_" + file16, outputFile);
    }
}
