package app.command.stats;

import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class ShowPreferredSongsCommand extends Command {

    /**
     * JAVADOC
     */
    public ShowPreferredSongsCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "showPreferredSongs");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "User not found.");
            return;
        }

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "showPreferredSongs");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        ArrayNode preferredSongsArray = resultNode.putArray("result");

        if (user.getLikedSongs() == null) {
            resultNode.put("message", "No liked songs found.");
            return;
        }

        for (Song song : user.getLikedSongs()) {
            preferredSongsArray.add(song.getName());
        }
    }

}
