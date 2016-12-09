package toromu.familymap;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import toromu.familymap.models.Model;

/**
 * Filter Activity
 * Created by Austin on 05-Dec-16.
 */
public class FilterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<String> filterList = new ArrayList<>();
    private FiltersAdapter filtersAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_filter);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        filtersAdapter = new FiltersAdapter(filterList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(filtersAdapter);
        prepareFilterData();
    }

    public void prepareFilterData() {
        for(String eventType : Model.SINGLETON.getEventTypes()) filterList.add(eventType);
        filterList.add("father's side");
        filterList.add("mother's side");
        filterList.add("gender: male");
        filterList.add("gender: female");
        filtersAdapter.notifyDataSetChanged();
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
