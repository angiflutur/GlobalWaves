package app.command.user.artist;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 *
 */
public class AddEventCommand extends Command {
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2023;
    private static final int MIN_MONTH = 1;
    private static final int MAX_MONTH = 12;
    private static final int MIN_DAY = 1;
    private static final int MAX_DAY = 31;
    private static final int LEAP_YEAR_DIV_4 = 4;
    private static final int LEAP_YEAR_DIV_100 = 100;
    private static final int LEAP_YEAR_DIV_400 = 400;
    private static final int LEAP_YEAR_FEB_DAYS = 29;
    private static final int NON_LEAP_YEAR_FEB_DAYS = 28;


    private final String eventName;
    private final String description;
    private final String date;

    /**
     *
     */
    public AddEventCommand(final String username, final Integer timestamp,
                           final String eventName, final String description, final String date) {
        super(username, timestamp);
        this.eventName = eventName;
        this.description = description;
        this.date = date;
    }

    /**
     *
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());
        ObjectNode resultNode = output.addObject();
        resultNode.put("command", "addEvent");
        resultNode.put("user", getUsername());
        resultNode.put("timestamp", getTimestamp());

        if (user == null) {
            resultNode.put("message", "The username " + getUsername() + " doesn't exist.");
            return;
        }

        if (!user.isArtist()) {
            resultNode.put("message", getUsername() + " is not an artist.");
            return;
        }

        if (user.hasEvent(eventName)) {
            resultNode.put("message", getUsername() + " has another event with the same name.");
            return;
        }

        if (!isValidDate(date)) {
            resultNode.put("message", "Event for "
                    + getUsername() + " does not have a valid date.");
            return;
        }

        user.addEvent(eventName, description, date);
        resultNode.put("message", getUsername() + " has added new event successfully.");
    }

    /**
     *
     */
    private boolean isValidDate(final String validDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        sdf.setLenient(false);

        String[] parts = validDate.split("-");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);

        if (year < MIN_YEAR || year > MAX_YEAR) {
            return false;
        }

        if (month < MIN_MONTH || month > MAX_MONTH) {
            return false;
        }

        if (day < MIN_DAY || day > MAX_DAY) {
            return false;
        }

        if (month == 2) {
            boolean leapYear = (year % LEAP_YEAR_DIV_4 == 0 && year % LEAP_YEAR_DIV_100 != 0)
                    || (year % LEAP_YEAR_DIV_400 == 0);
            if (day > (leapYear ? LEAP_YEAR_FEB_DAYS : NON_LEAP_YEAR_FEB_DAYS)) {
                return false;
            }
        }
        return true;
    }
}
