package toromu.familymap.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Person Class
 * Created by Austin on 05-Dec-16.
 */
public class Person {
    String firstName;
    String lastName;
    String personId;
    Gender gender;
    String fatherId;
    String motherId;
    String spouseId;
    HashSet<Event> events;

    public Person(String firstName, String lastName, String personId, Gender gender, String fatherId, String motherId, String spouseId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personId = personId;
        this.gender = gender;
        this.fatherId = fatherId;
        this.motherId = motherId;
        this.spouseId = spouseId;
        events = new HashSet<>();
    }

    public enum Gender {
        MALE,
        FEMALE
    }

    public void addEvent(Event event) { events.add(event); }

    public HashSet<Event> getEvents() { return events; }
}
