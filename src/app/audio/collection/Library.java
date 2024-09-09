package app.audio.collection;

import app.audio.file.PodcastEpisode;
import app.audio.file.Song;
import app.user.User;
import fileio.input.*;

import java.util.ArrayList;

public class Library {
    private ArrayList<Song> songs;
    private ArrayList<Podcast> podcasts;
    private ArrayList<User> users;

    public Library() {
    }

    public Library(ArrayList<Song> songs, ArrayList<Podcast> podcasts, ArrayList<User> users) {
        this.songs = songs;
        this.podcasts = podcasts;
        this.users = users;
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

            // Transformăm EpisodeInput în PodcastEpisode
            ArrayList<PodcastEpisode> podcastEpisodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcast.getEpisodes()) {
                PodcastEpisode episode = new PodcastEpisode();
                episode.setDuration(episodeInput.getDuration());
                episode.setDescription(episodeInput.getDescription());
                // Alte câmpuri din EpisodeInput pe care vrei să le mapezi în PodcastEpisode
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

    @Override
    public String toString() {
        return "Library{" +
                "songs=" + songs +
                ", podcasts=" + podcasts +
                ", users=" + users +
                '}';
    }
}
