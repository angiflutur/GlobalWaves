package app.audio.file;

public class PodcastEpisode extends AudioFile{
    private String description;

    public PodcastEpisode() {
    }

    public PodcastEpisode(String name, Integer duration, String description) {
        super(name, duration);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PodcastEpisode{" +
                "description='" + description + '\'' +
                '}';
    }
}
