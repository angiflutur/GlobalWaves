package app.command.user.host;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import app.entities.audio.collection.Podcast;
import app.entities.audio.file.PodcastEpisode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Comanda care adaugă un podcast unui utilizator de tip HOST.
 */
public class AddPodcastCommand extends Command {
    private final String podcastName;
    private final List<PodcastEpisode> episodes;

    /**
     * Constructor pentru comanda AddPodcastCommand.
     *
     * @param username     Numele utilizatorului care adaugă podcastul
     * @param timestamp    Timpul la care comanda este emisă
     * @param podcastName  Numele podcastului
     * @param episodes     Lista de episoade asociate podcastului
     */
    public AddPodcastCommand(final String username, final Integer timestamp,
                             final String podcastName, final List<PodcastEpisode> episodes) {
        super(username, timestamp);
        this.podcastName = podcastName;
        this.episodes = episodes;
    }

    /**
     * Execută comanda pentru a adăuga podcastul în bibliotecă.
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        ObjectNode result = output.addObject();
        result.put("command", "addPodcast");
        result.put("user", getUsername());
        result.put("timestamp", getTimestamp());

        User user = library.getUser(getUsername());
        if (user == null) {
            result.put("message", "The username " + getUsername() + " doesn't exist.");
            return;
        }

        if (!user.getType().equals(User.UserType.HOST)) {
            result.put("message", getUsername() + " is not a host.");
            return;
        }

        for (Podcast podcast : user.getPodcasts()) {
            if (podcast.getName().equals(podcastName)) {
                result.put("message", getUsername() + " has another podcast with the same name.");
                return;
            }
        }

        Set<String> episodeNames = new HashSet<>();
        for (PodcastEpisode episode : episodes) {
            if (!episodeNames.add(episode.getName())) {
                result.put("message", getUsername()
                        + " has duplicate episodes in the podcast " + podcastName + ".");
                return;
            }
        }

        Podcast newPodcast = new Podcast(podcastName, getUsername(), new ArrayList<>(episodes));
        user.addPodcast(newPodcast, library);
//        if(getTimestamp() == 53){
//            System.out.println(newPodcast);
//        }
        result.put("message", getUsername() + " has added new podcast successfully.");
    }
}
