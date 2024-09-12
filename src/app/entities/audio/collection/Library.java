package app.audio.collection;

import app.audio.file.PodcastEpisode;
import app.audio.file.Song;
import app.user.User;
import fileio.input.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Library {
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<User> users;
    private Map<String, Playlist> playlists; // Adăugăm un câmp pentru playlisturi

    public Library() {
        playlists = new HashMap<>();
    }

    public Library(ArrayList<Song> songs, ArrayList<Podcast> podcasts, ArrayList<User> users) {
        this.songs = songs;
        this.podcasts = podcasts;
        this.users = users;
        this.playlists = new HashMap<>();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void createLibrary(final LibraryInput library) {
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        users = new ArrayList<>();
        playlists = new HashMap<>(); // Inițializăm mapa pentru playlisturi

        for (SongInput song : library.getSongs()) {
            Song newSong = new Song();
            newSong.setName(song.getName());
            newSong.setArtist(song.getArtist());
            newSong.setAlbum(song.getAlbum());
            newSong.setReleaseYear(song.getReleaseYear());
            newSong.setTags(song.getTags());
            newSong.setDuration(song.getDuration());
            newSong.setLyrics(song.getLyrics());
            newSong.setGenre(song.getGenre());

            songs.add(newSong);
        }

        for (PodcastInput podcast : library.getPodcasts()) {
            Podcast newPodcast = new Podcast();
            newPodcast.setName(podcast.getName());
            newPodcast.setOwner(podcast.getOwner());

            ArrayList<PodcastEpisode> podcastEpisodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcast.getEpisodes()) {
                PodcastEpisode episode = new PodcastEpisode();
                episode.setDuration(episodeInput.getDuration());
                episode.setDescription(episodeInput.getDescription());
                podcastEpisodes.add(episode);
            }

            newPodcast.setEpisodes(podcastEpisodes);
            podcasts.add(newPodcast);
        }

        for (UserInput user : library.getUsers()) {
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setAge(user.getAge());
            newUser.setCity(user.getCity());

            users.add(newUser);
        }
    }

    public void createPlaylist(String playlistName) {
        if (!playlists.containsKey(playlistName)) {
            playlists.put(playlistName, new Playlist(playlistName));
        } else {
            System.out.println("Playlist with this name already exists.");
        }
    }

    public Playlist getPlaylist(String playlistName) {
        return playlists.get(playlistName);
    }

    public Map<String, Playlist> getPlaylists() {
        return playlists;
    }

    @Override
    public String toString() {
        return "Library{" +
                "songs=" + songs +
                ", podcasts=" + podcasts +
                ", users=" + users +
                ", playlists=" + playlists +
                '}';
    }
}
