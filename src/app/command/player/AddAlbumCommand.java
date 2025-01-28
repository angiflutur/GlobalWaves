package app.command.player;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class AddAlbumCommand extends Command {

    private final String name;
    private final Integer releaseYear;
    private final String description;
    private final List<Song> songs;

    /**
     *
     */
    public AddAlbumCommand(final String username, final Integer timestamp, final String name,
                           final Integer releaseYear,
                           final String description, final List<Song> songs) {
        super(username, timestamp);
        this.name = name;
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "addAlbum");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        try {
            validateUser(user);
            validateAlbum(user);
            validateSongs();

            user.addAlbum(name, releaseYear, description, songs);
            resultNode.put("message", getUsername()
                    + " has added new album successfully.");
        } catch (IllegalArgumentException e) {
            resultNode.put("message", e.getMessage());
        }
    }
    /**
     * JAVADOC
     */
    private void validateUser(final User user) {
        if (user == null) {
            throw new IllegalArgumentException("The username "
                    + getUsername() + " doesn't exist.");
        }
        if (!user.isArtist()) {
            throw new IllegalArgumentException(getUsername()
                    + " is not an artist.");
        }
    }
    /**
     * JAVADOC
     */
    private void validateAlbum(final User user) {
        if (user.hasAlbum(name)) {
            throw new IllegalArgumentException(getUsername()
                    + " has another album with the same name.");
        }
    }
    /**
     * JAVADOC
     */
    private void validateSongs() {
        Set<String> songNames = new HashSet<>();
        for (Song song : songs) {
            if (!songNames.add(song.getName())) {
                throw new IllegalArgumentException(getUsername()
                        + " has the same song at least twice in this album.");
            }
        }
    }
}
