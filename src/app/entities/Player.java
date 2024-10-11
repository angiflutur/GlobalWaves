package app.entities;

import app.entities.audio.collection.Podcast;
import app.entities.audio.file.AudioFile;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.PodcastEpisode;
import app.entities.audio.file.Song;

/**
 * Player class to manage audio playback and state.
 */
public class Player {
    private AudioFile currentAudio;
    private Playlist currentPlaylist;
    private boolean isPaused;
    private boolean isLoaded;
    private int remainingTime;
    private int lastUpdateTimestamp;
    private static Player instance = new Player();
    private int currentIndex;
    private int repeatState;

    /**
     * Constructor for Player class.
     */
    public Player() {
        this.isPaused = false;
        this.isLoaded = false;
        this.remainingTime = 0;
        this.lastUpdateTimestamp = 0;
        this.currentPlaylist = null;
        this.repeatState = 0;
        this.currentIndex = 0;
    }

    /**
     * Singleton instance retrieval.
     */
    public static Player getInstance() {
        return instance;
    }

    /**
     * Load audio file or playlist into the player.
     */
    public void loadAudio(final AudioFile audio, final int timestamp) {
        if (audio == null) {
            throw new IllegalArgumentException("AudioFile cannot be null");
        }

        this.currentAudio = audio;
        this.currentPlaylist = (audio instanceof Playlist) ? (Playlist) audio : null;
        this.currentIndex = 0;

        if (currentPlaylist != null) {
            if (currentPlaylist.getSongs().size() > 0
                    && currentIndex < currentPlaylist.getSongs().size()) {
                this.currentAudio = currentPlaylist.getSongs().get(currentIndex);
                this.remainingTime = this.currentAudio.getDuration();
            } else {
                this.currentAudio = null;
                this.remainingTime = 0;
            }
        } else {
            if (audio instanceof Podcast) {
                Podcast podcast = (Podcast) audio;
                if (podcast.getCurrentEpisodeIndex() >= 0
                        && podcast.getCurrentEpisodeIndex() < podcast.getEpisodes().size()) {
                    this.remainingTime = podcast.getCurrentEpisodeRemainingTime();
                } else {
                    this.remainingTime = 0;
                }
            } else {
                this.remainingTime = audio.getDuration();
            }
        }

        this.isLoaded = true;
        this.isPaused = false;
        this.lastUpdateTimestamp = timestamp;
    }


    /**
     * Play the audio.
     */
    public void play(final int currentTimestamp) {
        if (isPaused) {
            updateRemainingTime(currentTimestamp);
            isPaused = false;
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * Pause the audio.
     */
    public void pause(final int currentTimestamp) {
        if (!isPaused) {
            updateRemainingTime(currentTimestamp);
            isPaused = true;
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * Update the remaining time based on the current timestamp.
     */
    public void updateRemainingTime(final int currentTimestamp) {
        if (!isPaused && currentAudio != null) {

            int timeElapsed = currentTimestamp - lastUpdateTimestamp;

            if (currentPlaylist != null) {
                updatePlaylistRemainingTime(timeElapsed);
            } else if (currentAudio instanceof Podcast) {
                updatePodcastRemainingTime(timeElapsed);
            } else if (currentAudio instanceof Song) {
                updateSongRemainingTime(timeElapsed);
            }

            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * Update remaining time for the current Podcast.
     */
    private void updatePodcastRemainingTime(final int timeElapsed) {
        Podcast podcast = (Podcast) currentAudio;
        int currentEpisodeIndex = podcast.getCurrentEpisodeIndex();

        if (currentEpisodeIndex >= 0 && currentEpisodeIndex < podcast.getEpisodes().size()) {
            PodcastEpisode currentEpisode = podcast.getEpisodes().get(currentEpisodeIndex);
            int currentEpisodeRemainingTime = podcast.getCurrentEpisodeRemainingTime();

            currentEpisodeRemainingTime -= timeElapsed;

            if (currentEpisodeRemainingTime <= 0) {
                if (currentEpisodeIndex < podcast.getEpisodes().size() - 1) {
                    podcast.setCurrentEpisodeIndex(currentEpisodeIndex + 1);
                    PodcastEpisode nextEpisode
                            = podcast.getEpisodes().get(podcast.getCurrentEpisodeIndex());

                    podcast.setCurrentEpisodeRemainingTime(nextEpisode.getDuration()
                            + currentEpisodeRemainingTime);
                } else {
                    currentAudio = null;
                    isLoaded = false;
                    podcast.setCurrentEpisodeRemainingTime(0);
                }
            } else {
                podcast.setCurrentEpisodeRemainingTime(currentEpisodeRemainingTime);
            }

            this.remainingTime = podcast.getCurrentEpisodeRemainingTime();
        }
    }

    /**
     * Update remaining time for the current Song.
     */
    private void updateSongRemainingTime(final int timeElapsed) {
        remainingTime -= timeElapsed;

        if (remainingTime <= 0) {
            if (repeatState == 1) {
                remainingTime = currentAudio.getDuration() + remainingTime;
                repeatState = 0;
            } else if (repeatState == 2) {
                remainingTime = currentAudio.getDuration()
                        - (-remainingTime % currentAudio.getDuration());
            } else {
                if (currentPlaylist != null) {
                    currentIndex++;
                    if (currentIndex < currentPlaylist.getSongs().size()) {
                        currentAudio = currentPlaylist.getSongs().get(currentIndex);
                        remainingTime = currentAudio.getDuration() + remainingTime;
                    } else {
                        currentAudio = null;
                        remainingTime = 0;
                        isLoaded = false;
                        isPaused = true;
                    }
                } else {
                    remainingTime = 0;
                    isPaused = true;
                }
            }
        }
    }

    /**
     * Update remaining time for the current Playlist.
     */
    /**
     * Update remaining time for the current Playlist.
     */
    private void updatePlaylistRemainingTime(final int timeElapsed) {
        remainingTime -= timeElapsed;

        if (remainingTime <= 0) {
            if (repeatState == 1) {
                currentAudio = currentPlaylist.getSongs().get(currentIndex);
                remainingTime += timeElapsed;
            } else if (repeatState == 2) {
                remainingTime = currentAudio.getDuration() + remainingTime;
            } else {
                if (currentPlaylist != null) {
                    currentIndex++;
                    if (currentIndex < currentPlaylist.getSongs().size()) {
                        currentAudio = currentPlaylist.getSongs().get(currentIndex);
                        remainingTime = currentAudio.getDuration() + remainingTime;
                    } else {
                        currentAudio = null;
                        remainingTime = 0;
                        isLoaded = false;
                        isPaused = true;
                    }
                }
            }
        }
    }

    /**
     * Get the remaining time.
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Check if the player is paused.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Check if the player is loaded with audio.
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * Set the loaded state.
     */
    public void setLoaded(final boolean loaded) {
        isLoaded = loaded;
    }

    /**
     * Set the paused state.
     */
    public void setPaused(final boolean paused) {
        isPaused = paused;
    }

    /**
     * Get the current audio being played.
     */
    public AudioFile getCurrentAudio() {
        return currentAudio;
    }

    /**
     * Get the current playlist being played.
     */
    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    /**
     * Skip to the previous track in the current playlist.
     */
    public void previous() {
        if (currentIndex > 0) {
            currentIndex--;
            currentAudio = currentPlaylist.getSongs().get(currentIndex);
            remainingTime = currentAudio.getDuration();
        }
    }

    /**
     * Skip to the next track in the current playlist.
     */
    public void next() {
        if (currentPlaylist != null && currentIndex < currentPlaylist.getSongs().size() - 1) {
            currentIndex++;
            currentAudio = currentPlaylist.getSongs().get(currentIndex);
            remainingTime = currentAudio.getDuration();
        }
    }

    /**
     * JAVADOC
     */
    public String getRepeatStatus() {
        if (currentPlaylist instanceof Playlist) {
            switch (repeatState) {
                case 1:
                    return "Repeat All";
                case 2:
                    return "Repeat Current Song";
                default:
                    return "No Repeat";
            }
        } else {
            switch (repeatState) {
                case 1:
                    return "Repeat Once";
                case 2:
                    return "Repeat Infinite";
                default:
                    return "No Repeat";
            }
        }
    }

    /**
     * JAVADOC
     */
    public int getRepeatState() {
        return repeatState;
    }

    /**
     * JAVADOC
     */
    public void setRepeatState(final int repeatState) {
        this.repeatState = repeatState;
    }
}
