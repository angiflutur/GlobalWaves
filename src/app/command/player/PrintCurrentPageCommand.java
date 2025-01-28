package app.command.player;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.file.Song;
import app.entities.audio.collection.Playlist;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class PrintCurrentPageCommand extends Command {

    /**
     *
     */
    public PrintCurrentPageCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     *
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "printCurrentPage");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        if (user == null) {
            resultNode.put("message", "User not found.");
            return;
        }

        StringBuilder pageContent = new StringBuilder();

        List<Song> likedSongs = user.getLikedSongs();
        pageContent.append("Liked songs:\n\t")
                .append(likedSongs.isEmpty() ? "[]" : "[" + likedSongs.stream()
                        .map(Song::getName)
                        .collect(Collectors.joining(", ")) + "]")
                .append("\n\n");

        List<Playlist> followedPlaylists = user.getFollowedPlaylists();
        pageContent.append("Followed playlists:\n\t")
                .append(followedPlaylists.isEmpty() ? "[]" : "[" + followedPlaylists.stream()
                        .map(playlist -> playlist.getName() + " - "
                                + playlist.getOwner().getUsername())
                        .collect(Collectors.joining(", ")) + "]");

        resultNode.put("message", pageContent.toString());
    }
}
