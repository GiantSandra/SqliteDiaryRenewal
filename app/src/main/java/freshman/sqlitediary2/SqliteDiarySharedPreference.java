package freshman.sqlitediary2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by gin on 2017/11/08.
 */

public class SqliteDiarySharedPreference {

    private SharedPreferences sharedPref;
    private Context context;

    // Keys of SharedPreference
    private final static String DONE_CONVERT_ADDRESS = "doneConvertAddress";

    public SqliteDiarySharedPreference(String fileName, Context context){
        this.context = context;
        sharedPref = this.context.getSharedPreferences(fileName,Context.MODE_PRIVATE);
    }

    public void doneConvertAddress(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(DONE_CONVERT_ADDRESS, false);
        editor.apply();
        Log.i("doneConvertAddress", "true");
    }

    public void setAddressConverted(boolean bool){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(DONE_CONVERT_ADDRESS, bool);
        editor.apply();
    }

    public boolean checkAddressConverted() {
        return sharedPref.getBoolean(DONE_CONVERT_ADDRESS, false);
    }

}
