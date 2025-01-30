package app.command.user.artist;

import app.entities.Command;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * JAVADOC
 */
public class RemoveAlbumCommand extends Command {

    /**
     * JAVADOC
     */
    public RemoveAlbumCommand(final String username,
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
