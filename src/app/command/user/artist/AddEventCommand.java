package app.command.user.artist;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
/**
 * JAVADOC
 */
public class AddEventCommand extends Command {

    private String eventName;
    private String eventDescription;
    private String eventDate;
    /**
     * JAVADOC
     */
    public AddEventCommand(final String username,
                           final Integer timestamp,
                           final String eventName,
                           final String eventDescription,
                           final String eventDate) {
        super(username, timestamp);
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());
        ArrayNode resultArray = output.objectNode().putArray("result");

        if (user == null) {
            resultArray.add("The username " + getUsername() + " doesn't exist.");
        } else if (user.getType() != User.UserType.ARTIST) {
            resultArray.add(getUsername() + " is not an artist.");
        } else {
            boolean eventExists = false;
            for (List<String> event : user.getEvents()) {
                if (event.get(0).equals(this.eventName)) {
                    eventExists = true;
                    break;
                }
            }

            if (eventExists) {
                resultArray.add(getUsername() + " has another event with the same name.");
            } else {
                user.addEvent(this.eventName, this.eventDate);
                resultArray.add(getUsername() + " has added new event successfully.");
            }
        }

        if (resultArray.size() == 1) {
            output.add(output.objectNode()
                    .put("command", "addEvent")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .put("message", resultArray.get(0).asText()));
        } else {
            output.add(output.objectNode()
                    .put("command", "addEvent")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .set("result", resultArray));
        }
    }
}
