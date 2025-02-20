package app.command.playlist;

import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.audio.collection.Playlist;
import app.entities.User;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * JAVADOC
 */
public class SwitchVisibilityCommand extends Command {
    private Integer itemNumber;

    /**
     * JAVADOC
     */
    public SwitchVisibilityCommand(final String username,
                                   final Integer timestamp,
                                   final Integer itemNumber) {
        super(username, timestamp);
        this.itemNumber = itemNumber - 1;
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null) {
            output.addObject()
                    .put("command", "switchVisibility")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .put("message", "User not found.");
            return;
        }

        if (itemNumber < 0 || itemNumber >= user.getPlaylists().size()) {
            output.addObject()
                    .put("command", "switchVisibility")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .put("message", "The specified playlist ID is too high.");
            return;
        }

        Playlist playlist = user.getPlaylists().get(itemNumber);
        boolean currentVisibility = playlist.isPublic();
        playlist.setPublic(!currentVisibility);

        String newVisibility = playlist.isPublic() ? "public" : "private";

        output.addObject()
                .put("command", "switchVisibility")
                .put("user", getUsername())
                .put("timestamp", getTimestamp())
                .put("message", "Visibility status updated successfully to " + newVisibility + ".");
    }
}
