package app.command.searchBar;

import app.entities.audio.collection.Library;
import app.entities.audio.collection.Podcast;
import app.entities.audio.file.AudioFile;
import app.entities.audio.file.Song;
import app.entities.Command;
import app.entities.SearchBar;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
/**
 * JAVADOC
 */
public class SearchCommand extends Command {
    private String type;
    private String filterName;
    private String filterAlbum;
    private String filterLyrics;
    private String filterGenre;
    private String filterReleaseYear;
    private String filterArtist;
    private ArrayNode filterTags;
    private String filterOwner;
    private static final int MAX_FILTER_LENGTH = 5;

    /**
     * JAVADOC
     */
    public SearchCommand(final String username,
                         final int timestamp,
                         final String type,
                         final String filterName,
                         final String filterAlbum,
                         final String filterLyrics,
                         final String filterGenre,
                         final String filterReleaseYear,
                         final String filterArtist,
                         final ArrayNode filterTags,
                         final String filterOwner) {
        super(username, timestamp);
        this.type = type;
        this.filterName = filterName;
        this.filterAlbum = filterAlbum;
        this.filterLyrics = filterLyrics;
        this.filterGenre = filterGenre;
        this.filterReleaseYear = filterReleaseYear;
        this.filterArtist = filterArtist;
        this.filterTags = filterTags;
        this.filterOwner = filterOwner;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        SearchBar searchBar = new SearchBar(library);
        ArrayList<AudioFile> combinedResults = new ArrayList<>();

        if ("song".equals(type)) {
            ArrayList<Song> filteredSongs = new ArrayList<>(library.getSongs());

            if (filterName != null) {
                filteredSongs.retainAll(searchBar.searchSongsByName(filterName));
            }
            if (filterAlbum != null) {
                filteredSongs.retainAll(searchBar.searchSongsByAlbum(filterAlbum));
            }
            if (filterTags != null) {
                ArrayList<String> tagsList = convertArrayNodeToArrayList(filterTags);
                filteredSongs.retainAll(searchBar.searchSongsByTags(tagsList));
            }
            if (filterLyrics != null) {
                filteredSongs.retainAll(searchBar.searchSongsByLyrics(filterLyrics));
            }
            if (filterGenre != null) {
                filteredSongs.retainAll(searchBar.searchSongsByGenre(filterGenre));
            }
            if (filterReleaseYear != null) {
                filteredSongs.retainAll(searchBar.searchSongsByReleaseYear(filterReleaseYear));
            }
            if (filterArtist != null) {
                filteredSongs.retainAll(searchBar.searchSongsByArtist(filterArtist));
            }

            combinedResults.addAll(filteredSongs);
        }

        if ("podcast".equals(type)) {
            ArrayList<Podcast> filteredPodcasts = new ArrayList<>();

            if (filterName != null) {
                filteredPodcasts.addAll(searchBar.searchPodcastsByName(filterName));
            }
            if (filterOwner != null) {
                filteredPodcasts.addAll(searchBar.searchPodcastsByOwner(filterOwner));
            }

            combinedResults.addAll(filteredPodcasts);
        }

        combinedResults = new ArrayList<>(combinedResults.subList(0,
                Math.min(MAX_FILTER_LENGTH, combinedResults.size())));

        SelectCommand.updateLastSearchResults(combinedResults);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "search");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", "Search returned " + combinedResults.size() + " results");

        ArrayNode resultsArray = resultNode.putArray("results");
        for (AudioFile audioFile : combinedResults) {
            resultsArray.add(audioFile.getName());
        }
    }

    /**
     * JAVADOC
     */
    private ArrayList<String> convertArrayNodeToArrayList(final ArrayNode arrayNode) {
        ArrayList<String> list = new ArrayList<>();
        if (arrayNode != null) {
            for (JsonNode node : arrayNode) {
                list.add(node.asText());
            }
        }
        return list;
    }
}
