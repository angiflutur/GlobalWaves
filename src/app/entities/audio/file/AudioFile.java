package app.audio.file;

public abstract class AudioFile {
    private String name;
    private Integer duration;

    public AudioFile() {
    }

    public AudioFile(String name, Integer duration) {
        this.name = name;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "AudioFile{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
