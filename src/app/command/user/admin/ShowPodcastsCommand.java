package app.command.user.admin;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ShowPodcastsCommand extends Command {

    private final String username;

    public ShowPodcastsCommand(final String username, final Integer timestamp) {
        super(username, timestamp);
        this.username = username;
    }

    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(username);

        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "showPodcasts");
        resultNode.put("user", username);
        resultNode.put("timestamp", getTimestamp());

        if (user != null && user.getType() == User.UserType.HOST) {
            ArrayNode podcastsNode = resultNode.putArray("result");

            user.getPodcasts().forEach(podcast -> {
                ObjectNode podcastNode = podcastsNode.addObject();
                podcastNode.put("name", podcast.getName());

                ArrayNode episodesNode = podcastNode.putArray("episodes");
                podcast.getEpisodes().forEach(episode -> episodesNode.add(episode.getName()));
            });
        } else {
            resultNode.put("message", "The username " + username + " is not a host.");
        }
    }
}
