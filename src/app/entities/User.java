package app.entities;

import app.entities.audio.collection.Playlist;
import app.entities.audio.file.Song;

import java.util.ArrayList;

/**
 * JAVADOC
 */
public class User {
    private String username;
    private Integer age;
    private String city;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Playlist> followedPlaylists = new ArrayList<>();
    private ArrayList<Song> likedSongs = new ArrayList<>();

    /**
     * JAVADOC
     */
    public User() {
    }

    /**
     * JAVADOC
     */
    public User(final String username, final Integer age, final String city) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.likedSongs = new ArrayList<>();
    }

    /**
     * JAVADOC
     */
    public String getUsername() {
        return username;
    }

    /**
     * JAVADOC
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * JAVADOC
     */
    public void setAge(final Integer age) {
        this.age = age;
    }

    /**
     * JAVADOC
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * JAVADOC
     */
    public void addPlaylist(final Playlist playlist) {
        playlists.add(playlist);
    }

    /**
     * JAVADOC
     */
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * JAVADOC
     */
    public void likeSong(final Song song) {
        likedSongs.add(song);
    }

    /**
     * JAVADOC
     */
    public void unlikeSong(final Song song) {
        likedSongs.remove(song);
    }

    /**
     * JAVADOC
     */
    public ArrayList<Song> getLikedSongs() {
        return likedSongs;
    }

    /**
     * JAVADOC
     */
    public void followPlaylist(final Playlist playlist) {
        if (!followedPlaylists.contains(playlist)) {
            followedPlaylists.add(playlist);
        }
    }

    /**
     * JAVADOC
     */
    public void unfollowPlaylist(final Playlist playlist) {
        followedPlaylists.remove(playlist);
    }

    /**
     * JAVADOC
     */
    public ArrayList<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "User{"
                + "username='"
                + username
                + '\''
                + ", age="
                + age
                + ", city='"
                + city
                + '\''
                + '}';
    }
}
