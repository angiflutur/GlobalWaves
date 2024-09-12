package app.command.searchBar;

import app.audio.collection.Library;
import app.audio.collection.Podcast;
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
        if (name == null) {
            return songs;
        }
        String lowerCaseName = name.toLowerCase();
        for (Song song : library.getSongs()) {
            if (song.getName().toLowerCase().startsWith(lowerCaseName)) {
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

        if (tags == null || tags.isEmpty()) {
            return songs;
        }

        for (Song song : library.getSongs()) {
            if (song.getTags().containsAll(tags)) {
                songs.add(song);
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

    public ArrayList<Song> searchSongsByReleaseYear(String filterReleaseYear) {
        ArrayList<Song> songs = new ArrayList<>();

        if (filterReleaseYear.startsWith("<")) {
            int yearLimit = Integer.parseInt(filterReleaseYear.substring(1).trim());
            for (Song song : library.getSongs()) {
                if (song.getReleaseYear() < yearLimit) {
                    songs.add(song);
                }
            }
        } else if (filterReleaseYear.startsWith(">")) {
            int yearLimit = Integer.parseInt(filterReleaseYear.substring(1).trim());
            for (Song song : library.getSongs()) {
                if (song.getReleaseYear() > yearLimit) {
                    songs.add(song);
                }
            }
        } else {
            int exactYear = Integer.parseInt(filterReleaseYear.trim());
            for (Song song : library.getSongs()) {
                if (song.getReleaseYear() == exactYear) {
                    songs.add(song);
                }
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

    public ArrayList<Podcast> searchPodcastsByName(String name) {
        ArrayList<Podcast> podcasts = new ArrayList<>();
        if (name == null) {
            return podcasts;
        }
        String lowerCaseName = name.toLowerCase();
        for (Podcast podcast : library.getPodcasts()) {
            if (podcast.getName().toLowerCase().startsWith(lowerCaseName)) {
                podcasts.add(podcast);
            }
        }
        return podcasts;
    }

    public ArrayList<Podcast> searchPodcastsByOwner(String owner) {
        ArrayList<Podcast> podcasts = new ArrayList<>();
        if (owner == null) {
            return podcasts;
        }
        String lowerCaseOwner = owner.toLowerCase();
        for (Podcast podcast : library.getPodcasts()) {
            if (podcast.getOwner().toLowerCase().contains(lowerCaseOwner)) {
                podcasts.add(podcast);
            }
        }
        return podcasts;
    }

}
