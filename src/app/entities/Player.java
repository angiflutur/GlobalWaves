package app.entities;

import app.entities.audio.file.AudioFile;
/**
 * JAVADOC
 */
public class Player {
    private AudioFile currentAudio;
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
    public void loadAudio(final AudioFile audio,
                          final int timestamp) {
        if (audio == null) {
            throw new IllegalArgumentException("AudioFile cannot be null");
        }

        this.currentAudio = audio;
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
        if (!isPaused) {
            int timeElapsed = currentTimestamp - lastUpdateTimestamp;
            remainingTime -= timeElapsed;
            if (remainingTime < 0) {
                remainingTime = 0;
                isPaused = true;

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
}
