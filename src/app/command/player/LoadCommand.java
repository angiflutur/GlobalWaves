package app.command.player;

import app.command.searchBar.SelectCommand;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.AudioFile;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class LoadCommand extends Command {

    /**
     * JAVADOC
     */
    public LoadCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null || !user.isOnline()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "load");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }

        Player player = PlayerManager.getPlayer(getUsername());
        AudioFile selectedAudioFile = SelectCommand.getSelectedAudioFile();
        Playlist selectedPlaylist = SelectCommand.getSelectedPlaylist();
        Album selectedAlbum = SelectCommand.getSelectedAlbum();

        if (selectedAudioFile == null && selectedPlaylist == null && selectedAlbum == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "load");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Please select a source before attempting to load.");
            return;
        }

        if (library.getSongs().isEmpty()
                && library.getPodcasts().isEmpty() && library.getAlbums().isEmpty()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "load");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "You can't load an empty audio collection!");
            return;
        }

        if (player.isLoaded()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "load");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Please select a source before attempting to load.");
            return;
        }

        if (selectedAudioFile != null) {
            player.loadAudio(selectedAudioFile, getTimestamp());
        }

        if (selectedPlaylist != null) {
            player.loadPlaylist(selectedPlaylist, getTimestamp());
        }

        if (selectedAlbum != null) {
            player.loadAlbum(selectedAlbum, getTimestamp());
        }

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "load");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", "Playback loaded successfully.");
    }

}
