package app.entities;

import app.entities.audio.collection.Album;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * JAVADOC
 */
public class User {
    private String username;
    private Integer age;
    private String city;
    private boolean connectionStatus = true;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Playlist> followedPlaylists = new ArrayList<>();
    private ArrayList<Song> likedSongs = new ArrayList<>();
    private Player player;

    private List<Album> albums = new ArrayList<>();
    private boolean isArtist;
    /**
     * JAVADOC
     */
    public User() {
        this.player = Player.getInstance();
    }

    /**
     * JAVADOC
     */
    public User(final String username, final Integer age,
                final String city, final boolean isArtist) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.isArtist = isArtist;
    }
    /**
     * JAVADOC
     */
    public List<Album> getAlbums() {
        return albums;
    }
    /**
     * JAVADOC
     */
    public void setAlbums(final List<Album> albums) {
        this.albums = albums;
    }
    /**
     * JAVADOC
     */
    public void addAlbum(final String name, final Integer releaseYear,
                         final String description, final List<Song> songs) {
        Album newAlbum = new Album(name, releaseYear, description, songs);
        albums.add(newAlbum);
    }

    /**
     * Verifică dacă utilizatorul are deja un album cu un anumit nume.
     */
    public boolean hasAlbum(final String albumName) {
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(albumName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifică dacă utilizatorul este un artist.
     */
    public boolean isArtist() {
        return isArtist;
    }
    /**
     * JAVADOC
     */
    public void setArtist(final boolean artistStatus) {
        this.isArtist = artistStatus;
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
    public Player getPlayer() {
        return player;
    }
    /**
     * JAVADOC
     */
    public boolean isConnectionStatus() {
        return connectionStatus;
    }
    /**
     * JAVADOC
     */
    public void setConnectionStatus(final boolean connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "User{"
                + "username='"
                + username
                + '\'' + ", age=" + age
                + ", city='" + city + '\''
                + ", isArtist=" + isArtist + '}';
    }
}
