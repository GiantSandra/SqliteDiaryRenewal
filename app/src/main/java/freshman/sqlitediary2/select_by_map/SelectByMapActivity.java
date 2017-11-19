package freshman.sqlitediary2.select_by_map;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freshman.sqlitediary2.DatabaseHelper;
import freshman.sqlitediary2.LocationController;
import freshman.sqlitediary2.PageTransitionController;
import freshman.sqlitediary2.R;
import freshman.sqlitediary2.SqliteDiarySharedPreference;

public class SelectByMapActivity extends AppCompatActivity {

    // MapFragment mapFragment;
    GoogleMap googleMap;
    View mapLayout;
    SupportMapFragment mapFragment;

    List<MarkerInfo> listMarkerInfo;
    Map<String,MarkerInfo> mapMarkerIdAndCursorId;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqliteDatabase;
    Cursor sento_cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_by_map);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.map_container, new CustomMapFragment()).commit();
        }

    }





/*
    private void initMap() {

        mapFragment = MapFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content,mapFragment);
        fragmentTransaction.commit();


        //mapFragment.getMapAsync(this);
    }
*/


}
