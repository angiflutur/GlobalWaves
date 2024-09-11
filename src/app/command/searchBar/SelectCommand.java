package app.command.searchBar;

import app.audio.collection.Library;
import app.audio.file.Song;
import app.command.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public class SelectCommand extends Command {
    private int itemNumber;
    private static ArrayList<Song> lastSearchResults = new ArrayList<>();

    public SelectCommand(String username, Integer timestamp, int itemNumber) {
        super(username, timestamp);
        this.itemNumber = itemNumber;
    }

    @Override
    public void execute(ArrayNode output, Library library) {
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

        Song selectedSong = lastSearchResults.get(itemNumber - 1);
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "select");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", "Successfully selected " + selectedSong.getName() + ".");
    }

    public static void updateLastSearchResults(ArrayList<Song> searchResults) {
        lastSearchResults = searchResults;
    }
    public static void updateLastSearchPodcastResults(ArrayList<Song> searchResults) {
        lastSearchResults = searchResults;
    }
}
