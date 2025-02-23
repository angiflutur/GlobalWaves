package app.command.searchBar;

import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.AudioFile;
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
    private static User selectedArtist = null;
    /**
     * JAVADOC
     */
    public SelectCommand(final String username,
                         final Integer timestamp,
                         final Integer itemNumber) {
        super(username, timestamp);
        this.itemNumber = itemNumber;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null || !user.isOnline()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }

        ArrayList<Object> combinedResults = new ArrayList<>();
        combinedResults.addAll(SearchCommand.getLastSearchResultsAudio());
        combinedResults.addAll(SearchCommand.getLastSearchResultsPlaylists());
        combinedResults.addAll(SearchCommand.getLastSearchResultsArtists());

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
            resultNode.put("message", "Successfully selected "
                    + selectedAudioFile.getName() + ".");
        } else if (selectedItem instanceof Playlist) {
            selectedPlaylist = (Playlist) selectedItem;
            resultNode.put("message", "Successfully selected "
                    + selectedPlaylist.getName() + ".");
        } else if (selectedItem instanceof User) {
            User selectedUser = (User) selectedItem;
            resultNode.put("message", "Successfully selected "
                    + selectedUser.getUsername() + "'s page.");
            selectedArtist = selectedUser;
            if (selectedUser.getType() == User.UserType.ARTIST) {
                user.setCurrentPage(User.PageType.ARTIST_PAGE);
            } else if (selectedUser.getType() == User.UserType.HOST) {
                user.setCurrentPage(User.PageType.HOST_PAGE);
            }
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
    public static User getSelectedArtist() {
        return selectedArtist;
    }
    /**
     * JAVADOC
     */
    public static void setSelectedArtist(final User artist) {
        selectedArtist = artist;
    }

}
