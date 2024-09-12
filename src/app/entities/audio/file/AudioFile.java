package app.entities.audio.file;

/**
 * JAVADOC
 */
public abstract class AudioFile {
    private String name;
    private Integer duration = 0;

    /**
     * JAVADOC
     */
    public AudioFile() {
    }

    /**
     * JAVADOC
     */
    public AudioFile(final String name,
                     final Integer duration) {
        this.name = name;
        this.duration = duration;
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
    public Integer getDuration() {
        return duration;
    }

    /**
     * JAVADOC
     */
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "AudioFile{"
                + "name='" + name + '\''
                + ", duration=" + duration
                + '}';
    }
}
