package freshman.sqlitediary2.select_by_map;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    public MarkerInfo(int id, String name, String address, String titleImagePath, Context context){

        this.context = context;

        this.id = id;
        this.name = name;
        this.address = address;
        this.titleImagePath = titleImagePath;
        this.latLng = convertAddressToLatlng(this.address, context);

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

    //Convert Address to LatLng
    private LatLng convertAddressToLatlng(String address, Context context){

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        double latitude;
        double longitude;
        try{
            List<Address> addressList = geocoder.getFromLocationName(address, 1);
            if(!addressList.isEmpty()){
                Address address1 = addressList.get(0);
                latitude = address1.getLatitude();
                longitude = address1.getLongitude();
                Log.i("latitude", String.valueOf(latitude));
                Log.i("longitude", String.valueOf(longitude));
                return new LatLng(latitude,longitude);
            } else {
                return new LatLng(1, 0);
            }

        }catch(IOException e){
            e.printStackTrace();
        }

        return null;
    }


    public boolean isVisited() {
        return isVisited;
    }

    public String getTitleImagePath() {
        return titleImagePath;
    }
}
