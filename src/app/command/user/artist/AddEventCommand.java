package app.command.user.artist;

import app.entities.Command;
import app.entities.User;
import app.entities.audio.collection.Library;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
/**
 * JAVADOC
 */
public class AddEventCommand extends Command {
    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2023;
    private static final int MAX_MONTH = 12;
    private static final int MAX_DAY = 31;
    private static final int MAX_FEBRUARY_DAY = 28;

    private String eventName;
    private String eventDescription;
    private String eventDate;
    /**
     * JAVADOC
     */
    public AddEventCommand(final String username,
                           final Integer timestamp,
                           final String eventName,
                           final String eventDescription,
                           final String eventDate) {
        super(username, timestamp);
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventDate = eventDate;
    }
    /**
     * JAVADOC
     */
    @Override
    public void execute(final ArrayNode output, final Library library) {
        User user = library.getUser(getUsername());
        ArrayNode resultArray = output.objectNode().putArray("result");

        if (user == null) {
            resultArray.add("The username " + getUsername() + " doesn't exist.");
        } else if (user.getType() != User.UserType.ARTIST) {
            resultArray.add(getUsername() + " is not an artist.");
        } else {
            boolean eventExists = false;
            for (List<String> event : user.getEvents()) {
                if (event.get(0).equals(this.eventName)) {
                    eventExists = true;
                    break;
                }
            }

            if (eventExists) {
                resultArray.add(getUsername() + " has another event with the same name.");
            } else {
                if (!isValidDate(this.eventDate)) {
                    resultArray.add("Event for " + getUsername() + " does not have a valid date.");
                } else {
                    user.addEvent(this.eventName, this.eventDate, this.eventDescription);
                    resultArray.add(getUsername() + " has added new event successfully.");
                }
            }
        }

        if (resultArray.size() == 1) {
            output.add(output.objectNode()
                    .put("command", "addEvent")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .put("message", resultArray.get(0).asText()));
        } else {
            output.add(output.objectNode()
                    .put("command", "addEvent")
                    .put("user", getUsername())
                    .put("timestamp", getTimestamp())
                    .set("result", resultArray));
        }
    }
    /**
     * JAVADOC
     */
    private boolean isValidDate(final String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);

            int year = Integer.parseInt(date.split("-")[2]);
            if (year < MIN_YEAR || year > MAX_YEAR) {
                return false;
            }

            int month = Integer.parseInt(date.split("-")[1]);
            if (month < 1 || month > MAX_MONTH) {
                return false;
            }

            int day = Integer.parseInt(date.split("-")[0]);
            if (month == 2 && day > MAX_FEBRUARY_DAY) {
                return false;
            }
            if (day < 1 || day > MAX_DAY) {
                return false;
            }

            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
