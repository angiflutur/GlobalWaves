package app.audio.collection;

import app.audio.file.Song;
import app.user.User;

import java.util.ArrayList;

public class Playlist {
    private User owner;
    private boolean isPublic;
    private ArrayList<Song> songs;

    public Playlist() {}

    public Playlist(User owner, boolean isPublic, ArrayList<Song> songs) {
        this.owner = owner;
        this.isPublic = isPublic;
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "owner=" + owner +
                ", isPublic=" + isPublic +
                ", songs=" + songs +
                '}';
    }
}
