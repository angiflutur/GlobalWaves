package app.command.player;

import app.command.searchBar.SelectCommand;
import app.entities.Player;
import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.audio.file.AudioFile;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
/**
 * JAVADOC
 */
public class LoadCommand extends Command {
    /**
     * JAVADOC
     */
    public LoadCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        AudioFile selectedAudio = SelectCommand.getSelectedAudio();

        if (selectedAudio == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "load");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Please select a source before attempting to load.");
            return;
        }

        if (library.getSongs().isEmpty() && library.getPodcasts().isEmpty()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "load");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "You can't load an empty audio collection!");
            return;
        }
        Player player = new Player();

        player.getInstance().loadAudio(selectedAudio, getTimestamp());

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "load");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", "Playback loaded successfully.");
    }
}
