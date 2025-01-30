package app.command.user.host;

import app.entities.Command;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * JAVADOC
 */
public class AddAnnouncementCommand extends Command {

    /**
     * JAVADOC
     */
    public AddAnnouncementCommand(final String username,
                                  final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
    }
}
