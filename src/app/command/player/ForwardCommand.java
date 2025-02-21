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
public class ForwardCommand extends Command {
    private static final int NUMBER_OF_SECONDS = 90;

    public ForwardCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null || !user.isOnline()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "forward");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }
        Player player = PlayerManager.getPlayer(getUsername());

        if (!player.isLoaded()) {
            addMessage(output, "Please load a source before attempting to forward.");
            return;
        }

        if (!(player.getCurrentAudio() instanceof Podcast)) {
            addMessage(output, "The loaded source is not a podcast.");
            return;
        }

        player.updateRemainingTime(getTimestamp());
        Podcast podcast = (Podcast) player.getCurrentAudio();
        int remainingTime = podcast.getCurrentEpisodeRemainingTime();

        if (remainingTime < NUMBER_OF_SECONDS) {
            if (podcast.getCurrentEpisodeIndex() < podcast.getEpisodes().size() - 1) {
                podcast.setCurrentEpisodeIndex(podcast.getCurrentEpisodeIndex() + 1);
                PodcastEpisode nextEpisode =
                        podcast.getEpisodes().get(podcast.getCurrentEpisodeIndex());
                podcast.setCurrentEpisodeRemainingTime(nextEpisode.getDuration());
                addMessage(output, "Skipped forward successfully.");
            } else {
                addMessage(output, "No more episodes available.");
            }
        } else {
            podcast.setCurrentEpisodeRemainingTime(remainingTime - NUMBER_OF_SECONDS);
            addMessage(output, "Skipped forward successfully.");
        }
    }

    /**
     * JAVADOC
     */
    private void addMessage(final ArrayNode output, final String message) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "forward");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", message);
    }
}
