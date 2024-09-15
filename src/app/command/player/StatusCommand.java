package app.command.player;

import app.entities.Player;
import app.entities.audio.collection.Library;
import app.entities.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class StatusCommand extends Command {
    /**
     * JAVADOC
     */
    public StatusCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {

        Player player = Player.getInstance();

        int currentTimestamp = getTimestamp();

        player.updateRemainingTime(currentTimestamp);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "status");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", currentTimestamp);

        ObjectNode statsNode = resultNode.putObject("stats");

        if (player.getRemainingTime(currentTimestamp) > 0) {
            statsNode.put("name", player.getCurrentAudio() != null
                    ? player.getCurrentAudio().getName() : "");
        } else {
            statsNode.put("name", "");
        }
        statsNode.put("remainedTime", player.getRemainingTime(currentTimestamp));
        statsNode.put("repeat", "No Repeat");
        statsNode.put("shuffle", false);
        statsNode.put("paused", player.isPaused());
    }

}
