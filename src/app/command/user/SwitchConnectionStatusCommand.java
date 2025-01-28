package app.command.user;

import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.audio.collection.Library;
import app.entities.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 *
 */
public class SwitchConnectionStatusCommand extends Command {

    /**
     */
    public SwitchConnectionStatusCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "switchConnectionStatus");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "The username " + getUsername() + " doesn't exist.");
            return;
        }

        boolean currentStatus = user.isConnectionStatus();
        user.setConnectionStatus(!currentStatus);

        Player player = PlayerManager.getPlayer(getUsername());

        if (currentStatus) {
            player.updateRemainingTime(getTimestamp());
        }

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "switchConnectionStatus");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", getUsername() + " has changed status successfully.");
    }

}
