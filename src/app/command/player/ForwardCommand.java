package app.command.player;

import app.entities.Command;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * JAVADOC
 */
public class ForwardCommand extends Command {
    /**
     * JAVADOC
     */
    public ForwardCommand(final String username,
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
