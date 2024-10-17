package app.entities.audio.collection;

import app.entities.audio.file.PodcastEpisode;
import app.entities.audio.file.Song;
import app.entities.User;
import fileio.input.EpisodeInput;
import fileio.input.LibraryInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * JAVADOC
 */
public class Library {
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<User> users;
    private Map<String, Playlist> playlists;

    /**
     * JAVADOC
     */
    public Library() {
        playlists = new HashMap<>();
    }

    /**
     * JAVADOC
     */
    public Library(final ArrayList<Song> songs,
                   final ArrayList<Podcast> podcasts,
                   final ArrayList<User> users) {
        this.songs = songs;
        this.podcasts = podcasts;
        this.users = users;
        this.playlists = new HashMap<>();
    }

    /**
     * JAVADOC
     */
    public ArrayList<Song> getSongs() {
        return songs;
    }

    /**
     * JAVADOC
     */
    public void setSongs(final ArrayList<Song> songs) {
        this.songs = songs;
    }

    /**
     * JAVADOC
     */
    public ArrayList<Podcast> getPodcasts() {
        return podcasts;
    }

    /**
     * JAVADOC
     */
    public void setPodcasts(final ArrayList<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    /**
     * JAVADOC
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * JAVADOC
     */
    public User getUser(final String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * JAVADOC
     */
    public void createLibrary(final LibraryInput library) {
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        users = new ArrayList<>();
        playlists = new HashMap<>();

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
                episode.setName(episodeInput.getName());
                episode.setDescription(episodeInput.getDescription());
                podcastEpisodes.add(episode);
            }

            newPodcast.setEpisodes(podcastEpisodes);

            if (!podcastEpisodes.isEmpty()) {
                newPodcast.setCurrentEpisodeRemainingTime(podcastEpisodes.get(0).getDuration());
            }

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

    /**
     * JAVADOC
     */
    public Playlist getPlaylist(final String playlistName) {
        return playlists.get(playlistName);
    }

    /**
     * JAVADOC
     */
    public Map<String, Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "Library{"
                + "songs=" + songs
                + ", podcasts=" + podcasts
                + ", users=" + users
                + ", playlists=" + playlists
                + '}';
    }
}
