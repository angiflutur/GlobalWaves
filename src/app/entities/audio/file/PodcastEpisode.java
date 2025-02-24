package app.entities.audio.file;

/**
 * Clasa care reprezintă un episod de podcast.
 */
public class PodcastEpisode extends AudioFile {
    private String title;
    private Integer duration;
    private String description;

    /**
     * Constructor pentru PodcastEpisode.
     */
    public PodcastEpisode() {
    }

    /**
     * Constructor cu parametri pentru PodcastEpisode.
     */
    public PodcastEpisode(final String title, final Integer duration, final String description) {
        this.title = title;
        this.duration = duration;
        this.description = description;
    }

    /**
     * Getter pentru titlul episodului.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter pentru titlul episodului.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Getter pentru durata episodului.
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Setter pentru durata episodului.
     */
    public void setDuration(final Integer duration) {
        this.duration = duration;
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
                + "title='" + title + '\''
                + ", description='" + description + '\''
                + ", duration=" + duration
                + '}';
    }
}
