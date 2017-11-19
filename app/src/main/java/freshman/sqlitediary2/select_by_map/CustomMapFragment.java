package freshman.sqlitediary2.select_by_map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

/**
 * Created by gin on 2017/11/19.
 */

public class CustomMapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap googleMap;
    View mapLayout;
    SupportMapFragment mapFragment;

    List<MarkerInfo> listMarkerInfo;
    Map<String,MarkerInfo> mapMarkerIdAndCursorId;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqliteDatabase;
    Cursor sento_cursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listMarkerInfo = new ArrayList<MarkerInfo>();
        mapMarkerIdAndCursorId = new HashMap<String,MarkerInfo>();

        databaseHelper = new DatabaseHelper(getActivity());
        sqliteDatabase = databaseHelper.getWritableDatabase();
        CustomMapFragment.BackAsyncTask task = new CustomMapFragment.BackAsyncTask(sento_cursor, getActivity().getApplicationContext());
        task.execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_custom_map, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        FloatingActionButton floatButton = (FloatingActionButton)view.findViewById(R.id.gps_switch);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            }
        });
        return view;
    }

    class BackAsyncTask extends AsyncTask<Void, Void, Void>
    {
        Cursor res;
        Context context;

        public BackAsyncTask(Cursor res, Context context)
        {
            this.res = res;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

        }
    //ここでデータを取得する
    @Override
    protected Void doInBackground(Void... params) {
        sento_cursor = sqliteDatabase.query("sento_table", new String[]{"rowid _id","NAME", "ADDRESS", "DETAIL", "PIC0", "VISITED"},
                null, null, null, null, null);
        SqliteDiarySharedPreference sharedPreference = new SqliteDiarySharedPreference("sentoPref",this.context);
        if(!sharedPreference.checkAddressConverted()) {
            while (sento_cursor.moveToNext()) {
                boolean isVisited = false;
                int intIsVisited = sento_cursor.getInt(sento_cursor.getColumnIndex("VISITED"));
                if (intIsVisited == 1) {
                    isVisited = true;
                }
                MarkerInfo markerInfo = new MarkerInfo(sento_cursor.getInt(0) - 1,
                        sento_cursor.getString(1),
                        sento_cursor.getString(2),
                        sento_cursor.getString(3),
                        isVisited,
                        this.context, sharedPreference.checkAddressConverted());
                listMarkerInfo.add(markerInfo);
            }
            boolean addressConverted = LocationController.putLatlngToTable(listMarkerInfo,this.context,sharedPreference.checkAddressConverted());
            sharedPreference.setAddressConverted(addressConverted);
        } else {
            while (sento_cursor.moveToNext()) {
                boolean isVisited = false;
                int intIsVisited = sento_cursor.getInt(sento_cursor.getColumnIndex("VISITED"));
                if (intIsVisited == 1) {
                    isVisited = true;
                }
                Log.i("SentooooooID",String.valueOf(sento_cursor.getInt(0)-1));
                Cursor latlng_cursor = sqliteDatabase.rawQuery("select sento_id, latitude, longitude from latlng_table where sento_id = ?", new String[]{String.valueOf(sento_cursor.getInt(0)-1)});
                latlng_cursor.moveToFirst();
                MarkerInfo markerInfo = new MarkerInfo(sento_cursor.getInt(0) -1,
                        sento_cursor.getString(1),
                        sento_cursor.getString(2),
                        sento_cursor.getString(3),
                        latlng_cursor.getDouble(1),
                        isVisited,
                        latlng_cursor.getDouble(2),
                        this.context);
                listMarkerInfo.add(markerInfo);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    //ここでViewを操作する
    @Override
    protected void onPostExecute(Void aVoid) {

    }
}

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        // 京都駅を中心にズームイン
        zoomIn(34.9838247,135.7601094,11);


        for(int i=0 ; i<listMarkerInfo.size(); i++){
            MarkerInfo markerInfo = listMarkerInfo.get(i);
            Log.i("ADDRESS",markerInfo.getAddress());
            float color = markerInfo.isVisited() ? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED;
            MarkerOptions option4 = new MarkerOptions()
                    .title(markerInfo.getName())
                    .position(markerInfo.getLatLng())
                    .snippet(markerInfo.getAddress())
                    .icon(BitmapDescriptorFactory.defaultMarker(color));
            Marker marker = this.googleMap.addMarker(option4);
            mapMarkerIdAndCursorId.put(marker.getId(),markerInfo);
        }

        this.googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(final Marker marker) {
                View v = getActivity().getLayoutInflater().inflate(R.layout.marker_info,null);
                TextView name = (TextView)v.findViewById(R.id.sento_name);
                TextView address = (TextView)v.findViewById(R.id.sento_address);
                name.setText(marker.getTitle());
                address.setText(marker.getSnippet());
                return v;
            }
        });

        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                MarkerInfo info = mapMarkerIdAndCursorId.get(marker.getId());
                goToDetailActivity(info.getId(),info.getName(),info.getTitleImagePath());
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            this.googleMap.setMyLocationEnabled(true);
        } else {
            Toast.makeText(getActivity(), "位置情報へのアクセスが許可されていません", Toast.LENGTH_LONG).show();
        }

        /*
        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("XX MARKER ID XX",marker.getId());
                return false;
            }
        });
        */
    }

    private void zoomIn(double latitude, double longitude, float zoom){
        LatLng latLng = new LatLng(latitude,longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        this.googleMap.moveCamera(update);
    }

    private void goToDetailActivity(int row_id, String sentoName, String titleImageFilePath){
        PageTransitionController.goToDetailActivity(row_id, sentoName, titleImageFilePath, getActivity());
    }
}
