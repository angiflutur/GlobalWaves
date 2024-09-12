package app.command.playlist;

import app.entities.audio.collection.Library;
import app.entities.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
/**
 * JAVADOC
 */
public class SwitchVisibilityCommand extends Command {
    private Integer itemNumber;
    /**
     * JAVADOC
     */
    public SwitchVisibilityCommand(final String username,
                                   final Integer timestamp, final Integer itemNumber) {
        super(username, timestamp);
        this.itemNumber = itemNumber;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
    }
}
