package app.command.user.host;

import app.entities.Command;
import app.entities.audio.collection.Library;
import app.entities.User;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Comandă pentru eliminarea unui anunț.
 */
public class RemoveAnnouncementCommand extends Command {
    private final String announcementName;

    /**
     * Constructor pentru RemoveAnnouncementCommand.
     */
    public RemoveAnnouncementCommand(final String username,
                                     final Integer timestamp,
                                     final String announcementName) {
        super(username, timestamp);
        this.announcementName = announcementName;
    }

    /**
     * Execută comanda de eliminare a unui anunț.
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        ObjectNode response = output.objectNode();

        // Verifică dacă utilizatorul există
        User user = library.getUser(getUsername());
        if (user == null) {
            response.put("command", "removeAnnouncement");
            response.put("user", getUsername());
            response.put("timestamp", getTimestamp());
            response.put("message", "The username " + getUsername() + " doesn't exist.");
            output.add(response);
            return;
        }

        // Verifică dacă utilizatorul este un host
        if (user.getType() != User.UserType.HOST) {
            response.put("command", "removeAnnouncement");
            response.put("user", getUsername());
            response.put("timestamp", getTimestamp());
            response.put("message", getUsername() + " is not a host.");
            output.add(response);
            return;
        }

        // Verifică dacă host-ul are anunțul specificat și îl elimină
        if (!user.removeAnnouncement(announcementName)) {
            response.put("command", "removeAnnouncement");
            response.put("user", getUsername());
            response.put("timestamp", getTimestamp());
            response.put("message", getUsername() + " has no announcement with the given name.");
            output.add(response);
            return;
        }

        // Anunț șters cu succes
        response.put("command", "removeAnnouncement");
        response.put("user", getUsername());
        response.put("timestamp", getTimestamp());
        response.put("message", getUsername() + " has successfully deleted the announcement.");
        output.add(response);
    }
}
