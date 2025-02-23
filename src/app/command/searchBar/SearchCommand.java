package app.command.searchBar;

import app.entities.Command;
import app.entities.Player;
import app.entities.PlayerManager;
import app.entities.User;
import app.entities.SearchBar;
import app.entities.audio.collection.Album;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Playlist;
import app.entities.audio.collection.Podcast;
import app.entities.audio.file.AudioFile;
import app.entities.audio.file.Song;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

/**
 * Handle the search command.
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
     * Constructor for the search command.
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
     * Execute the search command and update the output.
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        Player player = PlayerManager.getPlayer(getUsername());
        player.setIsSearching(true);
        User user = library.getUser(getUsername());
        if (user == null || !user.isOnline()) {
            ObjectNode resultNode = output.addObject();
            resultNode.put("command", "search");
            resultNode.put("user", getUsername());
            resultNode.put("timestamp", getTimestamp());
            resultNode.put("message", getUsername() + " is offline.");
            ArrayNode resultsArray = resultNode.putArray("results");
            return;
        }
        if (player.isLoaded()) {
            if (player.getCurrentAudio() instanceof Podcast) {
                player.updateRemainingTime(getTimestamp());
            }
            player.setLoaded(false);
            player.setPaused(true);
        }
        SelectCommand.setSelectedAudioFile(null);
        SelectCommand.setSelectedPlaylist(null);
        SearchBar searchBar = new SearchBar(library);
        ArrayList<AudioFile> combinedResultsAudio = new ArrayList<>();
        ArrayList<Playlist> combinedResultsPlaylists = new ArrayList<>();
        ArrayList<String> filteredArtists = new ArrayList<>();
        ArrayList<String> filteredHosts = new ArrayList<>();

        if ("song".equals(type)) {
            ArrayList<Song> filteredSongs = new ArrayList<>(library.getSongs());
            if (filterName != null) {
                filteredSongs.retainAll(searchBar.searchSongsByName(filterName));
                for (User albumOwner : library.getUsers()) {
                    for (Album album : albumOwner.getAlbums()) {
                        for (Song song : album.getSongs()) {
                            if (song.getName().toLowerCase().startsWith(filterName.toLowerCase())) {
                                if (!filteredSongs.contains(song)) {
                                    filteredSongs.add(song);
                                }
                            }
                        }
                    }
                }
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
                ArrayList<Song> genreFilteredSongs = new ArrayList<>();
                for (Song song : filteredSongs) {
                    if (song.getGenre().equalsIgnoreCase(filterGenre)) {
                        genreFilteredSongs.add(song);
                    }
                }
                filteredSongs = genreFilteredSongs;
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
            combinedResultsAudio.addAll(filteredPodcasts);
        }
        if ("playlist".equals(type)) {
            ArrayList<Playlist> filteredPlaylists
                    = new ArrayList<>(library.getPlaylists().values());
            if (filterName != null) {
                filteredPlaylists.retainAll(searchBar.searchPlaylistsByName(filterName));
            }
            if (filterOwner != null) {
                filteredPlaylists.retainAll(searchBar.searchPlaylistsByOwner(filterOwner));
            }
            filteredPlaylists.removeIf(playlist -> !playlist.isPublic()
                    && !playlist.getOwner().getUsername().equals(getUsername()));
            combinedResultsPlaylists.addAll(filteredPlaylists);
        }
        combinedResultsAudio = new ArrayList<>(combinedResultsAudio.subList(0,
                Math.min(MAX_FILTER_LENGTH, combinedResultsAudio.size())));
        player.updateLastSearchResults(combinedResultsAudio, combinedResultsPlaylists);
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
        if ("artist".equals(type)) {
            if (filterName != null && !filterName.isEmpty()) {
                for (User artist : library.getUsers()) {
                    if (artist.getType() == User.UserType.ARTIST
                            && artist.getUsername() != null
                            && artist.getUsername().toLowerCase().
                            startsWith(filterName.toLowerCase())) {
                        filteredArtists.add(artist.getUsername());
                    }
                }

                ArrayList<User> filteredArtistUsers = new ArrayList<>();
                for (String artistUsername : filteredArtists) {
                    User artist = library.getUser(artistUsername);
                    if (artist != null) {
                        filteredArtistUsers.add(artist);
                    }
                }

                for (String artist : filteredArtists) {
                    resultsArray.add(artist);
                }

                if (!filteredArtistUsers.isEmpty()) {
                    player.updateLastSearchArtists(filteredArtistUsers);
                }

                resultNode.put("message", "Search returned "
                        + filteredArtistUsers.size() + " artists");
            }
        }
        if ("host".equals(type)) {
            if (filterName != null && !filterName.isEmpty()) {
                for (User host : library.getUsers()) {
                    if (host.getType() == User.UserType.HOST
                            && host.getUsername() != null
                            && host.getUsername().toLowerCase().startsWith(filterName.toLowerCase())) {
                        filteredHosts.add(host.getUsername());
                    }
                }

                ArrayList<User> filteredHostsUsers = new ArrayList<>();
                for (String hostUsername : filteredHosts) {
                    User host = library.getUser(hostUsername);
                    if (host != null) {
                        filteredHostsUsers.add(host);
                    }
                }

                for (String host : filteredHosts) {
                    resultsArray.add(host);
                }

                if (!filteredHostsUsers.isEmpty()) {
                    player.updateLastSearchHosts(filteredHostsUsers);
                }

                resultNode.put("message", "Search returned " + filteredHostsUsers.size() + " hosts");
            }
        }


        int totalResults = combinedResultsAudio.size() + combinedResultsPlaylists.size()
                + filteredArtists.size() + filteredHosts.size();
        resultNode.put("message", "Search returned " + totalResults + " results");
    }

}
