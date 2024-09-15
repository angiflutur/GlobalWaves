package app.entities;

import app.entities.audio.file.AudioFile;
import app.entities.audio.collection.Playlist;

import java.util.ArrayList;
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
    public void loadPlaylist(final Playlist playlist, final int timestamp) {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null");
        }

        this.currentPlaylist = playlist;
        ArrayList<AudioFile> audioFiles = new ArrayList<>(playlist.getSongs());
        this.playlistIterator = audioFiles.iterator();

        if (playlistIterator.hasNext()) {
            this.currentAudio = playlistIterator.next();
            this.isLoaded = true;
            this.isPaused = false;
            this.remainingTime = currentAudio.getDuration();
            this.lastUpdateTimestamp = timestamp;
        } else {
            this.currentAudio = null;
            this.isLoaded = false;
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
        if (!isPaused) {
            int timeElapsed = currentTimestamp - lastUpdateTimestamp;
            remainingTime -= timeElapsed;
            if (remainingTime < 0) {
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
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * JAVADOC
     */
    public int getRemainingTime(final int currentTimestamp) {
        updateRemainingTime(currentTimestamp);
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
    public AudioFile getCurrentAudio() {
        return currentAudio;
    }

    /**
     * JAVADOC
     */
    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }
}
