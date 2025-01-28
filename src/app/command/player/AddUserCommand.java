package app.command.player;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class AddUserCommand extends Command {

    private String userType;
    private String username;
    private Integer age;
    private String city;

    /**
     * JAVADOC
     */
    public AddUserCommand(final String username, final Integer timestamp,
                          final String userType, final String usernameToAdd,
                          final Integer age, final String city) {
        super(username, timestamp);
        this.userType = userType;
        this.username = usernameToAdd;
        this.age = age;
        this.city = city;
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User existingUser = library.getUser(username);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "addUser");
        resultNode.put("user", username);
        resultNode.put("timestamp", getTimestamp());

        if (existingUser != null) {
            resultNode.put("message", "The username " + username + " is already taken.");
            return;
        }

        boolean isArtist = "artist".equals(userType);

        User newUser = new User(username, age, city, isArtist);
        library.addUser(newUser);

        resultNode.put("message", "The username " + username + " has been added successfully.");
    }
}
