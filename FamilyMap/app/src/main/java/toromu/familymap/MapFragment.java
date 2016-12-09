package toromu.familymap;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import toromu.familymap.models.Event;
import toromu.familymap.models.MapSettings;
import toromu.familymap.models.Model;
import toromu.familymap.models.Person;

/**
 * Google Map fragment
 * Created by Austin on 05-Dec-16.
 */

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    SupportMapFragment smf;
    GoogleMap mMap;
    TextView personName;
    TextView eventDetails;
    ImageView personIcon;
    HashMap<String, Float> colorCoordinator;
    HashMap<Marker, Event> markerEvents;
    Marker currentMarker;
    boolean isMapActivity;
    HashMap<String, Integer> lineColors = new HashMap<>();
    HashSet<Polyline> lines = new HashSet<>();
    ArrayList<Float> colors = new ArrayList<>(Arrays.asList(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_ROSE));
    public MapFragment() {
        isMapActivity = false;
        lineColors.put("Red", Color.RED);
        lineColors.put("Green", Color.GREEN);
        lineColors.put("Blue", Color.BLUE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(Model.SINGLETON.getCurrentEvent() == null) inflater.inflate(R.menu.map_fragment_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.search:
                intent = new Intent(this.getActivity(), SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.filter:
                intent = new Intent(this.getActivity(), FilterActivity.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(this.getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.go_to_top:
                Model.SINGLETON.reinitialize();
                intent = new Intent(this.getActivity(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            case android.R.id.home:
                this.getActivity().finish();
                break;
            default:
                Toast.makeText(getContext(), "Error: Something weird happened", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void setListener(GoogleMap map) { map.setOnMarkerClickListener(this); }

    public void openPersonActivity() {
        if(currentMarker != null) {
            Model.SINGLETON.setCurrentPerson(markerEvents.get(currentMarker).getPersonId());
            Intent intent = new Intent(this.getActivity(), PersonActivity.class);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        personName = (TextView) view.findViewById(R.id.personName);
        eventDetails = (TextView) view.findViewById(R.id.eventDetails);
        personIcon = (ImageView) view.findViewById(R.id.personIcon);
        smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        setMap();
        return view;
    }

    public void setMap() {
        smf.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                initializeMap(googleMap);
            }
        });
    }

    public void initializeMap(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MapSettings.SINGLETON.getMapType());
        setListener(mMap);
//                HashSet<Event> events = (HashSet<Event>) Model.SINGLETON.getEvents().values();
//                for(Event event : events) continue;
        HashMap<String, Event> events = null;
        HashMap<String, Person> people = null;
        while(events == null) events = Model.SINGLETON.getEvents();
        while(people == null) people = Model.SINGLETON.getPeople();
        colorCoordinator = new HashMap<>();
        markerEvents = new HashMap<>();
        currentMarker = null;
        int currentColor = 0;
        Marker marker;
        for(String key : events.keySet()) {
            Event event = events.get(key);
            String description = event.getDescription();
            if(!colorCoordinator.containsKey(description)) {
                colorCoordinator.put(description, colors.get(currentColor));
                currentColor++;
            }
            Person person = Model.SINGLETON.getPeople().get(event.getPersonId());
            HashSet<String> side = Model.SINGLETON.getMotherSide();
            boolean isMotherSide = Model.SINGLETON.getMotherSide().contains(person.getPersonId());
            if(person.isFemale() && !MapSettings.SINGLETON.isShowFemales()) continue;
            else if(!person.isFemale() && !MapSettings.SINGLETON.isShowMales()) continue;
            else if(isMotherSide && !MapSettings.SINGLETON.isShowMotherSide()) continue;
            else if(!isMotherSide && !MapSettings.SINGLETON.isShowFatherSide()) continue;
            else if(MapSettings.SINGLETON.getEventTypeExclusions().contains(event.getDescription())) continue;
            marker = mMap.addMarker(new MarkerOptions().position(event.getLatLng()).title(event.getPersonId()).icon(BitmapDescriptorFactory.defaultMarker(colorCoordinator.get(description))));
            markerEvents.put(marker, event);
        }
        Event currentEvent = Model.SINGLETON.getCurrentEvent();
        if(currentEvent != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(currentEvent.getLatLng()));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(markerEvents.containsKey(marker)) {
            Event event = markerEvents.get(marker);
            Person person = Model.SINGLETON.getPeople().get(event.getPersonId());
            personName.setText(person.getName());
            eventDetails.setText(event.getEventDetails());
            if(person.isFemale()) personIcon.setImageResource(R.drawable.female);
            else personIcon.setImageResource(R.drawable.male);
            currentMarker = marker;
            drawLines(person);
            return true;
        } else return false;
    }

    @Override
    public void onResume() {
        if(mMap != null) {
            mMap.clear();
            mMap.setMapType(MapSettings.SINGLETON.getMapType());
            initializeMap(mMap);
        }
        super.onResume();
    }

    private ArrayList<Event> orderEvents(HashSet<Event> personEvents) {
        TreeMap<Integer, Event> yearEvents = new TreeMap<>();
        TreeMap<String, Event> yearlessEvents = new TreeMap<>();
        ArrayList<Event> orderedEvents = new ArrayList<>();
        Event death = null;
        for(Event event : personEvents) {
            if(event.getDescription().toLowerCase().equals("birth")) orderedEvents.add(event);
            else if(event.getDescription().toLowerCase().equals("death")) death = event;
            else {
                if(event.getYear() >= 0) yearEvents.put(event.getYear(), event);
                else yearlessEvents.put(event.getDescription(), event);
            }
        }
        for(int i : yearEvents.keySet()) orderedEvents.add(yearEvents.get(i));
        for(String s : yearlessEvents.keySet()) orderedEvents.add(yearlessEvents.get(s));
        if(death != null) orderedEvents.add(death);
        return orderedEvents;
    }

    void drawLines(Person person) {
        for(Polyline polyline : lines) polyline.remove();
        if(MapSettings.SINGLETON.isLifeStoryLines()) {
            ArrayList<Event> personEvents = orderEvents(person.getEvents());
            for(int i=1; i<personEvents.size(); i++) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(personEvents.get(i-1).getLatLng(), personEvents.get(i).getLatLng())
                        .width(15)
                        .color(lineColors.get(MapSettings.SINGLETON.getLifeStoryLinesColor())));
                lines.add(line);
            }
        }
        if(MapSettings.SINGLETON.isFamilyTreeLines()) {
            recursiveDrawLine(person, markerEvents.get(currentMarker), 20);
        }
        if(MapSettings.SINGLETON.isSpouseLines()) {
            Person spouse = person.getSpouse();
            if(spouse != null) {
                ArrayList<Event> spouseEvents = orderEvents(spouse.getEvents());
                if(spouseEvents.size() > 1) {
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(currentMarker.getPosition(), spouseEvents.get(0).getLatLng())
                            .width(15)
                            .color(lineColors.get(MapSettings.SINGLETON.getSpouseLinesColor())));
                    lines.add(line);
                }
            }
        }
    }

    public void recursiveDrawLine(Person person, Event event, int thickness) {
        if(thickness <= 0) return;
        Person father = person.getFather();
        Person mother = person.getMother();
        if(father != null) {
            ArrayList<Event> fatherEvents = orderEvents(father.getEvents());
            if(fatherEvents.size() > 0) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(event.getLatLng(), fatherEvents.get(0).getLatLng())
                        .width(thickness)
                        .color(lineColors.get(MapSettings.SINGLETON.getFamilyTreeLinesColor())));
                lines.add(line);
                recursiveDrawLine(father, fatherEvents.get(0), thickness-5);
            }
        }
        if(mother != null) {
            ArrayList<Event> motherEvents = orderEvents(mother.getEvents());
            if(motherEvents.size() > 0) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(event.getLatLng(), motherEvents.get(0).getLatLng())
                        .width(thickness)
                        .color(lineColors.get(MapSettings.SINGLETON.getFamilyTreeLinesColor())));
                lines.add(line);
                recursiveDrawLine(mother, motherEvents.get(0), thickness-5);
            }
        }
    }
}
