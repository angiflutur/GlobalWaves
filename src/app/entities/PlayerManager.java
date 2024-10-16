package app.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * JAVADOC
 */
public final class PlayerManager {
    private static Map<String, Player> players = new HashMap<>();

    private PlayerManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * JAVADOC
     */
    public static Player getPlayer(final String username) {
        return players.computeIfAbsent(username, k -> new Player());
    }

    /**
     * JAVADOC
     */
    public static void resetPlayers() {
        players.clear();
    }
}
