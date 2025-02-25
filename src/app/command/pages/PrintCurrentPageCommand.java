package app.command.pages;

import app.command.searchBar.SelectCommand;
import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Podcast;
import app.entities.audio.file.Song;
import app.entities.audio.collection.Playlist;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Command to print the current page content for a user
 */
public class PrintCurrentPageCommand extends Command {
    private static final int PARTS_COUNT = 3;

    /**
     * Constructor for the command
     */
    public PrintCurrentPageCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     * Executes the command to print the current page
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());
        ObjectNode resultNode = output.addObject();
        resultNode.put("user", getUsername());
        resultNode.put("command", "printCurrentPage");
        resultNode.put("timestamp", getTimestamp());

        if (user == null) {
            resultNode.put("message", "User not found.");
            return;
        }

        if (!user.isOnline()) {
            resultNode.put("message", user.getUsername() + " is offline.");
            return;
        }

        StringBuilder pageContent = new StringBuilder();
        User.PageType currentPage = user.getCurrentPage();

        switch (currentPage) {
            case HOME_PAGE:
                List<Song> likedSongs = user.getLikedSongs();
                pageContent.append("Liked songs:\n\t")
                        .append(likedSongs.isEmpty() ? "[]" : "["
                                + likedSongs.stream()
                                .map(Song::getName)
                                .collect(Collectors.joining(", ")) + "]")
                        .append("\n\n");

                List<Playlist> followedPlaylists = user.getFollowedPlaylists();
                pageContent.append("Followed playlists:\n\t")
                        .append(followedPlaylists.isEmpty() ? "[]" : "["
                                + followedPlaylists.stream()
                                .map(playlist -> playlist.getName() + " - "
                                        + playlist.getOwner().getUsername())
                                .collect(Collectors.joining(", ")) + "]");

                break;

            case LIKED_CONTENT_PAGE:
                List<Song> likedSongsWithArtists = user.getLikedSongs();
                pageContent.append("Liked Songs:\n\t")
                        .append(likedSongsWithArtists.isEmpty() ? "[]" : "["
                                + likedSongsWithArtists.stream()
                                .map(song -> song.getName() + " - " + song.getArtist())
                                .collect(Collectors.joining(", ")) + "]")
                        .append("\n\n");

                List<Playlist> followedPlaylistsWithOwners = user.getFollowedPlaylists();
                pageContent.append("Followed Playlists:\n\t")
                        .append(followedPlaylistsWithOwners.isEmpty() ? "[]" : "["
                                + followedPlaylistsWithOwners.stream()
                                .map(playlist -> playlist.getName() + " - "
                                        + playlist.getOwner().getUsername())
                                .collect(Collectors.joining(", ")) + "]");

                break;

            case ARTIST_PAGE:
                User artist = library.getUser(SelectCommand.getSelectedArtist().getUsername());
                List<Album> albums = artist.getAlbums();
                pageContent.append("Albums:\n\t")
                        .append(albums.isEmpty() ? "[]" : "[" + albums.stream()
                                .map(Album::getName)
                                .collect(Collectors.joining(", ")) + "]");

                List<String> merchItems = artist.getMerch().stream()
                        .map(item -> item.get(0) + " - " + item.get(2)
                                + ":\n\t" + item.get(1))
                        .collect(Collectors.toList());
                pageContent.append("\n\nMerch:\n\t")
                        .append(merchItems.isEmpty() ? "[]" : "["
                                + String.join(", ", merchItems) + "]");

                List<String> eventItems = artist.getEvents().stream()
                        .map(event -> event.get(0) + " - " + event.get(1)
                                + ":\n\t" + event.get(2))
                        .collect(Collectors.toList());
                pageContent.append("\n\nEvents:\n\t")
                        .append(eventItems.isEmpty() ? "[]" : "["
                                + String.join(", ", eventItems) + "]");
                break;
            case HOST_PAGE:
                User host = library.getUser(SelectCommand.getSelectedHost().getUsername());
                List<Podcast> podcasts = host.getPodcasts();
                pageContent.append("Podcasts:\n\t")
                        .append(podcasts.isEmpty() ? "[]" : "[" + podcasts.stream()
                                .map(podcast -> podcast.getName() + ":\n\t["
                                        + podcast.getEpisodes().stream()
                                                .map(episode -> episode.getName() + " - "
                                                        + episode.getDescription())
                                                .collect(Collectors.joining(", ")) + "]")
                                .collect(Collectors.joining("\n, ")) + "\n]")
                        .append("\n\n");


                List<List<String>> announcements = host.getAnnouncements();
                pageContent.append("Announcements:\n\t")
                        .append(announcements.isEmpty() ? "[]" : "[" + announcements.stream()
                                .map(announcement -> announcement.get(0) + ":\n\t"
                                        + announcement.get(1))
                                .collect(Collectors.joining("\n")) + "\n]");
                break;

            default:
                pageContent.append("Invalid page.");
        }

        resultNode.put("message", pageContent.toString());
    }
}
