package app.entities;

import app.entities.audio.collection.Podcast;
import app.entities.audio.file.AudioFile;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.PodcastEpisode;
import app.entities.audio.file.Song;

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
    private int currentIndex;

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

        if (audio instanceof Playlist) {
            Playlist playlist = (Playlist) audio;
            this.currentPlaylist = playlist;
            this.currentIndex = 0;

            if (playlist.getSongs().size() > 0) {
                this.currentAudio = playlist.getSongs().get(0);
                this.remainingTime = this.currentAudio.getDuration();
            } else {
                this.currentAudio = null;
                this.remainingTime = 0;
            }

            this.isLoaded = true;
            this.isPaused = false;
            this.lastUpdateTimestamp = timestamp;
        } else {
            this.currentAudio = audio;
            this.currentPlaylist = null;
            this.isLoaded = true;
            this.isPaused = false;
            this.lastUpdateTimestamp = timestamp;

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

                if (currentEpisodeIndex >= 0
                        && currentEpisodeIndex < podcast.getEpisodes().size()) {
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
            } else if (currentAudio instanceof Song) {
                remainingTime -= timeElapsed;

                if (remainingTime <= 0) {
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
            } else if (currentPlaylist != null) {
                remainingTime -= timeElapsed;
                if (remainingTime <= 0) {
                    currentIndex++;
                    if (currentIndex < currentPlaylist.getSongs().size()) {
                        currentAudio = currentPlaylist.getSongs().get(currentIndex);
                        remainingTime = currentAudio.getDuration();
                    } else {
                        currentAudio = null;
                        remainingTime = 0;
                        isLoaded = false;
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
    public void setPaused(final boolean paused) {
        isPaused = paused;
    }

    /**
     * JAVADOC
     */
    public AudioFile getCurrentAudio() {
        return currentAudio;
    }
}
