package app.command.playlist;

import app.command.searchBar.SelectCommand;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.Command;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
        User user = library.getUser(getUsername());
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "showPlaylists");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        ArrayNode playlistsArray = resultNode.putArray("result");

        if (user != null) {
            for (Playlist playlist : user.getPlaylists()) {
                ObjectNode playlistNode = playlistsArray.addObject();
                playlistNode.put("name", playlist.getName());

                ArrayNode songsArray = playlistNode.putArray("songs");
                for (Song song : playlist.getSongs()) {
                    songsArray.add(song.getName());
                }

                playlistNode.put("visibility", playlist.getVisibility());
                playlistNode.put("followers", playlist.getFollowers());
            }
        }
        Player player = PlayerManager.getPlayer(getUsername());
        player.setCurrentPlaylist(null);
        player.setCurrentAudio(null);
        SelectCommand.setSelectedAudioFile(null);
        SelectCommand.setSelectedPlaylist(null);
        SelectCommand.setSelectedArtist(null);
        SelectCommand.setSelectedAlbum(null);
    }
}
