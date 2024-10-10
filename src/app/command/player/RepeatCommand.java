package app.command.player;

import app.entities.Command;
import app.entities.Player;
import app.entities.audio.collection.Library;
import app.entities.audio.file.AudioFile;
import app.entities.audio.collection.Playlist;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RepeatCommand extends Command {
    public RepeatCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = Player.getInstance();

        if (!player.isLoaded()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("message", "Please load a source before setting the repeat status.");
            return;
        }

        AudioFile currentAudio = player.getCurrentAudio();
        String newRepeatMode = "";

        if (currentAudio instanceof Playlist) {
            switch (player.getRepeatState()) {
                case 0:
                    player.setRepeatState(1);
                    newRepeatMode = "repeat all";
                    break;
                case 1:
                    player.setRepeatState(2);
                    newRepeatMode = "repeat current song";
                    break;
                case 2:
                    player.setRepeatState(0);
                    newRepeatMode = "no repeat";
                    break;
            }
        } else {
            switch (player.getRepeatState()) {
                case 0:
                    player.setRepeatState(1);
                    newRepeatMode = "repeat once";
                    break;
                case 1:
                    player.setRepeatState(2);
                    newRepeatMode = "repeat infinite";
                    break;
                case 2:
                    player.setRepeatState(0);
                    newRepeatMode = "no repeat";
                    break;
            }
        }

        ObjectNode resultNode = output.addObject();
        int currentTimestamp = getTimestamp();
        player.updateRemainingTime(currentTimestamp);
        resultNode.put("command", "repeat");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", currentTimestamp);

        resultNode.put("message", "Repeat mode changed to " + newRepeatMode + ".");
    }
}
