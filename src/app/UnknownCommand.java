package app;

import app.entities.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
import app.entities.audio.collection.Library;

/**
 * JAVADOC
 */
public class UnknownCommand extends Command {

    /**
     * JAVADOC
     */
    public UnknownCommand(final String username,
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
