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
public class PrevCommand extends Command {

    /**
     * JAVADOC
     */
    public PrevCommand(final String username, final Integer timestamp) {
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
            resultNode.put("command", "prev");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }

        Player player = PlayerManager.getPlayer(getUsername());

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
        int currentAudioDuration = player.getCurrentAudio().getDuration();

        if (remainingTime != player.getCurrentAudio().getDuration()) {
            player.setRemainingTime(player.getCurrentAudio().getDuration());
            player.setLastUpdateTimestamp(getTimestamp());
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
            return;
        }

        if (remainingTime == currentAudioDuration && !player.isShuffleActive()) {
            if (currentIndex > 0) {
                player.nextPrevPlaylistLoad(currentIndex - 1);
                player.setRemainingTime(player.getCurrentAudio().getDuration());
                player.setLastUpdateTimestamp(getTimestamp());
                addMessage(output, "Returned to previous track successfully. The current track is "
                        + player.getCurrentAudio().getName() + ".");
                return;
            } else {
                addMessage(output, "Returned to previous track successfully. The current track is "
                        + player.getCurrentAudio().getName() + ".");
                return;
            }
        } else if (remainingTime == currentAudioDuration && player.isShuffleActive()) {
            int currentShuffleIndex = player.getShuffleIndices().indexOf(currentIndex);
            if (currentShuffleIndex > 0) {
                int prevIndex = player.getShuffleIndices().get(currentShuffleIndex - 1);
                player.nextPrevPlaylistLoad(prevIndex);
            } else {
                player.nextPrevPlaylistLoad(currentIndex);
            }
            player.setRemainingTime(player.getCurrentAudio().getDuration());
            player.setLastUpdateTimestamp(getTimestamp());
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
            return;
        }

        if (player.isShuffleActive() && player.getRepeatState() == 1) {
            player.nextPrevPlaylistLoad(currentIndex);
            player.setRemainingTime(currentAudioDuration);
            player.setLastUpdateTimestamp(getTimestamp());
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
        } else if (player.isShuffleActive() && player.getRepeatState() == 2) {
            player.nextPrevPlaylistLoad(currentIndex);
            player.setRemainingTime(currentAudioDuration);
            player.setLastUpdateTimestamp(getTimestamp());
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
        } else if (player.isShuffleActive()) {
            int currentShuffleIndex = player.getShuffleIndices().indexOf(currentIndex);
            if (currentShuffleIndex > 0) {
                int prevIndex = player.getShuffleIndices().get(currentShuffleIndex - 1);
                player.nextPrevPlaylistLoad(prevIndex);
            } else {
                player.nextPrevPlaylistLoad(currentIndex);
            }
            player.setRemainingTime(currentAudioDuration);
            player.setLastUpdateTimestamp(getTimestamp());
            addMessage(output, "Returned to previous track successfully. The current track is "
                    + player.getCurrentAudio().getName() + ".");
        } else {
            if (remainingTime < currentAudioDuration - 1) {
                player.nextPrevPlaylistLoad(currentIndex);
            } else if (currentIndex > 0) {
                player.nextPrevPlaylistLoad(currentIndex - 1);
            } else {
                player.nextPrevPlaylistLoad(0);
            }
            player.setLastUpdateTimestamp(getTimestamp());
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
