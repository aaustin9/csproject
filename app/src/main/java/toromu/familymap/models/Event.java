package toromu.familymap.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Event class
 * Created by Austin on 05-Dec-16.
 */
public class Event {
    String eventId;
    String personId;
    double latitude;
    double longitude;
    String country;
    String city;
    String description;
    int year;

    public Event(String eventId, String personId, double latitude, double longitude, String country, String city, String description, int year) {
        this.eventId = eventId;
        this.personId = personId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.description = description;
        this.year = year;
    }

    public LatLng getLatLng() { return new LatLng(latitude, longitude); }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return city+", "+country;
    }
}
