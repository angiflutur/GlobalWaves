package app.audio.file;

import java.util.ArrayList;

public class Song extends AudioFile {
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private int releaseYear;
    private String artist;

    public Song() {
    }

    public Song(String name, Integer duration, String album, ArrayList<String> tags, String lyrics, String genre, int releaseYear, String artist) {
        super(name, duration);
        this.album = album;
        this.tags = tags;
        this.lyrics = lyrics;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{" +
                "album='" + album + '\'' +
                ", tags=" + tags +
                ", lyrics='" + lyrics + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear=" + releaseYear +
                ", artist='" + artist + '\'' +
                '}';
    }
}
