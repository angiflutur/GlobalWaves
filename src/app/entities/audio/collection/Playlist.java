package app.audio.collection;

import app.audio.file.Song;
import app.user.User;

import java.util.ArrayList;

public class Playlist {
    private User owner;
    private boolean isPublic;
    private ArrayList<Song> songs;
    private String name;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
        this.isPublic = true;
    }

    public Playlist(User owner, boolean isPublic, ArrayList<Song> songs, String name) {
        this.owner = owner;
        this.isPublic = isPublic;
        this.songs = songs;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    public void addSong(Song song) {
        songs.add(song);
    }

    public void removeSong(Song song) {
        songs.remove(song);
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "owner=" + owner +
                ", isPublic=" + isPublic +
                ", songs=" + songs +
                ", name='" + name + '\'' +
                '}';
    }
}
