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
public class NextCommand extends Command {
    /**
     * JAVADOC
     */
    public NextCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = Player.getInstance();

        if (!player.isLoaded()) {
            addMessage(output, "Please load a source before skipping to the next track.");
            return;
        }

        player.updateRemainingTime(getTimestamp());
        player.setPaused(false);

        if (player.getCurrentPlaylist() != null) {
            handleNextTrackInPlaylist(output, player);
        } else if (player.getCurrentAudio() instanceof Podcast) {
            handleNextEpisodeInPodcast(output, player);
        } else {
            addMessage(output, "Please load a playlist or a podcast before skipping.");
        }
    }

    /**
     * JAVADOC
     */
    private void handleNextTrackInPlaylist(final ArrayNode output, final Player player) {
        int nextIndex = player.getCurrentIndex() + 1;

        if (nextIndex < player.getCurrentPlaylist().getSongs().size()) {
            player.nextPrevPlaylistLoad(nextIndex);
            addMessage(output, "Skipped to next track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
        } else {
            if (player.getRepeatState() == 0) {
                player.setPaused(true);
                player.setLoaded(false);
                addMessage(output, "Reached the end of the playlist. The player is paused.");
            } else {
                player.nextPrevPlaylistLoad(0);
                addMessage(output, "Skipped to next track successfully. The current track is "
                        + player.getCurrentAudio().getName() + ".");
            }
        }
    }

    /**
     * JAVADOC
     */
    private void handleNextEpisodeInPodcast(final ArrayNode output, final Player player) {
        Podcast podcast = (Podcast) player.getCurrentAudio();
        int currentEpisodeIndex = podcast.getCurrentEpisodeIndex();

        if (currentEpisodeIndex < podcast.getEpisodes().size() - 1) {
            podcast.setCurrentEpisodeIndex(currentEpisodeIndex + 1);
            PodcastEpisode nextEpisode = podcast.getEpisodes().
                    get(podcast.getCurrentEpisodeIndex());
            podcast.setCurrentEpisodeRemainingTime(nextEpisode.getDuration());
            addMessage(output, "Skipped to next track successfully. The current track is "
                    + nextEpisode.getName() + ".");
        } else {
            player.setPaused(true);
            player.setLoaded(false);
            addMessage(output, "Reached the end of the podcast. The player is paused.");
        }
    }

    /**
     * JAVADOC
     */
    private void addMessage(final ArrayNode output, final String message) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "next");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", message);
    }
}
