package app.command.searchBar;

import app.entities.audio.collection.Library;
import app.entities.audio.file.AudioFile;
import app.entities.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
/**
 * JAVADOC
 */
public class SelectCommand extends Command {
    private int itemNumber;
    private static ArrayList<AudioFile> lastSearchResults = new ArrayList<>();
    private static AudioFile selectedAudio = null;
    /**
     * JAVADOC
     */
    public SelectCommand(final String username,
                         final Integer timestamp, final int itemNumber) {
        super(username, timestamp);
        this.itemNumber = itemNumber;
    }
    /**
     * JAVADOC
     */
    public static AudioFile getSelectedAudio() {
        return selectedAudio;
    }
    /**
     * JAVADOC
     */
    public static void setSelectedAudio(final AudioFile selectedAudio) {
        SelectCommand.selectedAudio = selectedAudio;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        if (lastSearchResults.isEmpty()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Please conduct a search before making a selection.");
            return;
        }

        if (itemNumber < 1 || itemNumber > lastSearchResults.size()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "The selected ID is too high.");
            return;
        }

        selectedAudio = lastSearchResults.get(itemNumber - 1);
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "select");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", "Successfully selected " + selectedAudio.getName() + ".");
    }
    /**
     * JAVADOC
     */
    public static void updateLastSearchResults(final ArrayList<AudioFile> searchResults) {
        lastSearchResults = searchResults;
    }
}
