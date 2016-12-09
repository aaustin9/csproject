package toromu.familymap;

import toromu.familymap.models.Event;
import toromu.familymap.models.Person;

/**
 * Class used with the Search Adapter Class
 * Created by Austin on 08-Dec-16.
 */
public class SearchResult {
    Person person;
    Event event;
    String line;
    String subline;
    int pictureLocation;

    public SearchResult(Person person, Event event, String line, String subline, int pictureLocation) {
        this.person = person;
        this.event = event;
        this.line = line;
        this.subline = subline;
        this.pictureLocation = pictureLocation;
    }

    public Person getPerson() {
        return person;
    }

    public Event getEvent() {
        return event;
    }

    public String getLine() {
        return line;
    }

    public String getSubline() {
        return subline;
    }

    public int getPictureLocation() {
        return pictureLocation;
    }
}
