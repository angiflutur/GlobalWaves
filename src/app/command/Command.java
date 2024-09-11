package app.command;

import app.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

public abstract class Command {
    private String username;
    private Integer timestamp;

    public Command(String username, Integer timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public abstract void execute(ArrayNode output, Library library);
}
