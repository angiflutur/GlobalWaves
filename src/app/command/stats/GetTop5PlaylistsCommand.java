package app.command.stats;

import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.audio.collection.Playlist;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JAVADOC
 */
public class GetTop5PlaylistsCommand extends Command {
    private static final int MAX_FILTER_LENGTH = 5;

    /**
     * JAVADOC
     */
    public GetTop5PlaylistsCommand(final Integer timestamp) {
        super(null, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        List<Playlist> topPlaylists = library.getPlaylists().values().stream()
                .filter(Playlist::isPublic)
                .sorted(Comparator.comparingInt(Playlist::getFollowers).reversed()
                        .thenComparing(Comparator.comparingLong(Playlist::getCreationTime)))
                .limit(MAX_FILTER_LENGTH)
                .collect(Collectors.toList());

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "getTop5Playlists");
        resultNode.put("timestamp", getTimestamp());

        ArrayNode resultArray = resultNode.putArray("result");
        for (Playlist playlist : topPlaylists) {
            resultArray.add(playlist.getName());
        }

        if (topPlaylists.isEmpty()) {
            resultArray.add("No playlists available.");
        }
    }
}
