package app.command.player;

import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
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
        if (player.getRepeatState() == 2) {
            player.setRemainingTime(player.getCurrentAudio().getDuration());
            addMessage(output, "Skipped to next track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
            return;
        }

        if (player.isShuffleActive()) {
            int currentShuffleIndex = player.getShuffleIndices().indexOf(player.getCurrentIndex());

            if (currentShuffleIndex != -1
                    && currentShuffleIndex < player.getShuffleIndices().size() - 1) {
                int nextIndex = player.getShuffleIndices().get(currentShuffleIndex + 1);
                player.nextPrevPlaylistLoad(nextIndex);
                player.setRemainingTime(player.getCurrentAudio().getDuration());
                player.setLastUpdateTimestamp(getTimestamp());
                addMessage(output, "Skipped to next track successfully. The current track is "
                        + player.getCurrentAudio().getName() + ".");
            } else {
                if (player.getRepeatState() == 0) {
                    player.setPaused(true);
                    player.setLoaded(false);
                    addMessage(output, "Please load a source before skipping to the next track.");
                } else {
                    player.nextPrevPlaylistLoad(player.getShuffleIndices().get(0));
                    player.setRemainingTime(player.getCurrentAudio().getDuration());
                    player.setLastUpdateTimestamp(getTimestamp());
                    addMessage(output, "Skipped to next track successfully. The current track is "
                            + player.getCurrentAudio().getName() + ".");
                }
            }
        } else {
            int nextIndex = player.getCurrentIndex() + 1;

            if (nextIndex < player.getCurrentPlaylist().getSongs().size()) {
                player.nextPrevPlaylistLoad(nextIndex);
                player.setRemainingTime(player.getCurrentAudio().getDuration());
                player.setLastUpdateTimestamp(getTimestamp());
                addMessage(output, "Skipped to next track successfully. The current track is "
                        + player.getCurrentAudio().getName() + ".");
            } else {
                if (player.getRepeatState() == 0) {
                    player.setPaused(true);
                    player.setLoaded(false);
                    addMessage(output, "Please load a source before skipping to the next track.");
                } else {
                    player.nextPrevPlaylistLoad(0);
                    player.setRemainingTime(player.getCurrentAudio().getDuration());
                    player.setLastUpdateTimestamp(getTimestamp());
                    addMessage(output, "Skipped to next track successfully. The current track is "
                            + player.getCurrentAudio().getName() + ".");
                }
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
            player.setLastUpdateTimestamp(getTimestamp());
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
