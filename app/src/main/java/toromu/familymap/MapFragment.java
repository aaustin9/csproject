package toromu.familymap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.HashSet;

import toromu.familymap.models.Event;
import toromu.familymap.models.Model;
import toromu.familymap.models.Person;

/**
 * Google Map fragment
 * Created by Austin on 05-Dec-16.
 */
public class MapFragment extends Fragment {
    SupportMapFragment smf;
    GoogleMap mMap;
    boolean itWorked;
    public MapFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment, container, false);
        smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        smf.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
//                LatLng sydney = new LatLng(-34, 151);
//                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//                HashSet<Event> events = (HashSet<Event>) Model.SINGLETON.getEvents().values();
//                for(Event event : events) continue;
                HashMap<String, Event> events = null;
                HashMap<String, Person> people = null;
                while(events == null) events = Model.SINGLETON.getEvents();
                while(people == null) people = Model.SINGLETON.getPeople();
                for(String key : events.keySet()) {
                    mMap.addMarker(new MarkerOptions().position(events.get(key).getLatLng()).title("Event in "+events.get(key).getLocation()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                }
            }
        });
        return view;
    }


}
