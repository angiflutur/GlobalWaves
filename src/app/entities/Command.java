package app.entities;

import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
/**
 * JAVADOC
 */
public abstract class Command {
    private String username;
    private Integer timestamp;
    /**
     * JAVADOC
     */
    public Command(final String username,
                   final Integer timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }
    /**
     * JAVADOC
     */
    public String getUsername() {
        return username;
    }
    /**
     * JAVADOC
     */
    public Integer getTimestamp() {
        return timestamp;
    }
    /**
     * JAVADOC
     */
    public abstract void execute(ArrayNode output,
                                 Library library);
}
