package app.command.searchBar;

import app.audio.collection.Library;
import app.audio.collection.Podcast;
import app.audio.file.Song;
import app.command.Command;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

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

    public SearchCommand(String username, int timestamp, String type, String filterName, String filterAlbum, String filterLyrics, String filterGenre, String filterReleaseYear, String filterArtist, ArrayNode filterTags, String filterOwner) {
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

    @Override
    public void execute(ArrayNode output, Library library) {
        SearchBar searchBar = new SearchBar(library);
        ArrayList<Song> searchResults = new ArrayList<>();
        ArrayList<Podcast> searchPodcastResults = new ArrayList<>();

        if ("song".equals(type)) {
            ArrayList<Song> filteredResults = new ArrayList<>(library.getSongs());

            if (filterName != null) {
                filteredResults.retainAll(searchBar.searchSongsByName(filterName));
            }
            if (filterAlbum != null) {
                filteredResults.retainAll(searchBar.searchSongsByAlbum(filterAlbum));
            }
            if (filterTags != null) {
                ArrayList<String> tagsList = convertArrayNodeToArrayList(filterTags);
                filteredResults.retainAll(searchBar.searchSongsByTags(tagsList));
            }
            if (filterLyrics != null) {
                filteredResults.retainAll(searchBar.searchSongsByLyrics(filterLyrics));
            }
            if (filterGenre != null) {
                filteredResults.retainAll(searchBar.searchSongsByGenre(filterGenre));
            }
            if (filterReleaseYear != null) {
                filteredResults.retainAll(searchBar.searchSongsByReleaseYear(filterReleaseYear));
            }
            if (filterArtist != null) {
                filteredResults.retainAll(searchBar.searchSongsByArtist(filterArtist));
            }

            searchResults = new ArrayList<>(filteredResults);
            searchResults = new ArrayList<>(searchResults.subList(0, Math.min(5, searchResults.size())));
        } else if ("podcast".equals(type)) {
            if (filterName != null) {
                searchPodcastResults.addAll(searchBar.searchPodcastsByName(filterName));
            }
            if (filterOwner != null) {
                searchPodcastResults.addAll(searchBar.searchPodcastsByOwner(filterOwner));
            }
            searchPodcastResults = new ArrayList<>(searchPodcastResults.subList(0, Math.min(5, searchPodcastResults.size())));
        }

        if ("song".equals(type)) {
            SelectCommand.updateLastSearchResults(searchResults);
        } else if ("podcast".equals(type)) {
            SelectCommand.updateLastSearchPodcastResults(searchResults);
        }

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "search");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());
        resultNode.put("message", "Search returned " + (searchResults.size() + searchPodcastResults.size()) + " results");

        ArrayNode resultsArray = resultNode.putArray("results");
        if ("song".equals(type)) {
            for (Song song : searchResults) {
                resultsArray.add(song.getName());
            }
        } else if ("podcast".equals(type)) {
            for (Podcast podcast : searchPodcastResults) {
                resultsArray.add(podcast.getName());
            }
        }
    }

    private ArrayList<String> convertArrayNodeToArrayList(ArrayNode arrayNode) {
        ArrayList<String> list = new ArrayList<>();
        if (arrayNode != null) {
            for (JsonNode node : arrayNode) {
                list.add(node.asText());
            }
        }
        return list;
    }
}
