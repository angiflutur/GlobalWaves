package app.command.playlist;

import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.Command;
import app.entities.User;
import app.entities.Player;
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
        Player player = Player.getInstance();
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "follow");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        if (player.getCurrentPlaylist() == null && player.getCurrentAudio() == null) {
            resultNode.put("message", "Please select a source before following or unfollowing.");
            return;
        }
        if (!(player.getCurrentPlaylist() instanceof Playlist)) {
            resultNode.put("message", "The selected source is not a playlist.");
            return;
        }

        User user = library.getUser(getUsername());
        if (user == null) {
            resultNode.put("message", "User not found.");
            return;
        }

        Playlist currentPlaylist = player.getCurrentPlaylist();

        if (currentPlaylist.getOwner().equals(user)) {
            resultNode.put("message", "You cannot follow or unfollow your own playlist.");
            return;
        }

        if (user.getFollowedPlaylists().contains(currentPlaylist)) {
            user.unfollowPlaylist(currentPlaylist);
            currentPlaylist.removeFollower(user);
            resultNode.put("message", "Playlist unfollowed successfully.");
        } else {
            user.followPlaylist(currentPlaylist);
            currentPlaylist.addFollower(user);
            resultNode.put("message", "Playlist followed successfully.");
        }
    }
}
