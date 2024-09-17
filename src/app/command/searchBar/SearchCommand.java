package app.command.searchBar;

import app.entities.Player;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
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
    private static ArrayList<AudioFile> lastSearchResultsAudio = new ArrayList<>();
    private static ArrayList<Playlist> lastSearchResultsPlaylists = new ArrayList<>();

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
        Player player = Player.getInstance();

        if (player.isLoaded()) {
            if (player.getCurrentAudio() instanceof Podcast) {
                player.updateRemainingTime(getTimestamp());
            }
            player.setLoaded(false);
            player.setPaused(true);
        }

        SearchBar searchBar = new SearchBar(library);
        ArrayList<AudioFile> combinedResultsAudio = new ArrayList<>();
        ArrayList<Playlist> combinedResultsPlaylists = new ArrayList<>();

        if ("song".equals(type)) {
            ArrayList<Song> filteredSongs = new ArrayList<>(library.getSongs());

            if (filterName != null) {
                filteredSongs.retainAll(searchBar.searchSongsByName(filterName));
            }
            if (filterAlbum != null) {
                filteredSongs.retainAll(searchBar.searchSongsByAlbum(filterAlbum));
            }
            if (filterTags != null) {
                ArrayList<Song> tagFilteredSongs = new ArrayList<>();
                ArrayList<String> tagsList = new ArrayList<>();
                for (JsonNode tagNode : filterTags) {
                    tagsList.add(tagNode.asText());
                }
                tagFilteredSongs.addAll(searchBar.searchSongsByTags(tagsList));
                filteredSongs.retainAll(tagFilteredSongs);
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
                filteredSongs.removeIf(song -> !song.getArtist().equals(filterArtist));
            }

            combinedResultsAudio.addAll(filteredSongs);
        }

        if ("podcast".equals(type)) {
            ArrayList<Podcast> filteredPodcasts = new ArrayList<>();

            if (filterName != null) {
                filteredPodcasts.addAll(searchBar.searchPodcastsByName(filterName));
            }
            if (filterOwner != null) {
                filteredPodcasts.addAll(searchBar.searchPodcastsByOwner(filterOwner));
            }

            for (Podcast podcast : filteredPodcasts) {
                combinedResultsAudio.add(podcast);
            }
        }

        if ("playlist".equals(type)) {
            ArrayList<Playlist> filteredPlaylists = new ArrayList<>(library.getPlaylists().values());

            if (filterName != null) {
                filteredPlaylists.retainAll(searchBar.searchPlaylistsByName(filterName));
            }
            if (filterOwner != null) {
                filteredPlaylists.retainAll(searchBar.searchPlaylistsByOwner(filterOwner));
            }

            combinedResultsPlaylists.addAll(filteredPlaylists);
        }

        combinedResultsAudio = new ArrayList<>(combinedResultsAudio.subList(0,
                Math.min(MAX_FILTER_LENGTH, combinedResultsAudio.size())));

        updateLastSearchResults(combinedResultsAudio, combinedResultsPlaylists);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "search");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        ArrayNode resultsArray = resultNode.putArray("results");

        for (AudioFile audioFile : combinedResultsAudio) {
            resultsArray.add(audioFile.getName());
        }

        if ("playlist".equals(type)) {
            for (Playlist playlist : combinedResultsPlaylists) {
                resultsArray.add(playlist.getName());
            }
        }

        int totalResults = combinedResultsAudio.size() + combinedResultsPlaylists.size();
        resultNode.put("message", "Search returned " + totalResults + " results");
    }

    /**
     * JAVADOC
     */
    public static void updateLastSearchResults(final ArrayList<AudioFile> searchResultsAudio,
                                               final ArrayList<Playlist> searchResultsPlaylists) {
        lastSearchResultsAudio = searchResultsAudio;
        lastSearchResultsPlaylists = searchResultsPlaylists;
    }

    /**
     * JAVADOC
     */
    public static ArrayList<AudioFile> getLastSearchResultsAudio() {
        return lastSearchResultsAudio;
    }

    /**
     * JAVADOC
     */
    public static ArrayList<Playlist> getLastSearchResultsPlaylists() {
        return lastSearchResultsPlaylists;
    }
}
