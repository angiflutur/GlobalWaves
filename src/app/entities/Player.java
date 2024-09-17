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
        this.lastUpdateTimestamp = timestamp;

        // Set the remaining time based on the type of audio file
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

            if (currentAudio instanceof Podcast) {
                Podcast podcast = (Podcast) currentAudio;
                int currentEpisodeIndex = podcast.getCurrentEpisodeIndex();

                if (currentEpisodeIndex >= 0 && currentEpisodeIndex < podcast.getEpisodes().size()) {
                    PodcastEpisode currentEpisode = podcast.getEpisodes().get(currentEpisodeIndex);
                    int currentEpisodeRemainingTime = podcast.getCurrentEpisodeRemainingTime();

                    // Scade timpul care a trecut din timpul rămas al episodului curent
                    currentEpisodeRemainingTime -= timeElapsed;

                    // Verifică dacă episodul curent s-a terminat
                    if (currentEpisodeRemainingTime <= 0) {
                        // Dacă nu este ultimul episod, treci la episodul următor
                        if (currentEpisodeIndex < podcast.getEpisodes().size() - 1) {
                            podcast.setCurrentEpisodeIndex(currentEpisodeIndex + 1);
                            PodcastEpisode nextEpisode = podcast.getEpisodes().get(podcast.getCurrentEpisodeIndex());

                            // Setează timpul rămas la durata episodului următor minus timpul rămas din episodul curent
                            podcast.setCurrentEpisodeRemainingTime(nextEpisode.getDuration() + currentEpisodeRemainingTime);
                        } else {
                            // Dacă este ultimul episod, resetează
                            currentAudio = null;
                            isLoaded = false;
                            podcast.setCurrentEpisodeRemainingTime(0);
                        }
                    } else {
                        // Dacă episodul curent nu s-a terminat, actualizează timpul rămas
                        podcast.setCurrentEpisodeRemainingTime(currentEpisodeRemainingTime);
                    }

                    this.remainingTime = podcast.getCurrentEpisodeRemainingTime();
                }
            } else {
                remainingTime -= timeElapsed;
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

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    /**
     * JAVADOC
     */
    public AudioFile getCurrentAudio() {
        return currentAudio;
    }
}
