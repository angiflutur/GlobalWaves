package app.command;

import app.command.stats.*;
import app.command.user.artist.AddAlbumCommand;
import app.command.user.admin.AddUserCommand;
import app.command.player.BackwardCommand;
import app.command.player.ForwardCommand;
import app.command.player.LikeCommand;
import app.command.player.LoadCommand;
import app.command.player.NextCommand;
import app.command.player.PlayPauseCommand;
import app.command.player.PrevCommand;
import app.command.page.PrintCurrentPageCommand;
import app.command.player.RepeatCommand;
import app.command.user.admin.ShowAlbumsCommand;
import app.command.player.ShuffleCommand;
import app.command.player.StatusCommand;
import app.command.playlist.AddRemoveInPlaylistCommand;
import app.command.playlist.CreatePlaylistCommand;
import app.command.playlist.FollowPlaylistCommand;
import app.command.playlist.ShowPlaylistsCommand;
import app.command.playlist.SwitchVisibilityCommand;
import app.command.searchBar.SearchCommand;
import app.command.searchBar.SelectCommand;
import app.command.user.artist.AddEventCommand;
import app.command.user.artist.AddMerchCommand;
import app.command.user.normal.SwitchConnectionStatusCommand;
import app.entities.Command;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.ArrayList;
import java.util.List;

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
            case "switchConnectionStatus":
                return new SwitchConnectionStatusCommand(username, timestamp);
            case "getOnlineUsers":
                return new GetOnlineUsersCommand(timestamp);
            case "addUser":
                String userType = jsonNode.get("type").asText();
                Integer age = jsonNode.has("age") ? jsonNode.get("age").asInt() : null;
                String city = jsonNode.has("city") ? jsonNode.get("city").asText() : null;
                return new AddUserCommand(username, timestamp, userType, username, age, city);
            case "addAlbum":
                String albumName = jsonNode.has("name") ? jsonNode.get("name").asText() : null;
                Integer releaseYear = jsonNode.has("releaseYear")
                        ? jsonNode.get("releaseYear").asInt() : null;
                String description = jsonNode.has("description")
                        ? jsonNode.get("description").asText() : null;

                List<Song> songs = new ArrayList<>();
                if (jsonNode.has("songs") && jsonNode.get("songs").isArray()) {
                    for (JsonNode songNode : jsonNode.get("songs")) {
                        String songName = songNode.has("name")
                                ? songNode.get("name").asText() : null;
                        Integer duration = songNode.has("duration")
                                ? songNode.get("duration").asInt() : null;
                        String album = songNode.has("album")
                                ? songNode.get("album").asText() : null;
                        String artist = songNode.has("artist")
                                ? songNode.get("artist").asText() : null;
                        String lyrics = songNode.has("lyrics")
                                ? songNode.get("lyrics").asText() : null;
                        String genre = songNode.has("genre")
                                ? songNode.get("genre").asText() : null;
                        Integer songReleaseYear = songNode.has("releaseYear")
                                ? songNode.get("releaseYear").asInt() : 0;

                        ArrayList<String> tags = new ArrayList<>();
                        if (songNode.has("tags") && songNode.get("tags").isArray()) {
                            for (JsonNode tagNode : songNode.get("tags")) {
                                tags.add(tagNode.asText());
                            }
                        }

                        songs.add(new Song(songName, duration, album, tags,
                                lyrics, genre, songReleaseYear, artist));
                    }
                }

                return new AddAlbumCommand(username, timestamp, albumName,
                        releaseYear, description, songs);
            case "showAlbums":
                return new ShowAlbumsCommand(username, timestamp);
            case "printCurrentPage":
                return new PrintCurrentPageCommand(username, timestamp);
            case "addEvent":
                String eventName = jsonNode.has("name") ? jsonNode.get("name").asText() : null;
                String eventDescription = jsonNode.has("description")
                        ? jsonNode.get("description").asText() : null;
                String eventDate = jsonNode.has("date") ? jsonNode.get("date").asText() : null;
                return new AddEventCommand(username, timestamp,
                        eventName, eventDescription, eventDate);
            case "addMerch":
                String merchName = jsonNode.has("name") ? jsonNode.get("name").asText() : null;
                description = jsonNode.has("description")
                        ? jsonNode.get("description").asText() : null;
                int price = jsonNode.has("price") ? jsonNode.get("price").asInt() : 0;
                return new AddMerchCommand(username, timestamp, merchName, description, price);
            case "getAllUsers":
                return new GetAllUsersCommand(username, timestamp);
            default:
                return new UnknownCommand(username, timestamp);
        }
    }
}
