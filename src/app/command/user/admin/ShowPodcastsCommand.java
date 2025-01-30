package app.command.user.admin;

import app.entities.Command;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * JAVADOC
 */
public class ShowPodcastsCommand extends Command {

    /**
     * JAVADOC
     */
    public ShowPodcastsCommand(final String username,
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
