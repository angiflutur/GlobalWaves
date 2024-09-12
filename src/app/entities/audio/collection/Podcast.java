package app.audio.collection;

import app.audio.file.PodcastEpisode;

import java.util.ArrayList;

public class Podcast {
    private String name;
    private String owner;
    private ArrayList<PodcastEpisode> episodes;

    public Podcast() {
    }

    public Podcast(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public Podcast(String name, String owner, ArrayList<PodcastEpisode> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<PodcastEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<PodcastEpisode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public String toString() {
        return "Podcast{" +
                "name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", episodes=" + episodes +
                '}';
    }
}
