package app.command.searchBar;

import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.AudioFile;
import app.entities.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
* JAVADOC
*/
public class SelectCommand extends Command {
    private Integer itemNumber;
    private static AudioFile selectedAudioFile = null;
    private static Playlist selectedPlaylist = null;
    private static String selectedArtist = null;
    /**
     * JAVADOC
     */
    public SelectCommand(final String username, final Integer timestamp, final Integer itemNumber) {
        super(username, timestamp);
        this.itemNumber = itemNumber;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null || !user.isConnectionStatus()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }

        ArrayList<AudioFile> lastSearchResultsAudio = SearchCommand.getLastSearchResultsAudio();
        ArrayList<Playlist> lastSearchResultsPlaylists
                = SearchCommand.getLastSearchResultsPlaylists();
        ArrayList<String> lastSearchResultsArtists = SearchCommand.getLastSearchResultsArtists();

        ArrayList<Object> combinedResults = new ArrayList<>();
        combinedResults.addAll(lastSearchResultsAudio);
        combinedResults.addAll(lastSearchResultsPlaylists);
        combinedResults.addAll(lastSearchResultsArtists);

        Player player = PlayerManager.getPlayer(getUsername());
        player.setRepeatState(0);

        if (!SearchCommand.getIsSearching()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Please conduct a search before making a selection.");
            return;
        }

        if (itemNumber < 1 || itemNumber > combinedResults.size()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "The selected ID is too high.");
            return;
        }

        Object selectedItem = combinedResults.get(itemNumber - 1);
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "select");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        if (selectedItem instanceof AudioFile) {
            selectedAudioFile = (AudioFile) selectedItem;
            resultNode.put("message", "Successfully selected " + selectedAudioFile.getName() + ".");
        } else if (selectedItem instanceof Playlist) {
            selectedPlaylist = (Playlist) selectedItem;
            resultNode.put("message", "Successfully selected " + selectedPlaylist.getName() + ".");
        } else if (selectedItem instanceof String) {
            selectedArtist = (String) selectedItem;
            resultNode.put("message", "Successfully selected " + selectedArtist + "'s page.");
        }

        player.setCurrentPlaylist(selectedItem instanceof Playlist
                ? (Playlist) selectedItem : null);

        SearchCommand.clearLastSearchResults();
        SearchCommand.setIsSearching(false);
    }
    /**
     * JAVADOC
     */
    public static void setSelectedAudioFile(final AudioFile audioFile) {
        selectedAudioFile = audioFile;
    }
    /**
     * JAVADOC
     */
    public static void setSelectedPlaylist(final Playlist playlist) {
        selectedPlaylist = playlist;
    }
    /**
     * JAVADOC
     */
    public static void setSelectedArtist(final String artist) {
        selectedArtist = artist;
    }
    /**
     * JAVADOC
     */
    public static AudioFile getSelectedAudioFile() {
        return selectedAudioFile;
    }
    /**
     * JAVADOC
     */
    public static Playlist getSelectedPlaylist() {
        return selectedPlaylist;
    }
    /**
     * JAVADOC
     */
    public static String getSelectedArtist() {
        return selectedArtist;
    }

}
