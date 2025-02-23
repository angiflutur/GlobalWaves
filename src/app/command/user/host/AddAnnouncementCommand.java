package app.command.user.host;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.List;

/**
 * This class handles the command for adding an announcement.
 */
public class AddAnnouncementCommand extends Command {

    private String announcementName;
    private String announcementDescription;

    /**
     * Constructor for the addAnnouncement command.
     */
    public AddAnnouncementCommand(final String username,
                                  final Integer timestamp,
                                  final String announcementName,
                                  final String announcementDescription) {
        super(username, timestamp);
        this.announcementName = announcementName;
        this.announcementDescription = announcementDescription;
    }

    /**
     * Executes the command to add a new announcement for the host.
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());
        ArrayNode resultArray = output.objectNode().putArray("result");

        if (user == null) {
            resultArray.add("The username " + getUsername() + " doesn't exist.");
        } else if (user.getType() != User.UserType.HOST) {
            resultArray.add(getUsername() + " is not a host.");
        } else {
            boolean announcementExists = false;
            for (List<String> announcement : user.getEvents()) {
                if (announcement.get(0).equals(this.announcementName)) {
                    announcementExists = true;
                    break;
                }
            }

            if (announcementExists) {
                resultArray.add(getUsername() + " has already added an announcement with this name.");
            } else {
                user.addEvent(this.announcementName, getTimestamp().toString(), this.announcementDescription);
                resultArray.add(getUsername() + " has successfully added new announcement.");
            }
        }

        if (resultArray.size() == 1) {
            output.add(output.objectNode()
                    .put("command", "addAnnouncement")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .put("message", resultArray.get(0).asText()));
        } else {
            output.add(output.objectNode()
                    .put("command", "addAnnouncement")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .set("result", resultArray));
        }
    }
}
