package app.entities.audio.collection;

import app.entities.audio.file.Song;
import app.entities.User;

import java.util.ArrayList;

/**
 * JAVADOC
 */
public class Playlist extends Song {
    private User owner;
    private boolean isPublic;
    private ArrayList<Song> songs;
    private String name;
    private ArrayList<User> followers;

    /**
     * JAVADOC
     */
    public Playlist(final String name) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.isPublic = true;
        this.followers = new ArrayList<>();
    }

    /**
     * JAVADOC
     */
    public Playlist(final User owner,
                    final boolean isPublic,
                    final ArrayList<Song> songs,
                    final String name) {
        this.owner = owner;
        this.isPublic = isPublic;
        this.songs = songs;
        this.name = name;
        this.followers = new ArrayList<>();
    }
    /**
     * JAVADOC
     */
    public int getFollowers() {
        return followers.size();
    }

    /**
     * JAVADOC
     */
    public String getName() {
        return name;
    }

    /**
     * JAVADOC
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * JAVADOC
     */
    public User getOwner() {
        return owner;
    }

    /**
     * JAVADOC
     */
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    /**
     * JAVADOC
     */
    public String getVisibility() {
        return isPublic ? "public" : "private";
    }

    /**
     * JAVADOC
     */
    public void setPublic(final boolean aPublic) {
        isPublic = aPublic;
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
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * JAVADOC
     */
    public void removeSong(final Song song) {
        songs.remove(song);
    }

    /**
     * JAVADOC
     */
    @Override
    public String toString() {
        return "Playlist{"
                + "owner=" + owner
                + ", isPublic=" + isPublic
                + ", songs=" + songs
                + ", name='" + name + '\''
                + '}';
    }
}
