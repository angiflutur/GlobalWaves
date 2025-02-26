package app.command.user.artist;

import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class RemoveAlbumCommand extends Command {

    private final String albumName;

    public RemoveAlbumCommand(final String username, final Integer timestamp, final String albumName) {
        super(username, timestamp);
        this.albumName = albumName;
    }

    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(this.getUsername());
        if (user == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "removeAlbum");
            resultNode.put("user", this.getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "The username " + this.getUsername() + " doesn't exist.");
            return;
        }

        if (user.getType() != User.UserType.ARTIST) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "removeAlbum");
            resultNode.put("user", this.getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", this.getUsername() + " is not an artist.");
            return;
        }

        Album albumToRemove = null;
        for (Album album : user.getAlbums()) {
            if (album.getName().equals(this.albumName)) {
                albumToRemove = album;
                break;
            }
        }
        if (albumToRemove == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "removeAlbum");
            resultNode.put("user", this.getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", this.getUsername() + " doesn't have an album with the given name.");
            return;
        }

        for (User u : library.getUsers()) {
            if (u.getType() == User.UserType.USER) {
                Player player = PlayerManager.getPlayer(u.getUsername());
                if (player != null && player.isLoaded()) {
                    if (player.getCurrentAlbum() != null && player.getCurrentAlbum().equals(albumToRemove)) {
                        ObjectNode resultNode = output.addObject();
                        resultNode.put("command", "removeAlbum");
                        resultNode.put("user", this.getUsername());
                        resultNode.put("timestamp", getTimestamp());
                        resultNode.put("message", this.getUsername() + " can't delete this album.");
                        return;
                    }
                }
            }
        }

        for (Playlist playlist : library.getPlaylists().values()) {
            for (Song song : playlist.getSongs()) {
                if (song.getAlbum().equals(albumToRemove)) {
                    ObjectNode resultNode = output.addObject();
                    resultNode.put("command", "removeAlbum");
                    resultNode.put("user", this.getUsername());
                    resultNode.put("timestamp", getTimestamp());
                    resultNode.put("message", this.getUsername() + " can't delete this album.");
                    return;
                }
            }
        }

        user.getAlbums().remove(albumToRemove);
        library.getAlbums().remove(albumToRemove);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "removeAlbum");
        resultNode.put("user", this.getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", this.getUsername() + " deleted the album successfully.");
    }

}
