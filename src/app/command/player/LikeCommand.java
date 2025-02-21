package app.command.player;

import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.file.AudioFile;
import app.entities.audio.file.Song;
import app.entities.Command;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class LikeCommand extends Command {

    /**
     * JAVADOC
     */
    public LikeCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {

        Player player = PlayerManager.getPlayer(getUsername());
        player.updateRemainingTime(getTimestamp());
        AudioFile currentAudio = player.getCurrentAudio();
        User user = library.getUser(getUsername());

        if (user == null || !user.isOnline()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "like");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }
        if (!player.isLoaded() || !(currentAudio instanceof Song)) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "like");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "Please load a source before liking or unliking.");
            return;
        }

        if (user == null) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "like");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", "User not found.");
            return;
        }

        Song currentSong = (Song) currentAudio;
        boolean liked = user.getLikedSongs().contains(currentSong);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "like");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        if (liked) {
            user.unlikeSong(currentSong);
            currentSong.decrementLikeCount();
            resultNode.put("message", "Unlike registered successfully.");
        } else {
            user.likeSong(currentSong);
            currentSong.incrementLikeCount();
            resultNode.put("message", "Like registered successfully.");
        }
    }
}
