package toromu.familymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import toromu.familymap.models.Event;
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
    HashMap<String, Float> colorCoordinator;
    HashMap<Marker, Event> markerEvents;
    Marker currentMarker;
    boolean isMapActivity;
    ArrayList<Float> colors = new ArrayList<>(Arrays.asList(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_GREEN, BitmapDescriptorFactory.HUE_BLUE, BitmapDescriptorFactory.HUE_MAGENTA, BitmapDescriptorFactory.HUE_YELLOW, BitmapDescriptorFactory.HUE_CYAN, BitmapDescriptorFactory.HUE_VIOLET, BitmapDescriptorFactory.HUE_ORANGE, BitmapDescriptorFactory.HUE_AZURE, BitmapDescriptorFactory.HUE_ROSE));
    public MapFragment() {
        isMapActivity = false;
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
        smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
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
                    marker = mMap.addMarker(new MarkerOptions().position(event.getLatLng()).title(event.getPersonId()).icon(BitmapDescriptorFactory.defaultMarker(colorCoordinator.get(description))));
                    markerEvents.put(marker, event);
                }
                Event currentEvent = Model.SINGLETON.getCurrentEvent();
                if(currentEvent != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(currentEvent.getLatLng()));
            }
        });
        return view;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if(markerEvents.containsKey(marker)) {
            Event event = markerEvents.get(marker);
            personName.setText(Model.SINGLETON.getPeople().get(event.getPersonId()).getName());
            eventDetails.setText(event.getEventDetails());
            currentMarker = marker;
            return true;
        } else return false;
    }
}
