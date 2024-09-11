package app.command;

import com.fasterxml.jackson.databind.node.ArrayNode;
import app.audio.collection.Library;

public class UnknownCommand extends Command {

    public UnknownCommand(String username, Integer timestamp) {
        super(username, timestamp);
    }

    @Override
    public void execute(ArrayNode output, Library library) {
    }
}
