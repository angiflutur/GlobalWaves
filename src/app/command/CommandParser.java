package app.command;

import app.command.searchBar.SearchCommand;
import app.command.searchBar.SelectCommand;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class CommandParser {

    public static Command createCommand(JsonNode jsonNode) {
        String commandType = jsonNode.has("command") ? jsonNode.get("command").asText() : null;
        String username = jsonNode.has("username") ? jsonNode.get("username").asText() : null;
        Integer timestamp = jsonNode.has("timestamp") ? jsonNode.get("timestamp").asInt() : null;

        if (commandType == null || username == null || timestamp == null) {
            return new UnknownCommand(username, timestamp);
        }

        switch (commandType) {
            case "search":
                String type = jsonNode.has("type") ? jsonNode.get("type").asText() : null;
                JsonNode filtersNode = jsonNode.has("filters") ? jsonNode.get("filters") : null;

                String filterName = filtersNode != null && filtersNode.has("name") ? filtersNode.get("name").asText() : null;
                String filterAlbum = filtersNode != null && filtersNode.has("album") ? filtersNode.get("album").asText() : null;
                String filterLyrics = filtersNode != null && filtersNode.has("lyrics") ? filtersNode.get("lyrics").asText() : null;
                String filterGenre = filtersNode != null && filtersNode.has("genre") ? filtersNode.get("genre").asText() : null;
                String filterReleaseYear = filtersNode != null && filtersNode.has("releaseYear") ? filtersNode.get("releaseYear").asText() : null;
                String filterArtist = filtersNode != null && filtersNode.has("artist") ? filtersNode.get("artist").asText() : null;
                ArrayNode filterTags = filtersNode != null && filtersNode.has("tags") ? (ArrayNode) filtersNode.get("tags") : null;
                String filterOwner = filtersNode != null && filtersNode.has("owner") ? filtersNode.get("owner").asText() : null;
                return new SearchCommand(username, timestamp, type, filterName, filterAlbum, filterLyrics, filterGenre, filterReleaseYear, filterArtist, filterTags, filterOwner);

            case "select":
                int itemNumber = jsonNode.has("itemNumber") ? jsonNode.get("itemNumber").asInt() : -1;
                return new SelectCommand(username, timestamp, itemNumber);

            default:
                return new UnknownCommand(username, timestamp);
        }
    }
}
