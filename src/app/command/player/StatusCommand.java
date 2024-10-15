package app.command.player;

import app.entities.Player;
import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.audio.collection.Podcast;
import app.entities.audio.file.PodcastEpisode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class StatusCommand extends Command {
    /**
     * JAVADOC
     */
    public StatusCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = Player.getInstance();
        int currentTimestamp = getTimestamp();
        player.updateRemainingTime(currentTimestamp);

        if (player.getRemainingTime() <= 0) {
            player.setPaused(true);
            player.setLoaded(false);
        }

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "status");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", currentTimestamp);

        ObjectNode statsNode = resultNode.putObject("stats");

        int remainingTime = player.getRemainingTime();

        if (player.isLoaded()) {
            if (player.getCurrentAudio() != null) {
                String name = "";
                if (player.getCurrentAudio() instanceof Podcast) {
                    Podcast podcast = (Podcast) player.getCurrentAudio();
                    int currentEpisodeIndex = podcast.getCurrentEpisodeIndex();

                    if (currentEpisodeIndex >= 0
                            && currentEpisodeIndex < podcast.getEpisodes().size()) {
                        PodcastEpisode currentEpisode
                                = podcast.getEpisodes().get(currentEpisodeIndex);
                        name = (remainingTime > 0 && currentEpisode.getName() != null)
                                ? currentEpisode.getName() : "";
                    }
                } else {
                    name = (remainingTime > 0
                            && player.getCurrentAudio().getName() != null)
                            ? player.getCurrentAudio().getName() : "";
                }
                statsNode.put("name", name);
            } else {
                statsNode.put("name", "");
            }

            statsNode.put("remainedTime", remainingTime > 0 ? remainingTime : 0);
            statsNode.put("repeat", player.getRepeatStatus());
            statsNode.put("shuffle", player.isShuffleActive());
            statsNode.put("paused", player.isPaused());

        } else {
            statsNode.put("name", "");
            statsNode.put("remainedTime", 0);
            statsNode.put("repeat", "No Repeat");
            statsNode.put("shuffle", false);
            statsNode.put("paused", player.isPaused());
        }
    }
}
