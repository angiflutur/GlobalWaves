package app.command.playlist;

import app.command.searchBar.SelectCommand;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.Command;
import app.entities.User;
import app.entities.audio.file.AudioFile;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class AddRemoveInPlaylistCommand extends Command {
    private Integer playlistId;

    public AddRemoveInPlaylistCommand(final String username,
                                      final Integer timestamp, final Integer playlistId) {
        super(username, timestamp);
        this.playlistId = playlistId;
    }

    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());

        if (user == null || !user.isOnline()) {
            createResultNode(output, getUsername() + " is offline.");
            return;
        }

        Player player = PlayerManager.getPlayer(getUsername());
        AudioFile selectedAudio = SelectCommand.getSelectedAudioFile();
        Album selectedAlbum = SelectCommand.getSelectedAlbum();

        if (!player.isLoaded() || (selectedAudio == null && selectedAlbum == null)) {
            createResultNode(output, "Please load a source before adding to or removing from the playlist.");
            return;
        }

        if (selectedAudio != null && !(selectedAudio instanceof Song)) {
            createResultNode(output, "The loaded source is not a song.");
            return;
        }

        if (playlistId == null || playlistId <= 0 || playlistId > user.getPlaylists().size()) {
            createResultNode(output, "The specified playlist does not exist.");
            return;
        }

        Playlist playlist = user.getPlaylists().get(playlistId - 1);

        if (selectedAudio != null) {
            toggleSongInPlaylist(playlist, (Song) selectedAudio, output);
        } else if (selectedAlbum != null) {
            toggleAlbumInPlaylist(playlist, selectedAlbum, output);
        }
    }

    private void toggleSongInPlaylist(Playlist playlist, Song song, final ArrayNode output) {
        if (playlist.getSongs().contains(song)) {
            playlist.getSongs().remove(song);
            createResultNode(output, "Successfully removed from playlist.");
        } else {
            playlist.getSongs().add(song);
            createResultNode(output, "Successfully added to playlist.");
        }
    }

    private void toggleAlbumInPlaylist(Playlist playlist, Album album, final ArrayNode output) {
        boolean allSongsInPlaylist = playlist.getSongs().containsAll(album.getSongs());

        if (allSongsInPlaylist) {
            playlist.getSongs().removeAll(album.getSongs());
            createResultNode(output, "Successfully removed all album songs from playlist.");
        } else {
            for (Song song : album.getSongs()) {
                if (!playlist.getSongs().contains(song)) {
                    playlist.getSongs().add(song);
                }
            }
            createResultNode(output, "Successfully added to playlist.");
        }
    }

    private void createResultNode(final ArrayNode output, final String message) {
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "addRemoveInPlaylist");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", message);
    }
}
