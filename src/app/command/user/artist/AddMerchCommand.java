package app.command.user.artist;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
/**
 * JAVADOC
 */
public class AddMerchCommand extends Command {
    private final String merchName;
    private final String description;
    private final int price;
    /**
     * JAVADOC
     */
    public AddMerchCommand(final String username, final Integer timestamp,
                           final String merchName, final String description,
                           final int price) {
        super(username, timestamp);
        this.merchName = merchName;
        this.description = description;
        this.price = price;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output,
                        final Library library) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "addMerch");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        User user = library.getUser(getUsername());

        if (user == null) {
            resultNode.put("message", "The username " + getUsername() + " doesn't exist.");
            return;
        }

        if (!user.isArtist()) {
            resultNode.put("message", getUsername() + " is not an artist.");
            return;
        }

        if (user.hasMerch(merchName)) {
            resultNode.put("message", getUsername() + " has merchandise with the same name.");
            return;
        }

        if (price < 0) {
            resultNode.put("message", "Price for merchandise can not be negative.");
            return;
        }

        user.addMerch(merchName, description, price);
        resultNode.put("message", getUsername() + " has added new merchandise successfully.");
    }
}
