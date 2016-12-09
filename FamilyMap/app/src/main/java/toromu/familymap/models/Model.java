package toromu.familymap.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

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
    HashSet<Person> personSet;
    HashSet<Event> eventSet;
    HashSet<String> motherSide;
    HashSet<String> eventTypes;
    Person currentPerson;
    Event currentEvent;
    String username;
    String password;
    String server;
    String rootPersonId;
    boolean peopleLoaded;
    boolean eventsLoaded;
    boolean authorized;

    private Model() {
        people = new HashMap<>();
        events = new HashMap<>();
        personSet = new HashSet<>();
        eventSet = new HashSet<>();
        eventTypes = new HashSet<>();
        motherSide = new HashSet<>();
        currentPerson = null;
        currentEvent = null;
        peopleLoaded = false;
        eventsLoaded = false;
        authorized = false;
    }

    public void setRootPersonId(String rootPersonId) {
        this.rootPersonId = rootPersonId;
    }

    public void loadPersons(String data) {
        people = new HashMap<>();
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
                personSet.add(people.get(id));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(people.size() > 0) peopleLoaded = true;
        constructMotherSide();
    }

    public HashMap<String, Person> getPeople() {
        if(peopleLoaded) return people;
        else return null;
    }

    public HashMap<String, Event> getEvents() {
        if(eventsLoaded) return events;
        else return null;
    }

    public HashSet<Person> getPersonSet() {
        return personSet;
    }

    public HashSet<Event> getEventSet() {
        return eventSet;
    }

    public ArrayList<String> getCredentials() {
        return new ArrayList<>(Arrays.asList(username, password, server));
    }

    public void setCredentials(String username, String password, String server) {
        this.username = username;
        this.password = password;
        this.server = server;
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

    public HashSet<String> getEventTypes() { return eventTypes; }

    public void reinitialize() {
        currentEvent = null;
        currentPerson = null;
    }

    public void loadEvents(String data) {
        events = new HashMap<>();
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
                String description = object.getString("description").toLowerCase();
                eventTypes.add(description);
                int year = -1;
                if(object.has("year")) year = Integer.parseInt(object.getString("year"));
                Event event = new Event(id, personId, latitude, longitude, country, city, description, year);
                events.put(id, event);
                eventSet.add(events.get(id));
                people.get(personId).addEvent(event);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        if(events.size() > 0) eventsLoaded = true;
    }

    public HashSet<String> getMotherSide() {
        return motherSide;
    }

    private void constructMotherSide() {
        Person rootMother = people.get(rootPersonId).getMother();
        ArrayList<String> familyTree = new ArrayList<>();
        if(rootMother != null) familyTree.add(rootMother.getPersonId());
        for(int i=0; i<familyTree.size(); i++) {
            Person mother = people.get(familyTree.get(i)).getMother();
            Person father = people.get(familyTree.get(i)).getFather();
            if(mother!=null) familyTree.add(mother.getPersonId());
            if(father!=null) familyTree.add(father.getPersonId());
        }
        for(int i=0; i<familyTree.size(); i++) motherSide.add(familyTree.get(i));
    }
}
