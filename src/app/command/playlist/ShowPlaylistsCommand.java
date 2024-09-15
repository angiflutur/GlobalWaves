package app.command.playlist;

import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.Command;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

/**
 * JAVADOC
 */
public class ShowPlaylistsCommand extends Command {

    /**
     * JAVADOC
     */
    public ShowPlaylistsCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Map<String, Playlist> userPlaylists = library.getPlaylists();

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "showPlaylists");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        ArrayNode playlistsArray = resultNode.putArray("result");

        for (Map.Entry<String, Playlist> entry : userPlaylists.entrySet()) {
            Playlist playlist = entry.getValue();
            ObjectNode playlistNode = playlistsArray.addObject();
            playlistNode.put("name", playlist.getName());
            playlistNode.put("visibility", playlist.getVisibility());
            playlistNode.put("followers", playlist.getFollowers());

            ArrayNode songsArray = playlistNode.putArray("songs");
            for (Song song : playlist.getSongs()) {
                songsArray.add(song.getName());
            }
        }
    }
}
