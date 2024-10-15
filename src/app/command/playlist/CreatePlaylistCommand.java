package app.command.playlist;

import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.Command;
import app.entities.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * JAVADOC
 */
public class CreatePlaylistCommand extends Command {
    private String playlistName;

    /**
     * JAVADOC
     */
    public CreatePlaylistCommand(final String username,
                                 final Integer timestamp,
                                 final String playlistName) {
        super(username, timestamp);
        this.playlistName = playlistName;
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = null;
        for (User users : library.getUsers()) {
            if (users.getUsername().equals(getUsername())) {
                user = users;
                break;
            }
        }

        if (user == null) {
            ObjectNode errorResponse = JsonNodeFactory.instance.objectNode();
            errorResponse.put("command", "createPlaylist");
            errorResponse.put("user", getUsername());
            errorResponse.put("timestamp", getTimestamp());
            errorResponse.put("message", "User not found.");
            output.add(errorResponse);
            return;
        }

        if (library.getPlaylist(playlistName) != null) {
            ObjectNode errorResponse = JsonNodeFactory.instance.objectNode();
            errorResponse.put("command", "createPlaylist");
            errorResponse.put("user", getUsername());
            errorResponse.put("timestamp", getTimestamp());
            errorResponse.put("message", "A playlist with the same name already exists.");
            output.add(errorResponse);
        } else {
            Playlist newPlaylist = new Playlist(playlistName);
            newPlaylist.setOwner(user);
            newPlaylist.setPublic(true);

            library.getPlaylists().put(playlistName, newPlaylist);

            user.addPlaylist(newPlaylist);

            ObjectNode successResponse = JsonNodeFactory.instance.objectNode();
            successResponse.put("command", "createPlaylist");
            successResponse.put("user", getUsername());
            successResponse.put("timestamp", getTimestamp());
            successResponse.put("message", "Playlist created successfully.");
            newPlaylist.setCreationTime(getTimestamp());
            output.add(successResponse);
        }
    }
}
