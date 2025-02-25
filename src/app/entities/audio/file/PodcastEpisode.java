package app.entities.audio.file;

/**
 * Clasa care reprezintă un episod de podcast.
 */
public class PodcastEpisode extends AudioFile {
    private String description;

    /**
     * Constructor pentru PodcastEpisode.
     */
    public PodcastEpisode() {
    }

    /**
     * Constructor cu parametri pentru PodcastEpisode.
     */
    public PodcastEpisode(final String name, final Integer duration, final String description) {
        super(name, duration);
        this.description = description;
    }

    /**
     * Getter pentru descrierea episodului.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter pentru descrierea episodului.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Reprezentarea textului pentru acest obiect.
     */
    @Override
    public String toString() {
        return "PodcastEpisode{"
                + "name='" + getName() + '\''
                + ", description='" + description + '\''
                + ", duration=" + getDuration()
                + '}';
    }
}
