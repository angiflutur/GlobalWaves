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
public class PrevCommand extends Command {

    public PrevCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = Player.getInstance();

        if (!player.isLoaded()) {
            addMessage(output, "Please load a source before returning to the previous track.");
            return;
        }

        player.updateRemainingTime(getTimestamp());
        player.setPaused(false);

        if (player.getCurrentPlaylist() != null) {
            handlePrevTrackInPlaylist(output, player);
        } else if (player.getCurrentAudio() instanceof Podcast) {
            handlePrevEpisodeInPodcast(output, player);
        } else {
            addMessage(output, "Please load a source before returning to the previous track.");
        }
    }

    /**
     * JAVADOC
     */
    private void handlePrevTrackInPlaylist(final ArrayNode output, final Player player) {
        int currentIndex = player.getCurrentIndex();
        int remainingTime = player.getRemainingTime();

        if (remainingTime < player.getCurrentAudio().getDuration() - 1) {
            player.nextPrevPlaylistLoad(currentIndex);
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
        } else if (remainingTime >= player.getCurrentAudio().getDuration() - 1
                && currentIndex > 0) {
            player.nextPrevPlaylistLoad(currentIndex - 1);
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
        } else {
            player.nextPrevPlaylistLoad(0);
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
        }
    }

    /**
     * JAVADOC
     */
    private void handlePrevEpisodeInPodcast(final ArrayNode output, final Player player) {
        Podcast podcast = (Podcast) player.getCurrentAudio();
        int currentEpisodeIndex = podcast.getCurrentEpisodeIndex();

        if (currentEpisodeIndex > 0) {
            podcast.setCurrentEpisodeIndex(currentEpisodeIndex - 1);
            PodcastEpisode prevEpisode = podcast.getEpisodes().
                    get(podcast.getCurrentEpisodeIndex());
            podcast.setCurrentEpisodeRemainingTime(prevEpisode.getDuration());
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + prevEpisode.getName() + ".");
        } else {
            addMessage(output, "This is the first episode of the podcast.");
        }
    }

    /**
     * JAVADOC
     */
    private void addMessage(final ArrayNode output, final String message) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "prev");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", message);
    }
}
