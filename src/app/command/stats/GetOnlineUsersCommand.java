package app.command.stats;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JAVADOC
 */
public class GetOnlineUsersCommand extends Command {

    /**
     * JAVADOC
     */
    public GetOnlineUsersCommand(final Integer timestamp) {
        super(null, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        ArrayNode resultArray = output.objectNode().putArray("result");

        for (User user : library.getUsers()) {
            if (user.isOnline()) {
                resultArray.add(user.getUsername());
            }
        }

        output.add(output.objectNode()
                .put("command", "getOnlineUsers")
                .put("timestamp", getTimestamp())
                .set("result", resultArray));
    }

}
