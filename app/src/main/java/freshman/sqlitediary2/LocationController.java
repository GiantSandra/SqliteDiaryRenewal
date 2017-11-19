package freshman.sqlitediary2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import freshman.sqlitediary2.select_by_map.MarkerInfo;

/**
 * Created by gin on 2017/11/08.
 */

public class LocationController {

    private final static String LATLNG_TABLE_NAME = "latlng_table";


    //Convert Address to LatLng
    public static LatLng convertAddressToLatlng(String address, Context context){

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


    public static boolean putLatlngToTable(List<MarkerInfo> list, Context context, boolean doneAddressConverted){
        SQLiteDatabase sqliteDatabase = getSentoDB(context);
        boolean success = false;
        if(!doneAddressConverted) {
            try {
                Log.i("XX Create Table XX", "putlatlngToTable");
                SQLiteStatement statement = sqliteDatabase.compileStatement("DROP TABLE IF EXISTS " + LATLNG_TABLE_NAME + ";");
                statement.execute();
                statement = sqliteDatabase.compileStatement(String.format("CREATE TABLE IF NOT EXISTS %s (" +
                                "sento_id INTEGER , " +
                                "latitude DOUBLE ," +
                                "longitude DOUBLE " + ");",
                        LATLNG_TABLE_NAME));
                statement.execute();
                sqliteDatabase.beginTransaction();
                for(int i=0; i<list.size();i++) {
                    MarkerInfo marker = list.get(i);
                    Log.i("XX latitude XX", String.valueOf(marker.getLatLng().latitude));
                    ContentValues values = new ContentValues();
                    values.put("sento_id",marker.getId());
                    values.put("latitude",marker.getLatLng().latitude);
                    values.put("longitude",marker.getLatLng().longitude);
                    sqliteDatabase.insert(LATLNG_TABLE_NAME,null,values);
                }
                sqliteDatabase.setTransactionSuccessful();
                sqliteDatabase.endTransaction();
                Log.e("latlng_table", "success");
                success = true;
            } catch (SQLiteException err) {
                Log.e("latlng_table", "fail create");
                success = false;
            } finally {
                sqliteDatabase.close();
            }
        }
        return success;
    }

    private static SQLiteDatabase getSentoDB(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        return databaseHelper.getWritableDatabase();
    }

    class LatlngWithRowID{

        private int rowId;
        private double latitude;
        private double longitude;

        public LatlngWithRowID(int rowId, double latitude, double longitude){
            this.rowId = rowId;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public int getRowId() {
            return rowId;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

}
