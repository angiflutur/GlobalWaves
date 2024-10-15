package app.command.stats;

import app.entities.audio.collection.Library;
import app.entities.Command;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JAVADOC
 */
public class GetTop5SongsCommand extends Command {
    private static final int MAX_FILTER_LENGTH = 5;
    /**
     * JAVADOC
     */
    public GetTop5SongsCommand(final Integer timestamp) {
        super(null, timestamp);
    }

    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        List<Song> topSongs = library.getSongs().stream()
                .sorted(Comparator.comparingInt(Song::getLikeCount).reversed()
                        .thenComparing(library.getSongs()::indexOf))
                .limit(MAX_FILTER_LENGTH)
                .collect(Collectors.toList());

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "getTop5Songs");
        resultNode.put("timestamp", getTimestamp());

        ArrayNode resultArray = resultNode.putArray("result");
        for (Song song : topSongs) {
            resultArray.add(song.getName());
        }

        if (topSongs.isEmpty()) {
            resultArray.add("No songs available.");
        }
    }
}
