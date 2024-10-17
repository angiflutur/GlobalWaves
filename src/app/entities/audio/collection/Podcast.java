package app.entities.audio.collection;

import app.entities.audio.file.AudioFile;
import app.entities.audio.file.PodcastEpisode;

import java.util.ArrayList;
/**
 * JAVADOC
 */
public class Podcast extends AudioFile {
    private String name;
    private String owner;
    private ArrayList<PodcastEpisode> episodes;
    private int currentEpisodeIndex;
    private int currentEpisodeRemainingTime;

    /**
     * JAVADOC
     */
    public Podcast() {
        this.currentEpisodeIndex = 0;
        this.currentEpisodeRemainingTime = 0;
    }

    /**
     * JAVADOC
     */
    public Podcast(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        this.currentEpisodeIndex = 0;
        this.currentEpisodeRemainingTime = 0;
    }

    /**
     * JAVADOC
     */
    public Podcast(final String name,
                   final String owner,
                   final ArrayList<PodcastEpisode> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
        this.currentEpisodeIndex = 0;
        this.currentEpisodeRemainingTime = 0;
    }

    /**
     * JAVADOC
     */
    public String getName() {
        return name;
    }

    /**
     * JAVADOC
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * JAVADOC
     */
    public String getOwner() {
        return owner;
    }

    /**
     * JAVADOC
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * JAVADOC
     */
    public ArrayList<PodcastEpisode> getEpisodes() {
        return episodes;
    }

    /**
     * JAVADOC
     */
    public void setEpisodes(final ArrayList<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }

    /**
     * JAVADOC
     */
    public int getCurrentEpisodeIndex() {
        return currentEpisodeIndex;
    }

    /**
     * JAVADOC
     */
    public void setCurrentEpisodeIndex(final int index) {
        this.currentEpisodeIndex = index;
    }

    /**
     * JAVADOC
     */
    public int getCurrentEpisodeRemainingTime() {
        return currentEpisodeRemainingTime;
    }

    /**
     * JAVADOC
     */
    public void setCurrentEpisodeRemainingTime(final int time) {
        this.currentEpisodeRemainingTime = time;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "Podcast{"
                + "name='" + name + '\''
                + ", owner='" + owner + '\''
                + ", episodes=" + episodes
                + ", currentEpisodeIndex=" + currentEpisodeIndex
                + '}';
    }
}
