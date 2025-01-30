package app.command.stats;

import app.entities.Command;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * JAVADOC
 */
public class GetTop5AlbumsCommand extends Command {

    /**
     * JAVADOC
     */
    public GetTop5AlbumsCommand(final String username,
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
