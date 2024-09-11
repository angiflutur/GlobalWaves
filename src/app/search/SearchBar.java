package app.search;

import app.audio.collection.Library;
import app.audio.collection.Playlist;
import app.audio.file.Song;

import java.util.ArrayList;

public class SearchBar {
    private Library library;

    public SearchBar() {
    }

    public SearchBar(Library library) {
        this.library = library;
    }

    public ArrayList<Song> searchSongsByName(String name) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song : library.getSongs()) {
            if (song.getName().toLowerCase().contains(name.toLowerCase())) {
                songs.add(song);
            }
        }
        return songs;
    }

    public ArrayList<Song> searchSongsByAlbum(String album) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song : library.getSongs()) {
            if (song.getAlbum().toLowerCase().contains(album.toLowerCase())) {
                songs.add(song);
            }
        }
        return songs;
    }

    public ArrayList<Song> searchSongsByTags(ArrayList<String> tags) {
        ArrayList<Song> songs = new ArrayList<>();
        for (String tag : tags) {
            for (Song song : library.getSongs()) {
                if (song.getTags().contains(tag)) {
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    public ArrayList<Song> searchSongsByLyrics(String lyrics) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song : library.getSongs()) {
            if (song.getLyrics().toLowerCase().contains(lyrics.toLowerCase())) {
                songs.add(song);
            }
        }
        return songs;
    }

    public ArrayList<Song> searchSongsByGenre(String genre) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song : library.getSongs()) {
            if (song.getGenre().toLowerCase().contains(genre.toLowerCase())) {
                songs.add(song);
            }
        }
        return songs;
    }
    public ArrayList<Song> searchSongsByReleaseYear(Integer releaseYear) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song : library.getSongs()) {
            if (song.getReleaseYear() == releaseYear) {
                songs.add(song);
            }
        }
        return songs;
    }
    public ArrayList<Song> searchSongsByArtist(String artist) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song song : library.getSongs()) {
            if (song.getArtist().toLowerCase().contains(artist.toLowerCase())) {
                songs.add(song);
            }
        }
        return songs;
    }

}
