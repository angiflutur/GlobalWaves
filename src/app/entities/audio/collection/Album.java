package app.entities.audio.collection;

import app.entities.audio.file.Song;

import java.util.List;
/**
 * JAVADOC
 */
public class Album {
    private String name;
    private int releaseYear;
    private String description;
    private String artist;
    private List<Song> songs;
    /**
     * JAVADOC
     */
    public Album(final String name,
                 final int releaseYear,
                 final String description,
                 final String artist,
                 final List<Song> songs) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.description = description;
        this.artist = artist;
        this.songs = songs;
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
    public int getReleaseYear() {
        return releaseYear;
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
    public String getArtist() {
        return artist;
    }
    /**
     * JAVADOC
     */
    public List<Song> getSongs() {
        return songs;
    }
}
