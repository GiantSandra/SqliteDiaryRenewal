package freshman.sqlitediary2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingEvaluationDisplay extends AppCompatActivity {

    Cursor res;
    int row_id;
    String sento_name = null;
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;

    float old_bath_rating_num, old_changingroom_rating_num, old_restroom_rating_num, old_temperature_rating_num, old_total_rating_num;;
    String old_memo;

    boolean change_bath_rating = false;
    boolean change_changingroom_rating = false;
    boolean change_restroom_rating = false;
    boolean change_temperature_rating = false;
    boolean change_total_rating = false;
    boolean change_memo = false;

    float bath_rating_num, changingroom_rating_num, restroom_rating_num, temperature_rating_num, total_rating_num;

    boolean isChangedBathRatingNum = false;
    boolean isChangedChangingroomRatingNum = false;
    boolean isChangedRestroomRatingNum = false;
    boolean isChangedTemperatureRatingNum = false;
    boolean isChangedTotalRatingNum = false;

    int x2_bath_rating_num, x2_changingroom_rating_num, x2_restroom_rating_num, x2_temperature_rating_num, x2_total_rating_num;
    String new_memo = null;

    RatingBar bath_rating_bar;
    RatingBar changingroom_rating_bar;
    RatingBar restroom_rating_bar;
    RatingBar temperature_rating_bar;
    RatingBar total_rating_bar;
    EditText memo_edittext_view;
    SwitchCompat open_air_switch_compat;
    SwitchCompat visited_switch_compat;
    SwitchCompat favorite_switch_compat;

    boolean open_air_isTouched = false;
    boolean visited_isTouched = false;
    boolean favorite_isTouched = false;

    boolean open_air_isChanged = false;
    boolean visited_isChanged = false;
    boolean favorite_isChanged = false;

    int old_open_air_value, old_visited_value, old_favorite_value;
    int new_open_air_value, new_visited_value, new_favorite_value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_evaluation_display);


        Intent it = getIntent();
        row_id = it.getIntExtra("row_id", 0);
        sento_name = it.getStringExtra("sento_name");
        Log.i("ROW_ID", Integer.toString(row_id));


        final Toolbar toolbar = (Toolbar)findViewById(R.id.setting_evaluation_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("# " + sento_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                Log.i("BBBBBBBBBBB",Float.toString(bath_rating_num));
                if(id == R.id.action_save){
                    new AlertDialog.Builder(SettingEvaluationDisplay.this)
                            .setTitle("確認")
                            .setMessage("本当に保存しますか？")
                            .setPositiveButton("はい", new SaverDialogClickListener())
                            .setNegativeButton("いいえ", null).show();
                }
                return true;
            }
        });

        BackAsyncTask task = new BackAsyncTask();
        task.execute();

    }

    class BackAsyncTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            SettingEvaluationDisplay.this.getCursorFromDatabase();
            SettingEvaluationDisplay.this.getOldValues();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            SettingEvaluationDisplay.this.findViewsFromId();
            SettingEvaluationDisplay.this.setViews();
        }
    }

    public void getCursorFromDatabase()
    {
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        res = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        res.moveToPosition(row_id);
    }

    public void getOldValues()
    {
        //Get old values from detail
        old_bath_rating_num = getFloat(res.getInt(5));
        old_changingroom_rating_num = getFloat(res.getInt(6));
        old_restroom_rating_num = getFloat(res.getInt(7));
        old_temperature_rating_num = getFloat(res.getInt(8));
        old_total_rating_num = getFloat(res.getInt(9));
        old_memo = res.getString(15);
        old_open_air_value = res.getInt(res.getColumnIndex("OPEN_AIR"));
        old_visited_value = res.getInt(res.getColumnIndex("VISITED"));
        old_favorite_value = res.getInt(res.getColumnIndex("FAVORITE"));
    }


    public void findViewsFromId()
    {
        bath_rating_bar = (RatingBar)findViewById(R.id.bath_rating_bar);
        changingroom_rating_bar = (RatingBar)findViewById(R.id.changingroom_rating_bar);
        restroom_rating_bar = (RatingBar)findViewById(R.id.restroom_rating_bar);
        temperature_rating_bar = (RatingBar)findViewById(R.id.temperature_rating_bar);
        total_rating_bar = (RatingBar)findViewById(R.id.total_rating_bar);
        memo_edittext_view = (EditText)findViewById(R.id.memo_edittext_view);
        open_air_switch_compat = (SwitchCompat)findViewById(R.id.open_air_switch_compat);
        visited_switch_compat = (SwitchCompat)findViewById(R.id.visited_switch_compat);
        favorite_switch_compat = (SwitchCompat)findViewById(R.id.favorite_switch_compat);
    }

    public void setViews()
    {
        bath_rating_bar.setRating(old_bath_rating_num);
        bath_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int id = ratingBar.getId();
                if(id == R.id.bath_rating_bar){
                    bath_rating_num = ratingBar.getRating();
                    x2_bath_rating_num = (int)(bath_rating_num*2);
                    isChangedBathRatingNum = true;
                }
            }

        });


        changingroom_rating_bar.setRating(old_changingroom_rating_num);
        changingroom_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int id = ratingBar.getId();
                if(id == R.id.changingroom_rating_bar){
                    changingroom_rating_num = rating;
                    x2_changingroom_rating_num = (int)(changingroom_rating_num*2);
                    isChangedChangingroomRatingNum = true;
                }
            }
        });



        restroom_rating_bar.setRating(old_restroom_rating_num);
        restroom_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int id = ratingBar.getId();
                if(id == R.id.restroom_rating_bar){
                    restroom_rating_num = rating;
                    x2_restroom_rating_num = (int)(restroom_rating_num*2);
                    isChangedRestroomRatingNum = true;
                }
            }
        });



        temperature_rating_bar.setRating(old_temperature_rating_num);
        temperature_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int id = ratingBar.getId();
                if(id == R.id.temperature_rating_bar){
                    temperature_rating_num = rating;
                    x2_temperature_rating_num = (int)(temperature_rating_num*2);
                    isChangedTemperatureRatingNum = true;
                }
            }
        });



        total_rating_bar.setRating(old_total_rating_num);
        total_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int id = ratingBar.getId();
                if(id == R.id.total_rating_bar){
                    total_rating_num = rating;
                    x2_total_rating_num = (int)(total_rating_num*2);
                    isChangedTotalRatingNum = true;
                }
            }
        });

        memo_edittext_view.setText(old_memo, TextView.BufferType.NORMAL);






        if(old_open_air_value == 0){
            open_air_switch_compat.setChecked(false);
        }else if(old_open_air_value == 1){
            open_air_switch_compat.setChecked(true);
        }
        open_air_switch_compat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    new_open_air_value = 1;
                }else{
                    new_open_air_value = 0;
                }

                open_air_isChanged = true;

            }
        });




        if(old_visited_value == 0){
            visited_switch_compat.setChecked(false);
        }else if(old_visited_value == 1){
            visited_switch_compat.setChecked(true);
        }
        visited_switch_compat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    new_visited_value = 1;
                }else{
                    new_visited_value = 0;
                }

                visited_isChanged = true;

            }
        });



        if(old_favorite_value == 0){
            favorite_switch_compat.setChecked(false);
        }else if(old_favorite_value == 1){
            favorite_switch_compat.setChecked(true);
        }
        favorite_switch_compat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    new_favorite_value = 1;
                }else{
                    new_favorite_value = 0;
                }

                favorite_isChanged = true;

            }
        });
    }


    class SaverDialogClickListener implements DialogInterface.OnClickListener
    {


        @Override
        public void onClick(DialogInterface dialog, int which) {

            Log.i(Float.toString(SettingEvaluationDisplay.this.old_bath_rating_num), Float.toString(SettingEvaluationDisplay.this.bath_rating_num));


            if(SettingEvaluationDisplay.this.isChangedBathRatingNum == false){
                SettingEvaluationDisplay.this.bath_rating_num = SettingEvaluationDisplay.this.bath_rating_bar.getRating();
            }
            if(SettingEvaluationDisplay.this.isChangedChangingroomRatingNum == false){
                SettingEvaluationDisplay.this.changingroom_rating_num = SettingEvaluationDisplay.this.changingroom_rating_bar.getRating();
            }
            if(SettingEvaluationDisplay.this.isChangedRestroomRatingNum == false){
                SettingEvaluationDisplay.this.restroom_rating_num = SettingEvaluationDisplay.this.restroom_rating_bar.getRating();
            }
            if(SettingEvaluationDisplay.this.isChangedTemperatureRatingNum == false){
                SettingEvaluationDisplay.this.temperature_rating_num = SettingEvaluationDisplay.this.temperature_rating_bar.getRating();
            }
            if(SettingEvaluationDisplay.this.isChangedTotalRatingNum == false){
                SettingEvaluationDisplay.this.total_rating_num = SettingEvaluationDisplay.this.total_rating_bar.getRating();
            }
            if(SettingEvaluationDisplay.this.change_memo == false){
                SettingEvaluationDisplay.this.change_memo = true;
            }




            if(SettingEvaluationDisplay.this.old_bath_rating_num != SettingEvaluationDisplay.this.bath_rating_num){
                SettingEvaluationDisplay.this.change_bath_rating = true;
            }
            if(SettingEvaluationDisplay.this.old_changingroom_rating_num != SettingEvaluationDisplay.this.changingroom_rating_num){
                SettingEvaluationDisplay.this.change_changingroom_rating = true;
            }
            if(SettingEvaluationDisplay.this.old_restroom_rating_num != SettingEvaluationDisplay.this.restroom_rating_num){
                SettingEvaluationDisplay.this.change_restroom_rating = true;
            }
            if(SettingEvaluationDisplay.this.old_temperature_rating_num != SettingEvaluationDisplay.this.temperature_rating_num){
                SettingEvaluationDisplay.this.change_temperature_rating = true;
            }
            if(SettingEvaluationDisplay.this.old_total_rating_num != SettingEvaluationDisplay.this.total_rating_num){
                SettingEvaluationDisplay.this.change_total_rating = true;
            }
            if(SettingEvaluationDisplay.this.old_memo != SettingEvaluationDisplay.this.new_memo){
                Log.i("HHHHHHH", "MEMO");
                SettingEvaluationDisplay.this.change_memo = true;
            }


            String TABLE_NAME = "sento_table";
            int ID = SettingEvaluationDisplay.this.res.getInt(0);


            SettingEvaluationDisplay.this.database.beginTransaction();

            try {

                if (SettingEvaluationDisplay.this.change_bath_rating == true) {
                    Log.i("FFFFFFFFFFFFF", Integer.toString(row_id));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FEEL", SettingEvaluationDisplay.this.x2_bath_rating_num);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.change_bath_rating = false;
                }
                if (SettingEvaluationDisplay.this.change_changingroom_rating == true) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("BREADTH", SettingEvaluationDisplay.this.x2_changingroom_rating_num);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.change_changingroom_rating = false;
                }
                if (SettingEvaluationDisplay.this.change_restroom_rating == true) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("CALM", SettingEvaluationDisplay.this.x2_restroom_rating_num);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.change_restroom_rating = false;
                }
                if (SettingEvaluationDisplay.this.change_temperature_rating == true) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("TEMPERATURE", SettingEvaluationDisplay.this.x2_temperature_rating_num);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.change_temperature_rating = false;
                }
                if (SettingEvaluationDisplay.this.change_total_rating == true) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("TOTAL", SettingEvaluationDisplay.this.x2_total_rating_num);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.change_total_rating = false;
                }
                if (SettingEvaluationDisplay.this.change_memo == true) {
                    Log.i("RRRRR","MEMO");
                    SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) SettingEvaluationDisplay.this.memo_edittext_view.getText();
                    SettingEvaluationDisplay.this.new_memo = spannableStringBuilder.toString();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("MEMO", SettingEvaluationDisplay.this.new_memo);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.change_memo = false;
                }
                if(SettingEvaluationDisplay.this.open_air_isChanged == true){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("OPEN_AIR", SettingEvaluationDisplay.this.new_open_air_value);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.open_air_isChanged = false;
                }
                if(SettingEvaluationDisplay.this.visited_isChanged == true){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("VISITED", SettingEvaluationDisplay.this.new_visited_value);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.visited_isChanged = false;
                }
                if(SettingEvaluationDisplay.this.favorite_isChanged == true){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("FAVORITE", SettingEvaluationDisplay.this.new_favorite_value);
                    SettingEvaluationDisplay.this.database.update(TABLE_NAME, contentValues, "id = " + ID, null);
                    SettingEvaluationDisplay.this.favorite_isChanged = false;
                }

                SettingEvaluationDisplay.this.database.setTransactionSuccessful();

            }catch (Exception e){
                e.printStackTrace();
            }finally {
                SettingEvaluationDisplay.this.database.endTransaction();
            }
            Toast.makeText(SettingEvaluationDisplay.this, "データが更新されました", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.setting_evaluation_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        boolean result = true;

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                result = super.onOptionsItemSelected(item);
        }

        return result;
    }

    public float getFloat(int data)
    {
        float old_rating_num;
        try{
            old_rating_num = (float)data/2;
        }catch(Exception e){
            old_rating_num = (float)0.0;
        }
        return old_rating_num;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
