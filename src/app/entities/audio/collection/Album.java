package app.entities.audio.collection;

import app.entities.audio.file.Song;

import java.util.List;

/**
 *
 */
public class Album {
    private String name;
    private Integer releaseYear;
    private String description;
    private List<Song> songs;

    /**
     *
     */
    public Album(final String name, final Integer releaseYear,
                 final String description, final List<Song> songs) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.description = description;
        this.songs = songs;
    }
    /**
     *
     */
    public String getName() {
        return name;
    }
    /**
     *
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     *
     */
    public Integer getReleaseYear() {
        return releaseYear;
    }
    /**
     *
     */
    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
    /**
     *
     */
    public String getDescription() {
        return description;
    }
    /**
     *
     */
    public void setDescription(final String description) {
        this.description = description;
    }
    /**
     *
     */
    public List<Song> getSongs() {
        return songs;
    }
    /**
     *
     */
    public void setSongs(final List<Song> songs) {
        this.songs = songs;
    }
    /**
     *
     */
    @Override
    public String toString() {
        return "Album{"
                + "name='" + name + '\''
                + ", releaseYear=" + releaseYear
                + ", description='" + description + '\''
                + ", songs=" + songs
                + '}';
    }
}
