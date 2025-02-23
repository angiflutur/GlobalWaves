package app.entities;

import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.Song;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private Integer age;
    private String city;
    private ArrayList<Playlist> playlists = new ArrayList<>();
    private ArrayList<Playlist> followedPlaylists = new ArrayList<>();
    private ArrayList<Song> likedSongs = new ArrayList<>();
    private Player player;
    private boolean isOnline = true;
    private ArrayList<Album> albums = new ArrayList<>();
    private List<List<String>> events = new ArrayList<>();
    private List<List<String>> merch = new ArrayList<>();

    public enum UserType {
        USER, ARTIST, HOST
    }
    public enum PageType {
        HOME_PAGE,
        ARTIST_PAGE,
        LIKED_CONTENT_PAGE,
        HOST_PAGE
    }
    private PageType currentPage = PageType.HOME_PAGE;
    private UserType type;
    /**
     * JAVADOC
     */
    public User() {
        this.player = Player.getInstance();
    }
    /**
     * JAVADOC
     */
    public User(final String username,
                final Integer age,
                final String city,
                final UserType type) {
        this.username = username;
        this.age = age;
        this.city = city;
        this.likedSongs = new ArrayList<>();
        this.player = Player.getInstance();
        this.type = type;
    }
    /**
     * JAVADOC
     */
    public void setCurrentPage(final PageType page) {
        this.currentPage = page;
    }

    /**
     * JAVADOC
     */
    public PageType getCurrentPage() {
        return this.currentPage;
    }
    /**
     * JAVADOC
     */
    public void addMerch(final String name,
                         final String description,
                         final int price) {
        List<String> merchItem = new ArrayList<>();
        merchItem.add(name);
        merchItem.add(description);
        merchItem.add(String.valueOf(price));
        merch.add(merchItem);
    }

    /**
     * JAVADOC
     */
    public List<List<String>> getMerch() {
        return merch;
    }
    /**
     * JAVADOC
     */
    public void addEvent(final String eventName,
                         final String eventDate,
                         final String description) {
        List<String> event = new ArrayList<>();
        event.add(eventName);
        event.add(eventDate);
        event.add(description);
        this.events.add(event);
    }

    /**
     * JAVADOC
     */
    public List<List<String>> getEvents() {
        return this.events;
    }
    /**
     * JAVADOC
     */
    public void addAlbum(final Album album,
                         final Library library) {
        if (this.type == UserType.ARTIST) {
            albums.add(album);
            library.getAlbums().add(album);
        }
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
    public UserType getType() {
        return type;
    }
    /**
     * JAVADOC
     */
    public void setType(final UserType type) {
        this.type = type;
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
    public ArrayList<Album> getAlbums() {
        return albums;
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
    public boolean isOnline() {
        return isOnline;
    }
    /**
     * JAVADOC
     */
    public void setOnline(final boolean online) {
        isOnline = online;
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
    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + ", age=" + age
                + ", city='" + city + '\''
                + '}';
    }
}
