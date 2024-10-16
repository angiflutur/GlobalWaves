package app.command.player;

import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class RepeatCommand extends Command {
    public RepeatCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = PlayerManager.getPlayer(getUsername());
        int currentTimestamp = getTimestamp();
        player.updateRemainingTime(currentTimestamp);

        if (!player.isLoaded()) {
            ObjectNode resultNode = createResultNode(output, currentTimestamp,
                    "Please load a source before setting the repeat status.");
            return;
        }

        String newRepeatMode = updateRepeatState(player);

        ObjectNode resultNode = createResultNode(output, currentTimestamp,
                "Repeat mode changed to " + newRepeatMode + ".");
    }

    /**
     * Update the repeat state and return the corresponding mode string.
     */
    private String updateRepeatState(final Player player) {
        String newRepeatMode;
        int repeatState = player.getRepeatState();

        if (player.getCurrentPlaylist() != null) {
            newRepeatMode = switch (repeatState) {
                case 0 -> {
                    player.setRepeatState(1);
                    yield "repeat all";
                }
                case 1 -> {
                    player.setRepeatState(2);
                    yield "repeat current song";
                }
                case 2 -> {
                    player.setRepeatState(0);
                    yield "no repeat";
                }
                default -> "no repeat";
            };
        } else {
            newRepeatMode = switch (repeatState) {
                case 0 -> {
                    player.setRepeatState(1);
                    yield "repeat once";
                }
                case 1 -> {
                    player.setRepeatState(2);
                    yield "repeat infinite";
                }
                case 2 -> {
                    player.setRepeatState(0);
                    yield "no repeat";
                }
                default -> "no repeat";
            };
        }
        return newRepeatMode;
    }

    /**
     * Create and return a result node for the output.
     */
    private ObjectNode createResultNode(final ArrayNode output,
                                        final int currentTimestamp,
                                        final String message) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "repeat");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", currentTimestamp);
        resultNode.put("message", message);
        return resultNode;
    }
}
