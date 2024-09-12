package app.entities.audio.file;

import java.util.ArrayList;

/**
 * JAVADOC
 */
public class Song extends AudioFile {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private int releaseYear;
    private String artist;

    /**
     * JAVADOC
     */
    public Song() {
    }

    /**
     * JAVADOC
     */
    public Song(final String name,
                final Integer duration,
                final String album,
                final ArrayList<String> tags,
                final String lyrics,
                final String genre,
                final int releaseYear,
                final String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
    }

    /**
     * JAVADOC
     */
    public String getAlbum() {
        return album;
    }

    /**
     * JAVADOC
     */
    public void setAlbum(final String album) {
        this.album = album;
    }

    /**
     * JAVADOC
     */
    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * JAVADOC
     */
    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }

    /**
     * JAVADOC
     */
    public String getLyrics() {
        return lyrics;
    }

    /**
     * JAVADOC
     */
    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }

    /**
     * JAVADOC
     */
    public String getGenre() {
        return genre;
    }

    /**
     * JAVADOC
     */
    public void setGenre(final String genre) {
        this.genre = genre;
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
    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
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
    public void setArtist(final String artist) {
        this.artist = artist;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "Song{"
                + "album='" + album + '\''
                + ", tags=" + tags
                + ", lyrics='" + lyrics + '\''
                + ", genre='" + genre + '\''
                + ", releaseYear=" + releaseYear
                + ", artist='" + artist + '\''
                + '}';
    }
}
