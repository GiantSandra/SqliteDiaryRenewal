package freshman.sqlitediary2.select_by_map;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import freshman.sqlitediary2.LocationController;

/**
 * Created by gin on 2017/11/05.
 */

public class MarkerInfo {

    // Show these info in layout of markers
    private int id;
    private String name;
    private String address;
    private String titleImagePath = null;
    private LatLng latLng;
    private boolean isVisible = true;
    private boolean isVisited = false;


    private Context context;

    public MarkerInfo(int id, String name, String address, String titleImagePath, boolean isVisited, Context context, boolean doneAddressConverted){

        this.context = context;

        this.id = id;
        this.name = name;
        this.address = address;
        this.titleImagePath = titleImagePath;
        this.isVisited = isVisited;
        this.latLng = convertAddressToLatlng(this.address, context);
    }

    public MarkerInfo(int id, String name, String address, String titleImagePath,double latitude, boolean isVisited, double longitude, Context context){

        this.context = context;

        this.id = id;
        this.name = name;
        this.address = address;
        this.titleImagePath = titleImagePath;
        this.isVisited = isVisited;
        this.latLng = new LatLng(latitude, longitude);
    }

    private LatLng convertAddressToLatlng(String address, Context context) {
        return LocationController.convertAddressToLatlng(address,context);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LatLng getLatLng() {
        return latLng;
    }




    public boolean isVisited() {
        return isVisited;
    }

    public String getTitleImagePath() {
        return titleImagePath;
    }
}
