package app.command;

import app.command.player.BackwardCommand;
import app.command.player.ForwardCommand;
import app.command.player.LikeCommand;
import app.command.player.LoadCommand;
import app.command.player.NextCommand;
import app.command.player.PlayPauseCommand;
import app.command.player.PrevCommand;
import app.command.player.RepeatCommand;
import app.command.player.ShuffleCommand;
import app.command.player.StatusCommand;
import app.command.playlist.AddRemoveInPlaylistCommand;
import app.command.playlist.CreatePlaylistCommand;
import app.command.playlist.FollowPlaylistCommand;
import app.command.playlist.ShowPlaylistsCommand;
import app.command.playlist.SwitchVisibilityCommand;
import app.command.searchBar.SearchCommand;
import app.command.searchBar.SelectCommand;
import app.command.stats.GetTop5PlaylistsCommand;
import app.command.stats.GetTop5SongsCommand;
import app.command.stats.ShowPreferredSongsCommand;
import app.entities.Command;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * JAVADOC
 */
public final class CommandParser {

    /**
     * JAVADOC
     */
    private CommandParser() {
    }

    /**
     * JAVADOC
     */
    public static Command createCommand(final JsonNode jsonNode) {
        String commandType = jsonNode.has("command") ? jsonNode.get("command").asText() : null;
        String username = jsonNode.has("username") ? jsonNode.get("username").asText() : null;
        Integer timestamp = jsonNode.has("timestamp") ? jsonNode.get("timestamp").asInt() : null;

        switch (commandType) {
            case "search":
                String type = jsonNode.has("type") ? jsonNode.get("type").asText() : null;
                JsonNode filtersNode = jsonNode.has("filters") ? jsonNode.get("filters") : null;

                String filterName = filtersNode != null && filtersNode.has("name")
                        ? filtersNode.get("name").asText() : null;
                String filterAlbum = filtersNode != null && filtersNode.has("album")
                        ? filtersNode.get("album").asText() : null;
                String filterLyrics = filtersNode != null && filtersNode.has("lyrics")
                        ? filtersNode.get("lyrics").asText() : null;
                String filterGenre = filtersNode != null && filtersNode.has("genre")
                        ? filtersNode.get("genre").asText() : null;
                String filterReleaseYear = filtersNode != null && filtersNode.has("releaseYear")
                        ? filtersNode.get("releaseYear").asText() : null;
                String filterArtist = filtersNode != null && filtersNode.has("artist")
                        ? filtersNode.get("artist").asText() : null;
                ArrayNode filterTags = filtersNode != null && filtersNode.has("tags")
                        ? (ArrayNode) filtersNode.get("tags") : null;
                String filterOwner = filtersNode != null && filtersNode.has("owner")
                        ? filtersNode.get("owner").asText() : null;

                return new SearchCommand(username, timestamp, type, filterName,
                        filterAlbum, filterLyrics, filterGenre, filterReleaseYear,
                        filterArtist, filterTags, filterOwner);
            case "select":
                Integer itemNumber = jsonNode.has("itemNumber")
                        ? jsonNode.get("itemNumber").asInt() : -1;
                return new SelectCommand(username, timestamp, itemNumber);
            case "load":
                return new LoadCommand(username, timestamp);
            case "playPause":
                return new PlayPauseCommand(username, timestamp);
            case "status":
                return new StatusCommand(username, timestamp);
            case "createPlaylist":
                String playlistName = jsonNode.has("playlistName")
                        ? jsonNode.get("playlistName").asText() : null;
                return new CreatePlaylistCommand(username, timestamp, playlistName);
            case "addRemoveInPlaylist":
                Integer playlistId = jsonNode.has("playlistId")
                        ? jsonNode.get("playlistId").asInt() : null;
                return new AddRemoveInPlaylistCommand(username, timestamp, playlistId);
            case "like":
                return new LikeCommand(username, timestamp);
            case "showPlaylists":
                return new ShowPlaylistsCommand(username, timestamp);
            case "showPreferredSongs":
                return new ShowPreferredSongsCommand(username, timestamp);
            case "repeat":
                return new RepeatCommand(username, timestamp);
            case "shuffle":
                Integer seed = jsonNode.has("seed") ? jsonNode.get("seed").asInt() : -1;
                return new ShuffleCommand(username, timestamp, seed);
            case "forward":
                return new ForwardCommand(username, timestamp);
            case "backward":
                return new BackwardCommand(username, timestamp);
            case "next":
                return new NextCommand(username, timestamp);
            case "prev":
                return new PrevCommand(username, timestamp);
            case "follow":
                return new FollowPlaylistCommand(username, timestamp);
            case "switchVisibility":
                playlistId = jsonNode.has("playlistId") ? jsonNode.get("playlistId").asInt() : -1;
                return new SwitchVisibilityCommand(username, timestamp, playlistId);
            case "getTop5Songs":
                return new GetTop5SongsCommand(timestamp);
            case "getTop5Playlists":
                return new GetTop5PlaylistsCommand(timestamp);
            default:
                return new UnknownCommand(username, timestamp);
        }
    }
}
