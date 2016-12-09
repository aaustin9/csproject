package toromu.familymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import toromu.familymap.models.MapSettings;
import toromu.familymap.models.Model;

/**
 * Settings Activity
 * Created by Austin on 05-Dec-16.
 */
public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Switch switch1;
    Switch switch2;
    Switch switch3;
    HashMap<String, Integer> colors = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_settings);
        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
        View view = inflater.inflate(R.layout.activity_settings, null);
        colors.put("Red", 1);
        colors.put("Green", 2);
        colors.put("Blue", 3);
        spinner = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.modes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner1 = (Spinner) findViewById(R.id.spinner0);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        spinner1.setOnItemSelectedListener(this);
        spinner2 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(this);
        spinner3 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.colors_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);
        spinner3.setOnItemSelectedListener(this);
        spinner1.setSelection(colors.get(MapSettings.SINGLETON.getLifeStoryLinesColor()), false);
        spinner2.setSelection(colors.get(MapSettings.SINGLETON.getFamilyTreeLinesColor()), false);
        spinner3.setSelection(colors.get(MapSettings.SINGLETON.getSpouseLinesColor()), false);
        switch1 = (Switch) findViewById(R.id.switch1);
        switch2 = (Switch) findViewById(R.id.switch2);
        switch3 = (Switch) findViewById(R.id.switch3);
        switch1.setChecked(MapSettings.SINGLETON.isLifeStoryLines());
        switch2.setChecked(MapSettings.SINGLETON.isFamilyTreeLines());
        switch3.setChecked(MapSettings.SINGLETON.isSpouseLines());
//        switch1.setChecked(MapSettings.SINGLETON.is);
    }
    // spinner0
    // spinner
    // spinner2

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

    public void logout(View view) {
        Model.SINGLETON.reinitialize();
        Model.SINGLETON.setAuthorized(false);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void resyncLogin(View view) {
        Model.SINGLETON.reinitialize();
//        LoginFragment loginFragment = new LoginFragment();
//        ArrayList<String> credentials = Model.SINGLETON.getCredentials();
//        loginFragment.login(credentials.get(0), credentials.get(1), credentials.get(2));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        View viewParent = (View) view.getParent();
        String input = (String) parent.getItemAtPosition(position);
        if(input.equals("Normal")) MapSettings.SINGLETON.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        else if(input.equals("Hybrid")) MapSettings.SINGLETON.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        else if(input.equals("Satellite")) MapSettings.SINGLETON.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        else if(input.equals("Terrain")) MapSettings.SINGLETON.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        else {
            switch(parent.getId()) {
                case R.id.spinner0:
                    break;
                case R.id.spinner:
                    break;
                case R.id.spinner2:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MapSettings.SINGLETON.setLifeStoryLines(switch1.isChecked());
        MapSettings.SINGLETON.setFamilyTreeLines(switch2.isChecked());
        MapSettings.SINGLETON.setSpouseLines(switch3.isChecked());
        MapSettings.SINGLETON.setLifeStoryLinesColor((String) spinner1.getSelectedItem());
        MapSettings.SINGLETON.setFamilyTreeLinesColor((String) spinner2.getSelectedItem());
        MapSettings.SINGLETON.setSpouseLinesColor((String) spinner3.getSelectedItem());
//        MapSettings.SINGLETON.setFamilyTreeLinesColor((String) );
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
//        parent.setSelection();
    }
}
