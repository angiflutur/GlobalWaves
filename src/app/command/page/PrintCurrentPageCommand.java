package app.command.page;

import app.command.searchBar.SelectCommand;
import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.audio.file.Song;
import app.entities.audio.collection.Playlist;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class PrintCurrentPageCommand extends Command {
    private static final int PARTS_COUNT = 3;

    /**
     *
     */
    public PrintCurrentPageCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
    }

    /**
     *
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

        if (!user.isConnectionStatus()) {
            resultNode.put("message", user.getUsername() + " is offline.");
            return;
        }
        StringBuilder pageContent = new StringBuilder();

        if (SelectCommand.getSelectedArtist() != null) {
            User artist = library.getUser(SelectCommand.getSelectedArtist());

            if (artist != null) {
                List<Album> albums = artist.getAlbums();
                pageContent.append("Albums:\n\t")
                        .append(albums.isEmpty() ? "[]" : "[" + albums.stream()
                                .map(Album::getName)
                                .collect(Collectors.joining(", ")) + "]");

                List<String> merchItems = artist.getMerch();
                if (!merchItems.isEmpty()) {
                    pageContent.append("\n\nMerch:\n\t[");
                    for (int i = 0; i < merchItems.size(); i++) {
                        String[] parts = merchItems.get(i).split(" - ", PARTS_COUNT);
                        pageContent.append(parts[0]).append(" - ").append(parts[2]).append(":\n\t")
                                    .append(parts[1]);
                        if (i < merchItems.size() - 1) {
                            pageContent.append(", ");
                        }
                    }
                    pageContent.append("]");
                } else {
                    pageContent.append("\n\nMerch:\n\t[]");
                }

                if (!artist.getEvents().isEmpty()) {
                    String formattedEvents = artist.getEvents().stream()
                            .map(event -> {
                                String[] parts = event.split(" - ", PARTS_COUNT);
                                return parts[0] + " - " + parts[2] + ":\n\t" + parts[1];
                            })
                            .collect(Collectors.joining(", "));
                    pageContent.append("\n\nEvents:\n\t[").append(formattedEvents).append("]");
                } else {
                    pageContent.append("\n\nEvents:\n\t[]");
                }
            }
        } else {
            List<Song> likedSongs = user.getLikedSongs();
            pageContent.append("Liked songs:\n\t")
                    .append(likedSongs.isEmpty() ? "[]" : "[" + likedSongs.stream()
                            .map(Song::getName)
                            .collect(Collectors.joining(", ")) + "]")
                    .append("\n\n");

            List<Playlist> followedPlaylists = user.getFollowedPlaylists();
            pageContent.append("Followed playlists:\n\t")
                    .append(followedPlaylists.isEmpty() ? "[]" : "[" + followedPlaylists.stream()
                            .map(playlist -> playlist.getName() + " - "
                                    + playlist.getOwner().getUsername())
                            .collect(Collectors.joining(", ")) + "]");

            resultNode.put("message", pageContent.toString());
        }

        resultNode.put("message", pageContent.toString());
    }

}
