package app.entities.audio.collection;

import app.entities.audio.file.AudioFile;
import app.entities.audio.file.PodcastEpisode;

import java.util.ArrayList;

public class Podcast extends AudioFile {
    private String name;
    private String owner;
    private ArrayList<PodcastEpisode> episodes;
    private int currentEpisodeIndex;
    private int currentEpisodeRemainingTime; // Adăugat aici

    public Podcast() {
        this.currentEpisodeIndex = 0;
        this.currentEpisodeRemainingTime = 0;
    }

    public Podcast(final String name, final String owner) {
        this.name = name;
        this.owner = owner;
        this.currentEpisodeIndex = 0;
        this.currentEpisodeRemainingTime = 0;
    }

    public Podcast(final String name, final String owner, final ArrayList<PodcastEpisode> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
        this.currentEpisodeIndex = 0;
        this.currentEpisodeRemainingTime = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public ArrayList<PodcastEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(final ArrayList<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }

    public int getCurrentEpisodeIndex() {
        return currentEpisodeIndex;
    }

    public void setCurrentEpisodeIndex(final int index) {
        this.currentEpisodeIndex = index;
    }

    public int getCurrentEpisodeRemainingTime() {
        return currentEpisodeRemainingTime;
    }

    public void setCurrentEpisodeRemainingTime(int time) {
        this.currentEpisodeRemainingTime = time;
    }

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
