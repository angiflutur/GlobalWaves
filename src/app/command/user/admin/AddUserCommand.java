package app.command.user.admin;

import app.entities.Command;
import app.entities.audio.collection.Library;
import app.entities.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Command to add a new user to the system.
 */
public class AddUserCommand extends Command {
    private final String username;
    private final int age;
    private final String city;
    private final User.UserType type;

    public AddUserCommand(final String username, final Integer timestamp,
                          final User.UserType type,
                          final int age, final String city) {
        super(username, timestamp);
        this.username = username;
        this.age = age;
        this.city = city;
        this.type = type;
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode response = mapper.createObjectNode();
        response.put("command", "addUser");
        response.put("timestamp", getTimestamp());
        response.put("user", username);

        if (library.getUser(username) != null) {
            response.put("message", "The username " + username + " is already taken.");
        } else {
            User newUser = new User(username, age, city, type);
            library.addUser(newUser);
            response.put("message", "The username " + username + " has been added successfully.");
        }
        output.add(response);
    }
}
