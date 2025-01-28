package app.command.playlist;

import app.command.searchBar.SelectCommand;
import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.AudioFile;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * JAVADOC
 */
public class FollowPlaylistCommand extends Command {
    public FollowPlaylistCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = PlayerManager.getPlayer(getUsername());
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "follow");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        Playlist selectedPlaylist = SelectCommand.getSelectedPlaylist();
        AudioFile selectedAudioFile = SelectCommand.getSelectedAudioFile();

        if (selectedPlaylist == null && selectedAudioFile == null) {
            resultNode.put("message", "Please select a source before following or unfollowing.");
            return;
        }

        if (selectedPlaylist == null) {
            resultNode.put("message", "The selected source is not a playlist.");
            return;
        }

        User user = library.getUser(getUsername());
        if (user == null) {
            resultNode.put("message", "User not found.");
            return;
        }

        if (!user.isConnectionStatus()) {
            resultNode.put("command", "select");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            return;
        }

        if (selectedPlaylist.getOwner() == null || selectedPlaylist.getOwner().equals(user)) {
            resultNode.put("message", "You cannot follow or unfollow your own playlist.");
            return;
        }

        if (user.getFollowedPlaylists().contains(selectedPlaylist)) {
            user.unfollowPlaylist(selectedPlaylist);
            selectedPlaylist.removeFollower(user);
            resultNode.put("message", "Playlist unfollowed successfully.");
        } else {
            user.followPlaylist(selectedPlaylist);
            selectedPlaylist.addFollower(user);
            resultNode.put("message", "Playlist followed successfully.");
        }
    }
}
