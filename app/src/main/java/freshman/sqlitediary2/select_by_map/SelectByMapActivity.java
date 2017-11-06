package freshman.sqlitediary2.select_by_map;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freshman.sqlitediary2.DatabaseHelper;
import freshman.sqlitediary2.DiaryDetail;
import freshman.sqlitediary2.R;
import freshman.sqlitediary2.SqliteDiary2;

public class SelectByMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap googleMap;

    List<MarkerInfo> listMarkerInfo;
    Map<String,MarkerInfo> mapMarkerIdAndCursorId;

    DatabaseHelper databaseHelper;
    SQLiteDatabase sqliteDatabase;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_by_map);

        listMarkerInfo = new ArrayList<MarkerInfo>();
        mapMarkerIdAndCursorId = new HashMap<String,MarkerInfo>();

        databaseHelper = new DatabaseHelper(this);
        sqliteDatabase = databaseHelper.getWritableDatabase();
        BackAsyncTask task = new BackAsyncTask(cursor, this);
        task.execute();


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
            cursor = sqliteDatabase.query("sento_table", new String[]{"rowid _id", "NAME", "ADDRESS", "DETAIL", "PIC0", "VISITED"},
                    null, null, null, null, null);
            int count = 0;
            while(cursor.moveToNext()){
                MarkerInfo markerInfo = new MarkerInfo(cursor.getInt(0)-1,
                                                       cursor.getString(1),
                                                       cursor.getString(2),
                                                       cursor.getString(3),
                                                       SelectByMapActivity.this);
                listMarkerInfo.add(markerInfo);
                count++;
                if(count == 10){
                    return null;
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
            initMap();
        }
    }


    private void initMap() {
        mapFragment = MapFragment.newInstance();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(android.R.id.content,mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        zoomIn(34.9838247,135.7601094,11);


        for(int i=0 ; i<listMarkerInfo.size(); i++){
            MarkerInfo markerInfo = listMarkerInfo.get(i);
            Log.i("ADDRESS",markerInfo.getAddress());
            MarkerOptions option4 = new MarkerOptions()
                                .title(markerInfo.getName())
                                .position(markerInfo.getLatLng())
                                .snippet(markerInfo.getAddress());
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
                View v = getLayoutInflater().inflate(R.layout.marker_info,null);
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

        this.googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i("XX MARKER ID XX",marker.getId());
                return false;
            }
        });
    }

    private void zoomIn(double latitude, double longitude, float zoom){
        LatLng latLng = new LatLng(latitude,longitude);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        this.googleMap.moveCamera(update);
    }

    private void goToDetailActivity(int row_id, String sentoName, String titleImageFilePath){
        Intent intent = new Intent(getApplicationContext(), DiaryDetail.class);
        intent.putExtra("row_id", row_id);
        intent.putExtra("sento_name", sentoName);
        intent.putExtra("title_image_path", titleImageFilePath);
        startActivity(intent);
    }
}
