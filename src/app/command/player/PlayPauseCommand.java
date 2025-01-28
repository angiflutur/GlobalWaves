package app.command.player;

import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class PlayPauseCommand extends Command {

    /**
     * JAVADOC
     */
    public PlayPauseCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {

        Player player = PlayerManager.getPlayer(getUsername());
        User user = library.getUser(getUsername());

        if (user == null || !user.isConnectionStatus()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }
        if (!player.isLoaded()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "playPause");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message",
                    "Please load a source before attempting to pause or resume playback.");
            return;
        }

        if (player.isPaused()) {
            player.play(getTimestamp());
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "playPause");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Playback resumed successfully.");
        } else {
            player.pause(getTimestamp());
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "playPause");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Playback paused successfully.");
        }
    }
}
