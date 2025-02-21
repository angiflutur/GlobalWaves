package app.command.playlist;

import app.command.searchBar.SelectCommand;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.Command;
import app.entities.User;
import app.entities.audio.file.AudioFile;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class AddRemoveInPlaylistCommand extends Command {
    private Integer playlistId;

    /**
     * JAVADOC
     */
    public AddRemoveInPlaylistCommand(final String username,
                                      final Integer timestamp, final Integer playlistId) {
        super(username, timestamp);
        this.playlistId = playlistId;
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null || !user.isOnline()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "addRemoveInPlaylist");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }

        Player player = PlayerManager.getPlayer(getUsername());
        AudioFile selectedAudio = SelectCommand.getSelectedAudioFile();

        if (!player.isLoaded() || selectedAudio == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "addRemoveInPlaylist");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message",
                    "Please load a source before adding to or removing from the playlist.");
            return;
        }

        if (!(selectedAudio instanceof Song)) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "addRemoveInPlaylist");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "The loaded source is not a song.");
            return;
        }

        if (user == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "addRemoveInPlaylist");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "User not found.");
            return;
        }

        if (playlistId == null || playlistId <= 0 || playlistId > user.getPlaylists().size()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "addRemoveInPlaylist");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "The specified playlist does not exist.");
            return;
        }

        Playlist playlist = user.getPlaylists().get(playlistId - 1);

        boolean isInPlaylist = playlist.getSongs().contains(selectedAudio);

        if (isInPlaylist) {
            playlist.getSongs().remove(selectedAudio);
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "addRemoveInPlaylist");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Successfully removed from playlist.");
        } else {
            playlist.getSongs().add((Song) selectedAudio);
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "addRemoveInPlaylist");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Successfully added to playlist.");
        }
    }
}
