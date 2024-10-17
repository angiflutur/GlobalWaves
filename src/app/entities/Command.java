package app.entities;

import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
/**
 * JAVADOC
 */
public abstract class Command {
    private static String username;
    private static Integer timestamp;

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
    public static String getUsername() {
        return username;
    }

    /**
     * JAVADOC
     */
    public static Integer getTimestamp() {
        return timestamp;
    }

    /**
     * JAVADOC
     */
    public abstract void execute(ArrayNode output,
                                 Library library);
}
