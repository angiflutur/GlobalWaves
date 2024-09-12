package app.entities.audio.file;

/**
 * JAVADOC
 */
public class PodcastEpisode extends AudioFile {
    private String description;

    /**
     * JAVADOC
     */
    public PodcastEpisode() {
    }

    /**
     * JAVADOC
     */
    public PodcastEpisode(final String name,
                          final Integer duration,
                          final String description) {
        super(name, duration);
        this.description = description;
    }

    /**
     * JAVADOC
     */
    public String getDescription() {
        return description;
    }

    /**
     * JAVADOC
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "PodcastEpisode{"
                + "description='" + description + '\''
                + '}';
    }
}
