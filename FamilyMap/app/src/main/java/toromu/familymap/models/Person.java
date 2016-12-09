package toromu.familymap.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() { return firstName+" "+lastName; }

    public String getPersonId() { return personId; }

    public boolean isFemale() { return (gender == Gender.FEMALE); }

    public Person getFather() { return Model.SINGLETON.getPeople().get(fatherId); }

    public Person getMother() { return Model.SINGLETON.getPeople().get(motherId); }

    public Person getSpouse() { return Model.SINGLETON.getPeople().get(spouseId); }
}
