package toromu.familymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeMap;

import toromu.familymap.models.Event;
import toromu.familymap.models.Model;
import toromu.familymap.models.Person;

/**
 * Person Activity
 * Created by Austin on 05-Dec-16.
 */
public class PersonActivity extends AppCompatActivity {
    TextView firstName;
    TextView lastName;
    TextView gender;
    TreeMap<Integer, Event> yearEvents;
    TreeMap<String, Event> yearlessEvents;
    ArrayList<Event> events;
    Event numberedEvent;

    Person person;
    private static final String MALE = "Male";
    private static final String FEMALE = "Female";
    ArrayList<Integer> eventIds = new ArrayList<>(Arrays.asList(R.id.event1, R.id.event2, R.id.event3, R.id.event4, R.id.event5, R.id.event6, R.id.event7, R.id.event8, R.id.event9, R.id.event10));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_person);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View view = inflater.inflate(R.layout.activity_person, null);
        person = Model.SINGLETON.getCurrentPerson();
        firstName = (TextView) findViewById(R.id.firstNameOutput);
        lastName = (TextView) findViewById(R.id.lastNameOutput);
        gender = (TextView) findViewById(R.id.genderOutput);
        orderEvents();
        setTextFields();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        setTextFields();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.go_to_top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        setTextFields();
        super.onResume();
    }

    private void orderEvents() {
        yearEvents = new TreeMap<>();
        yearlessEvents = new TreeMap<>();
        events = new ArrayList<>();
        Event death = null;
        HashSet<Event> personEvents = person.getEvents();
        for(Event event : personEvents) {
            if(event.getDescription().toLowerCase().equals("birth")) events.add(event);
            else if(event.getDescription().toLowerCase().equals("death")) death = event;
            else {
                if(event.getYear() >= 0) yearEvents.put(event.getYear(), event);
                else yearlessEvents.put(event.getDescription(), event);
            }
        }
        for(int i : yearEvents.keySet()) events.add(yearEvents.get(i));
        for(String s : yearlessEvents.keySet()) events.add(yearlessEvents.get(s));
        if(death != null) events.add(death);
    }

    private void setCurrent() {
        Model.SINGLETON.setCurrentEvent(numberedEvent);
    }

    private void setTextFields() {
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        if(person.isFemale()) gender.setText(FEMALE);
        else gender.setText(MALE);
        Event event;
        for(int i=0; i<events.size(); i++) {
            event = events.get(i);
            numberedEvent = event;
            TextView textView = (TextView) findViewById(eventIds.get(i));
            textView.setText(event.getEventDetails());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrent();
                    openMapActivity();
                }
            });
        }
        if(person.getFather() != null) {
            TextView fatherView = (TextView) findViewById(R.id.fatherOutput);
            fatherView.setText(person.getFather().getName());
            fatherView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.SINGLETON.setCurrentPerson(person.getFather());
                    openPersonActivity();
                }
            });
        }
        if(person.getMother() != null) {
            TextView motherView = (TextView) findViewById(R.id.motherOutput);
            motherView.setText(person.getMother().getName());
            motherView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.SINGLETON.setCurrentPerson(person.getMother());
                    openPersonActivity();
                }
            });
        }
        if(person.getSpouse() != null) {
            TextView spouseView = (TextView) findViewById(R.id.spouseOutput);
            spouseView.setText(person.getSpouse().getName());
            spouseView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.SINGLETON.setCurrentPerson(person.getSpouse());
                    openPersonActivity();
                }
            });
        }
    }

    private void openMapActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void openPersonActivity() {
        Intent intent = new Intent(this, PersonActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.go_to_top:
                Model.SINGLETON.reinitialize();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
