package toromu.familymap;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MapsActivity extends AppCompatActivity {
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_main);
        mapFragment = new MapFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, mapFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.go_to_top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openPersonActivity(View view) {
        mapFragment.openPersonActivity();
    }
}
