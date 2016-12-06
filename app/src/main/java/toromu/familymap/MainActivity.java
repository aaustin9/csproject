package toromu.familymap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity { // implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean authenticated;

    public MainActivity() { authenticated = false; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FragmentManager fm = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = new LoginFragment();
        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
    }

    public void authenticate() {
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = new MapFragment();

        this.authenticated = true;
        fm.beginTransaction()
                .replace(R.id.fragment_container, mapFragment)
                .commit();
    }
}