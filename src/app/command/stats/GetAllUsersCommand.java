package app.command.stats;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

/**
 * JAVADOC
 */
public class GetAllUsersCommand extends Command {

    /**
     * JAVADOC
     */
    public GetAllUsersCommand(final String username,
                              final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "getAllUsers");
        resultNode.put("timestamp", getTimestamp());

        List<String> normalUsers = new ArrayList<>();
        List<String> artists = new ArrayList<>();
        List<String> hosts = new ArrayList<>();

        for (User user : library.getUsers()) {
            if (user.isHost()) {
                hosts.add(user.getUsername());
            } else if (user.isArtist()) {
                artists.add(user.getUsername());
            } else {
                normalUsers.add(user.getUsername());
            }
        }

        List<String> orderedUsers = new ArrayList<>();
        orderedUsers.addAll(normalUsers);
        orderedUsers.addAll(artists);
        orderedUsers.addAll(hosts);

        ArrayNode resultArray = resultNode.putArray("result");
        for (String username : orderedUsers) {
            resultArray.add(username);
        }
    }

}
