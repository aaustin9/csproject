package toromu.familymap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import toromu.familymap.models.Event;
import toromu.familymap.models.Model;
import toromu.familymap.models.Person;

/**
 * Search Activity
 * Created by Austin on 05-Dec-16.
 */
public class SearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<SearchResult> results = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_search);
        editText = (EditText) findViewById(R.id.searchBar);
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        searchAdapter = new SearchAdapter(results);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(searchAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                results.clear();
                String term = s.toString().toLowerCase();
                if(term.equals("")) {
                    searchAdapter.notifyDataSetChanged();
                    return;
                }
                for(Person person : Model.SINGLETON.getPersonSet()) {
                    if(person.getName().toLowerCase().contains(term)) {
                        results.add(new SearchResult(person,null,person.getName(),"",(person.isFemale()?R.drawable.female:R.drawable.male)));
                    }
                }
                for(Event event : Model.SINGLETON.getEventSet()) {
                    if(event.getEventDetails().toLowerCase().contains(term)) {
                        results.add(new SearchResult(null,event,event.getEventDetails(),Model.SINGLETON.getPeople().get(event.getPersonId()).getName(),R.drawable.location));
                    }
                }
                searchAdapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
