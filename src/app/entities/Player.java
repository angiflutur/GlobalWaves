package app.entities;

import app.entities.audio.collection.Podcast;
import app.entities.audio.file.AudioFile;
import app.entities.audio.collection.Playlist;
import app.entities.audio.file.PodcastEpisode;
import app.entities.audio.file.Song;

import java.util.ArrayList;

/**
 * JAVADOC
 */
public class Player {
    private AudioFile currentAudio;
    private Playlist currentPlaylist;
    private boolean isPaused;
    private boolean isLoaded;
    private int remainingTime;
    private int lastUpdateTimestamp;
    private static Player instance = new Player();
    private int currentIndex;
    private int repeatState;
    private boolean isShuffleActive;
    private ArrayList<Integer> shuffleIndices;

    private boolean isSearching = false;
    private ArrayList<AudioFile> lastSearchResultsAudio = new ArrayList<>();
    private ArrayList<Playlist> lastSearchResultsPlaylists = new ArrayList<>();
    private ArrayList<User> lastSearchResultsArtists = new ArrayList<>();

    /**
     * JAVADOC
     */
    public Player() {
        this.currentAudio = null;
        this.currentPlaylist = null;
        this.isPaused = false;
        this.isLoaded = false;
        this.remainingTime = 0;
        this.lastUpdateTimestamp = 0;
        this.currentIndex = 0;
        this.repeatState = 0;
        this.isShuffleActive = false;
        this.shuffleIndices = new ArrayList<>();
    }

    /**
     * JAVADOC
     */
    public static Player getInstance() {
        return instance;
    }

    /**
     * JAVADOC
     */
    public void nextPrevPlaylistLoad(final int index) {
        if (currentPlaylist != null && index >= 0 && index < currentPlaylist.getSongs().size()) {
            currentIndex = index;
            currentAudio = currentPlaylist.getSongs().get(index);
            remainingTime = currentAudio.getDuration();

        }
    }

    /**
     * JAVADOC
     */
    public void loadPlaylist(final Playlist playlist, final int timestamp) {
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist cannot be null");
        }

        this.currentPlaylist = playlist;
        this.currentIndex = 0;

        if (currentPlaylist.getSongs().size() > 0
                && currentIndex < currentPlaylist.getSongs().size()) {
            this.currentAudio = currentPlaylist.getSongs().get(currentIndex);
            this.remainingTime = this.currentAudio.getDuration();
        } else {
            this.currentAudio = null;
            this.remainingTime = 0;
        }

        this.isLoaded = true;
        this.isPaused = false;
        this.lastUpdateTimestamp = timestamp;
    }

    /**
     * JAVADOC
     */
    public void loadAudio(final AudioFile audio, final int timestamp) {
        if (audio == null) {
            throw new IllegalArgumentException("AudioFile cannot be null");
        }

        this.currentAudio = audio;
        this.currentPlaylist = null;

        if (audio instanceof Podcast) {
            Podcast podcast = (Podcast) audio;
            if (podcast.getCurrentEpisodeIndex() >= 0
                    && podcast.getCurrentEpisodeIndex() < podcast.getEpisodes().size()) {
                this.remainingTime = podcast.getCurrentEpisodeRemainingTime();
            } else {
                this.remainingTime = 0;
            }
        } else if (audio instanceof Song) {
            this.remainingTime = audio.getDuration();
        } else {
            this.remainingTime = audio.getDuration();
        }

        this.isLoaded = true;
        this.isPaused = false;
        this.lastUpdateTimestamp = timestamp;
    }

    /**
     * JAVADOC
     */
    public void play(final int currentTimestamp) {
        if (isPaused) {
            updateRemainingTime(currentTimestamp);
            isPaused = false;
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * JAVADOC
     */
    public void pause(final int currentTimestamp) {
        if (!isPaused) {
            updateRemainingTime(currentTimestamp);
            isPaused = true;
            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * JAVADOC
     */
    public void updateRemainingTime(final int currentTimestamp) {
        if (!isPaused && currentAudio != null && currentTimestamp != lastUpdateTimestamp) {
            int timeElapsed = currentTimestamp - lastUpdateTimestamp;

            if (currentPlaylist != null) {
                updatePlaylistRemainingTime(timeElapsed);
            } else if (currentAudio instanceof Podcast) {
                updatePodcastRemainingTime(timeElapsed);
            } else if (currentAudio instanceof Song) {
                updateSongRemainingTime(timeElapsed);
            }

            lastUpdateTimestamp = currentTimestamp;
        }
    }

    /**
     * JAVADOC
     */
    private void updatePlaylistRemainingTime(final int timeElapsed) {
        remainingTime -= timeElapsed;

        while (remainingTime <= 0) {
            if (repeatState == 1) {
                if (isShuffleActive && shuffleIndices.size() > 0) {
                    int shuffledIndex = shuffleIndices.indexOf(currentIndex);
                    if (shuffledIndex < shuffleIndices.size() - 1) {
                        currentIndex = shuffleIndices.get(shuffledIndex + 1);
                    } else {
                        currentIndex = shuffleIndices.get(0);
                    }
                } else {
                    currentIndex = (currentIndex + 1) % currentPlaylist.getSongs().size();
                }
                currentAudio = currentPlaylist.getSongs().get(currentIndex);
                remainingTime += currentAudio.getDuration();

            } else if (repeatState == 2) {
                remainingTime += currentAudio.getDuration();
            } else {
                if (isShuffleActive && shuffleIndices.size() > 0) {
                    int shuffledIndex = shuffleIndices.indexOf(currentIndex);

                    if (shuffledIndex < shuffleIndices.size() - 1) {
                        currentIndex = shuffleIndices.get(shuffledIndex + 1);
                    } else {
                        currentAudio = null;
                        remainingTime = 0;
                        isLoaded = false;
                        isPaused = false;
                        return;
                    }
                } else {
                    currentIndex++;
                    if (currentIndex >= currentPlaylist.getSongs().size()) {
                        currentAudio = null;
                        remainingTime = 0;
                        isLoaded = false;
                        isPaused = false;
                        return;
                    }
                }
                if (currentIndex >= 0 && currentIndex < currentPlaylist.getSongs().size()) {
                    currentAudio = currentPlaylist.getSongs().get(currentIndex);
                    remainingTime += currentAudio.getDuration();
                } else {
                    currentAudio = null;
                    remainingTime = 0;
                    isLoaded = false;
                    isPaused = false;
                    return;
                }
            }
        }
    }

    /**
     * JAVADOC
     */
    private void updatePodcastRemainingTime(final int timeElapsed) {
        Podcast podcast = (Podcast) currentAudio;
        int currentEpisodeIndex = podcast.getCurrentEpisodeIndex();

        if (currentEpisodeIndex >= 0 && currentEpisodeIndex < podcast.getEpisodes().size()) {
            PodcastEpisode currentEpisode = podcast.getEpisodes().get(currentEpisodeIndex);
            int currentEpisodeRemainingTime = podcast.getCurrentEpisodeRemainingTime();

            currentEpisodeRemainingTime -= timeElapsed;

            if (currentEpisodeRemainingTime <= 0) {
                if (currentEpisodeIndex < podcast.getEpisodes().size() - 1) {
                    podcast.setCurrentEpisodeIndex(currentEpisodeIndex + 1);
                    PodcastEpisode nextEpisode
                            = podcast.getEpisodes().get(podcast.getCurrentEpisodeIndex());

                    podcast.setCurrentEpisodeRemainingTime(nextEpisode.getDuration()
                            + currentEpisodeRemainingTime);
                } else {
                    currentAudio = null;
                    isLoaded = false;
                    podcast.setCurrentEpisodeRemainingTime(0);
                }
            } else {
                podcast.setCurrentEpisodeRemainingTime(currentEpisodeRemainingTime);
            }

            this.remainingTime = podcast.getCurrentEpisodeRemainingTime();
        }
    }

    /**
     * JAVADOC
     */
    private void updateSongRemainingTime(final int timeElapsed) {
        remainingTime -= timeElapsed;

        if (remainingTime <= 0) {
            if (repeatState == 1) {
                remainingTime = currentAudio.getDuration() + remainingTime;
                repeatState = 0;
            } else if (repeatState == 2) {
                remainingTime = currentAudio.getDuration()
                        - (-remainingTime % currentAudio.getDuration());
            } else {
                if (currentPlaylist != null) {
                    if (isShuffleActive) {
                        int shuffledIndex = shuffleIndices.indexOf(currentIndex);
                        if (shuffledIndex < shuffleIndices.size() - 1) {
                            currentIndex = shuffleIndices.get(shuffledIndex + 1);
                            currentAudio = currentPlaylist.getSongs().get(currentIndex);
                            remainingTime = currentAudio.getDuration();
                        } else {
                            currentAudio = null;
                            remainingTime = 0;
                            isLoaded = false;
                            isPaused = true;
                        }
                    } else {
                        currentIndex++;
                        if (currentIndex < currentPlaylist.getSongs().size()) {
                            currentAudio = currentPlaylist.getSongs().get(currentIndex);
                            remainingTime = currentAudio.getDuration();
                        } else {
                            currentAudio = null;
                            remainingTime = 0;
                            isLoaded = false;
                            isPaused = true;
                        }
                    }
                } else {
                    remainingTime = 0;
                    isPaused = true;
                }
            }
        }
    }

    /**
     * JAVADOC
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * JAVADOC
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * JAVADOC
     */
    public boolean isLoaded() {
        return isLoaded;
    }

    /**
     * JAVADOC
     */
    public void setLoaded(final boolean loaded) {
        isLoaded = loaded;
    }

    /**
     * JAVADOC
     */
    public void setPaused(final boolean paused) {
        isPaused = paused;
    }

    /**
     * JAVADOC
     */
    public AudioFile getCurrentAudio() {
        return currentAudio;
    }

    /**
     * JAVADOC
     */
    public Playlist getCurrentPlaylist() {
        return currentPlaylist;
    }

    /**
     * JAVADOC
     */
    public void setShuffleIndices(final ArrayList<Integer> shuffleIndices) {
        this.shuffleIndices = shuffleIndices;
    }

    /**
     * JAVADOC
     */
    public void setRemainingTime(final int remainingTime) {
        this.remainingTime = remainingTime;
    }

    /**
     * JAVADOC
     */
    public void setLastUpdateTimestamp(final int lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    /**
     * JAVADOC
     */
    public ArrayList<Integer> getShuffleIndices() {
        return shuffleIndices;
    }

    /**
     * JAVADOC
     */
    public void setShuffleActive(final boolean newShuffleActive) {
        this.isShuffleActive = newShuffleActive;
        if (!newShuffleActive) {
            clearShuffleIndices();
        }
    }

    /**
     * JAVADOC
     */
    private void clearShuffleIndices() {
        this.shuffleIndices.clear();
    }

    /**
     * JAVADOC
     */
    public boolean isShuffleActive() {
        return isShuffleActive;
    }

    /**
     * JAVADOC
     */
    public String getRepeatStatus() {
        if (currentPlaylist instanceof Playlist) {
            switch (repeatState) {
                case 1:
                    return "Repeat All";
                case 2:
                    return "Repeat Current Song";
                default:
                    return "No Repeat";
            }
        } else {
            switch (repeatState) {
                case 1:
                    return "Repeat Once";
                case 2:
                    return "Repeat Infinite";
                default:
                    return "No Repeat";
            }
        }
    }

    /**
     * JAVADOC
     */
    public int getRepeatState() {
        return repeatState;
    }

    /**
     * JAVADOC
     */
    public void setRepeatState(final int repeatState) {
        this.repeatState = repeatState;
    }

    /**
     * JAVADOC
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * JAVADOC
     */
    public void setCurrentPlaylist(final Playlist currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }

    /**
     * JAVADOC
     */
    public void setCurrentAudio(final AudioFile currentAudio) {
        this.currentAudio = currentAudio;
    }
    /**
     *
     */
    public void setLastSearchResultsAudio(ArrayList<AudioFile> lastSearchResultsAudio) {
        this.lastSearchResultsAudio = lastSearchResultsAudio;
    }
    /**
     *
     */
    public ArrayList<AudioFile> getLastSearchResultsAudio() {
        return this.lastSearchResultsAudio;
    }
    /**
     *
     */
    public ArrayList<Playlist> getLastSearchResultsPlaylists() {
        return lastSearchResultsPlaylists;
    }
    /**
     *
     */
    public void setLastSearchResultsPlaylists(ArrayList<Playlist> lastSearchResultsPlaylists) {
        this.lastSearchResultsPlaylists = lastSearchResultsPlaylists;
    }
    /**
     *
     */
    public ArrayList<User> getLastSearchResultsArtists() {
        return lastSearchResultsArtists;
    }
    /**
     *
     */
    public void setLastSearchResultsArtists(ArrayList<User> lastSearchResultsArtists) {
        this.lastSearchResultsArtists = lastSearchResultsArtists;
    }
    /**
     *
     */
    public void updateLastSearchResults(ArrayList<AudioFile> audioFiles, ArrayList<Playlist> playlists) {
        this.lastSearchResultsAudio = audioFiles;
        this.lastSearchResultsPlaylists = playlists;
    }
    /**
     *
     */
    public void updateLastSearchArtists(ArrayList<User> artists) {
        this.lastSearchResultsArtists = artists;
    }
    /**
     *
     */
    public void clearLastSearchResults() {
        this.lastSearchResultsAudio.clear();
        this.lastSearchResultsPlaylists.clear();
        this.lastSearchResultsArtists.clear();
    }
    /**
     *
     */
    public boolean getIsSearching() {
        return this.isSearching;
    }
    /**
     *
     */
    public void setIsSearching(boolean isSearching) {
        this.isSearching = isSearching;
    }
}
