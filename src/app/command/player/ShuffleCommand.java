package app.command.player;

import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * JAVADOC
 */
public class ShuffleCommand extends Command {
    private final int seed;

    /**
     * JAVADOC
     */
    public ShuffleCommand(final String username, final Integer timestamp, final Integer seed) {
        super(username, timestamp);
        this.seed = seed;
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = PlayerManager.getPlayer(getUsername());
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "shuffle");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        int currentTimestamp = getTimestamp();

        player.updateRemainingTime(currentTimestamp);
        if (player.getRemainingTime() <= 0 || !player.isLoaded()) {
            player.setLoaded(false);
            player.setShuffleActive(false);
            resultNode.put("message", "Please load a source before using the shuffle function.");
            return;
        }

        Playlist currentPlaylist = player.getCurrentPlaylist();

        if (currentPlaylist == null || currentPlaylist.getSongs().isEmpty()) {
            player.setShuffleActive(false);
            resultNode.put("message", "The loaded source is not a playlist.");
            return;
        }

        List<Song> songs = currentPlaylist.getSongs();

        if (player.isShuffleActive()) {
            player.setShuffleActive(false);

            resultNode.put("message", "Shuffle function deactivated successfully.");
        } else {
            ArrayList<Integer> indices = new ArrayList<>();
            for (int i = 0; i < songs.size(); i++) {
                indices.add(i);
            }
            Random random = new Random(seed);
            Collections.shuffle(indices, random);

            player.setShuffleIndices(indices);
            player.setShuffleActive(true);
            resultNode.put("message", "Shuffle function activated successfully.");
        }
    }

}
