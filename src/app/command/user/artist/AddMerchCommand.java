package app.command.user.artist;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;
/**
 * JAVADOC
 */
public class AddMerchCommand extends Command {

    private String merchName;
    private String merchDescription;
    private int merchPrice;
    /**
     * JAVADOC
     */
    public AddMerchCommand(final String username,
                           final Integer timestamp,
                           final String merchName,
                           final String merchDescription,
                           final int merchPrice) {
        super(username, timestamp);
        this.merchName = merchName;
        this.merchDescription = merchDescription;
        this.merchPrice = merchPrice;
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
            boolean merchExists = false;
            for (List<String> merch : user.getMerch()) {
                if (merch.get(0).equals(this.merchName)) {
                    merchExists = true;
                    break;
                }
            }

            if (merchExists) {
                resultArray.add(getUsername() + " has merchandise with the same name.");
            } else if (this.merchPrice < 0) {
                resultArray.add("Price for merchandise can not be negative.");
            } else {
                user.addMerch(this.merchName, this.merchDescription, this.merchPrice);
                resultArray.add(getUsername() + " has added new merchandise successfully.");
            }
        }

        if (resultArray.size() == 1) {
            output.add(output.objectNode()
                    .put("command", "addMerch")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .put("message", resultArray.get(0).asText()));
        } else {
            output.add(output.objectNode()
                    .put("command", "addMerch")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .set("result", resultArray));
        }
    }
}
