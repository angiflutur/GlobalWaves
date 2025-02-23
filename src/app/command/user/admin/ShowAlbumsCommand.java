package app.command.user.admin;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
/**
 * JAVADOC
 */
public class ShowAlbumsCommand extends Command {

    private final String username;
    /**
     * JAVADOC
     */
    public ShowAlbumsCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
        this.username = username;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(username);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "showAlbums");
        resultNode.put("user", username);
        resultNode.put("timestamp", getTimestamp());

        if (user != null && user.getType() == User.UserType.ARTIST) {
            ArrayNode albumsNode = resultNode.putArray("result");

            user.getAlbums().forEach(album -> {
                ObjectNode albumNode = albumsNode.addObject();
                albumNode.put("name", album.getName());

                ArrayNode songsNode = albumNode.putArray("songs");
                album.getSongs().forEach(song -> songsNode.add(song.getName()));
            });

        } else {
            resultNode.put("message", "The username " + username + " is not an artist.");
        }
    }
}
