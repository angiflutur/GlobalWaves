package app.entities;

import app.entities.audio.collection.Podcast;
import app.entities.audio.file.AudioFile;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.PodcastEpisode;

import java.util.Iterator;

/**
 * JAVADOC
 */
public class Player {
    private AudioFile currentAudio;
    private Playlist currentPlaylist;
    private Iterator<AudioFile> playlistIterator;
    private boolean isPaused;
    private boolean isLoaded;
    private int remainingTime;
    private int lastUpdateTimestamp;
    private static Player instance = new Player();

    /**
     * JAVADOC
     */
    public Player() {
        this.isPaused = false;
        this.isLoaded = false;
        this.remainingTime = 0;
        this.lastUpdateTimestamp = 0;
        this.currentPlaylist = null;
        this.playlistIterator = null;
    }

    /**
     * JAVADOC
     */
    public static Player getInstance() {
        return instance;
    }

    /**
     * JAVADOC
     */
    public void loadAudio(final AudioFile audio, final int timestamp) {
        if (audio == null) {
            throw new IllegalArgumentException("AudioFile cannot be null");
        }

        this.currentAudio = audio;
        this.currentPlaylist = null;
        this.isLoaded = true;
        this.isPaused = false;
        this.remainingTime = audio.getDuration();
        this.lastUpdateTimestamp = timestamp;
    }

    /**
     * JAVADOC
     */
    public void play(final int currentTimestamp) {
        if (isPaused) {
            updateRemainingTime(currentTimestamp);
            isPaused = false;
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * JAVADOC
     */
    public void pause(final int currentTimestamp) {
        if (!isPaused) {
            updateRemainingTime(currentTimestamp);
            isPaused = true;
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * JAVADOC
     */
    public void updateRemainingTime(final int currentTimestamp) {
        if (!isPaused && currentAudio != null) {
            int timeElapsed = currentTimestamp - lastUpdateTimestamp;
            remainingTime -= timeElapsed;

            if (remainingTime < 0) {
                remainingTime = 0;
            }

            if (currentAudio instanceof Podcast) {
                Podcast podcast = (Podcast) currentAudio;
                int currentEpisodeIndex = podcast.getCurrentEpisodeIndex();

                if (currentEpisodeIndex >= 0
                        && currentEpisodeIndex < podcast.getEpisodes().size()) {
                    PodcastEpisode currentEpisode = podcast.getEpisodes().get(currentEpisodeIndex);
                    remainingTime = Math.max(0, currentEpisode.getDuration() - timeElapsed);

                    if (remainingTime <= 0) {
                        if (currentEpisodeIndex < podcast.getEpisodes().size() - 1) {
                            podcast.setCurrentEpisodeIndex(currentEpisodeIndex + 1);
                            remainingTime =
                                    podcast.getEpisodes().
                                            get(currentEpisodeIndex + 1).getDuration();
                        } else {
                            currentAudio = null;
                            isLoaded = false;
                            remainingTime = 0;
                        }
                    }
                }
            } else {
                if (remainingTime <= 0) {
                    if (currentPlaylist != null && playlistIterator != null) {
                        if (playlistIterator.hasNext()) {
                            currentAudio = playlistIterator.next();
                            remainingTime = currentAudio.getDuration();
                        } else {
                            currentAudio = null;
                            isLoaded = false;
                            remainingTime = 0;
                        }
                    } else {
                        remainingTime = 0;
                        isPaused = true;
                    }
                }
            }

            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * JAVADOC
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * JAVADOC
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * JAVADOC
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * JAVADOC
     */
    public void setLoaded(final boolean loaded) {
        isLoaded = loaded;
    }

    /**
     * JAVADOC
     */
    public AudioFile getCurrentAudio() {
        return currentAudio;
    }

}
