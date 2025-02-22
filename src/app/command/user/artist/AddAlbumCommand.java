package app.command.user.artist;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JAVADOC
 */
public class AddAlbumCommand extends Command {
    private final String albumName;
    private final int releaseYear;
    private final String description;
    private final List<Song> songs;
    /**
     * JAVADOC
     */
    public AddAlbumCommand(final String username, final Integer timestamp,
                           final String albumName, final int releaseYear,
                           final String description, final List<Song> songs) {
        super(username, timestamp);
        this.albumName = albumName;
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        ObjectNode result = output.addObject();
        result.put("command", "addAlbum");
        result.put("user", getUsername());
        result.put("timestamp", getTimestamp());

        User user = library.getUser(getUsername());
        if (user == null) {
            result.put("message", "The username " + getUsername() + " doesn't exist.");
            return;
        }

        if (!user.getType().equals(User.UserType.ARTIST)) {
            result.put("message", getUsername() + " is not an artist.");
            return;
        }

        for (Album album : library.getAlbums()) {
            if (album.getName().equals(albumName) && album.getArtist().equals(getUsername())) {
                result.put("message", getUsername() + " has another album with the same name.");
                return;
            }
        }

        Set<String> songNames = new HashSet<>();
        for (Song song : songs) {
            if (!songNames.add(song.getName())) {
                result.put("message", getUsername()
                        + " has the same song at least twice in this album.");
                return;
            }
        }

        Album newAlbum = new Album(albumName, releaseYear, description, getUsername(), songs);
        library.getAlbums().add(newAlbum);
        result.put("message", getUsername() + " has added new album successfully.");
    }
}
