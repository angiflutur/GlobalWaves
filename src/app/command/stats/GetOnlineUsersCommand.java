package app.command.stats;

import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class GetOnlineUsersCommand extends Command {

    /**
     *
     */
    public GetOnlineUsersCommand(final Integer timestamp) {
        super(null, timestamp);
    }

    /**
     *
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        List<String> onlineUsers = library.getUsers().stream()
                .filter(user -> user.isConnectionStatus() && !"artist".equals(user.getUserType()))
                .map(User::getUsername)
                .collect(Collectors.toList());

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "getOnlineUsers");
        resultNode.put("timestamp", getTimestamp());

        ArrayNode resultArray = resultNode.putArray("result");
        if (!onlineUsers.isEmpty()) {
            onlineUsers.forEach(resultArray::add);
        } else {
            resultArray.add("No users online.");
        }
    }
}
