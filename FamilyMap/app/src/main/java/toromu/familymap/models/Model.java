package toromu.familymap.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Model
 * Contains all information for people and events
 * Utilizes the Person and Event class
 * Created by Austin on 05-Dec-16.
 */
public class Model {
    public static final Model SINGLETON = new Model();
    HashMap<String, Person> people;
    HashMap<String, Event> events;
    Person currentPerson;
    Event currentEvent;
    boolean peopleLoaded;
    boolean eventsLoaded;
    boolean authorized;

    private Model() {
        people = new HashMap<>();
        events = new HashMap<>();
        currentPerson = null;
        currentEvent = null;
        peopleLoaded = false;
        eventsLoaded = false;
        authorized = false;
    }

    public void loadPersons(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("data");
            for(int i=0; i<array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String id = object.getString("personID");
                String firstName = object.getString("firstName");
                String lastName = object.getString("lastName");
                Person.Gender gender = (object.getString("gender").contains("m") ? Person.Gender.MALE : Person.Gender.FEMALE);
                String father = (object.has("father") ? object.getString("father") : null);
                String mother = (object.has("mother") ? object.getString("mother") : null);
                String spouse = (object.has("spouse") ? object.getString("spouse") : null);
                people.put(id, new Person(firstName, lastName, id, gender, father, mother, spouse));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(people.size() > 0) peopleLoaded = true;
    }

    public HashMap<String, Person> getPeople() {
        if(peopleLoaded) return people;
        else return null;
    }

    public HashMap<String, Event> getEvents() {
        if(eventsLoaded) return events;
        else return null;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public void setCurrentPerson(String currentPerson) {
        this.currentPerson = people.get(currentPerson);
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public void setAuthorized(boolean authorized) { this.authorized = authorized; }

    public boolean getAuthorized() { return authorized; }

    public void reinitialize() {
        currentEvent = null;
        currentPerson = null;
    }

    public void loadEvents(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray array = jsonObject.getJSONArray("data");
            for(int i=0; i<array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                String id = object.getString("eventID");
                String personId = object.getString("personID");
                double latitude = object.getDouble("latitude");
                double longitude = object.getDouble("longitude");
                String country = object.getString("country");
                String city = object.getString("city");
                String description = object.getString("description");
                int year = -1;
                if(object.has("year")) year = Integer.parseInt(object.getString("year"));
                Event event = new Event(id, personId, latitude, longitude, country, city, description, year);
                events.put(id, event);
                people.get(personId).addEvent(event);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(events.size() > 0) eventsLoaded = true;


    }
}
