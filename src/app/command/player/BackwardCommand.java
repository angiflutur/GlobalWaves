package app.command.player;

import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Library;

import app.entities.audio.collection.Podcast;
import app.entities.audio.file.PodcastEpisode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class BackwardCommand extends Command {
    private static final int NUMBER_OF_SECONDS = 90;
    public BackwardCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = PlayerManager.getPlayer(getUsername());
        User user = library.getUser(getUsername());

        if (user == null || !user.isConnectionStatus()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }
        if (!player.isLoaded()) {
            addMessage(output, "Please load a source before rewinding.");
            return;
        }

        if (!(player.getCurrentAudio() instanceof Podcast)) {
            addMessage(output, "The loaded source is not a podcast.");
            return;
        }

        player.updateRemainingTime(getTimestamp());
        Podcast podcast = (Podcast) player.getCurrentAudio();
        PodcastEpisode currentEpisode = podcast.getEpisodes().get(podcast.getCurrentEpisodeIndex());
        int remainingTime = podcast.getCurrentEpisodeRemainingTime();

        if (remainingTime < NUMBER_OF_SECONDS) {
            podcast.setCurrentEpisodeRemainingTime(currentEpisode.getDuration());
            addMessage(output, "Rewound successfully.");
        } else {
            podcast.setCurrentEpisodeRemainingTime(remainingTime + NUMBER_OF_SECONDS);
            addMessage(output, "Rewound successfully.");
        }
    }

    /**
     * JAVADOC
     */
    private void addMessage(final ArrayNode output, final String message) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "backward");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", message);
    }
}
