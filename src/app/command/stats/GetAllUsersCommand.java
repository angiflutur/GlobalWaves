package app.command.stats;

import app.entities.Command;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

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
    }
}
