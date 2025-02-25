package app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import main.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
 * JAVADOC
 */
public class JSONComparator {

    private final ObjectMapper objectMapper;

    /**
     * JAVADOC
     */
    public JSONComparator() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * JAVADOC
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
     * JAVADOC
     */
    public static void main(final String[] args) {
        try {
            Main.main(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONComparator comparator = new JSONComparator();

        String file0 = "test00_etapa2.json";
        String file1 = "test01_etapa2.json";
        String file2 = "test02_etapa2.json";
        String file3 = "test03_etapa2.json";
        String file4 = "test04_etapa2.json";
        String file5 = "test05_etapa2_playPause_playlist_podcast.json";
        String file6 = "test06_etapa2_repeat.json";
        String file7 = "test07_etapa2_repeat_error.json";
        String file8 = "test08_etapa2_searchHost_printCurrentPage.json";
        String file9 = "test09_etapa2_shuffle_album.json";
        String file10 = "test10_etapa2_next_prev_forward_backward.json";
        String file11 = "test11_etapa2_shuffle_error.json";
        String file12 = "test12_etapa2_next_prev_forward_backward_error.json";
        String file13 = "test13_statistics.json";
        String file14 = "test14_etapa2_delete_cases.json";
        String file15 = "test15_etapa2_complex.json";
        String file16 = "test16_etapa2_complex.json";

        String outputFile = "comparison_output.txt";

        comparator.clearOutputFile(outputFile);

        String file = file6;
        comparator.compareJsonFiles("ref/ref_" + file, "result/out_" + file, outputFile);
    }
}
