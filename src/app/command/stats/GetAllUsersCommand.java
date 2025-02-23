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
public class GetAllUsersCommand extends Command {

    /**
     * JAVADOC
     */
    public GetAllUsersCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        List<User> users = library.getUsers();

        List<String> sortedUsers = users.stream()
                .sorted((u1, u2) -> Integer.compare(getUserTypePriority(u1),
                        getUserTypePriority(u2)))
                .map(User::getUsername)
                .collect(Collectors.toList());

        ObjectNode resultNode = output.objectNode();
        resultNode.put("command", "getAllUsers");
        resultNode.put("timestamp", getTimestamp());

        ArrayNode usersArray = resultNode.putArray("result");
        sortedUsers.forEach(usersArray::add);

        output.add(resultNode);
    }

    /**
     * JAVADOC
     */
    private int getUserTypePriority(final User user) {
        if (user.getType() == null) {
            return 0;
        }
        switch (user.getType()) {
            case USER:
                return 0;
            case ARTIST:
                return 1;
            case HOST:
                return 2;
            default: break;
        }
        return 0;
    }
}
