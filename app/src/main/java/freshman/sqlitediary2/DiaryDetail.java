package freshman.sqlitediary2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;

public class DiaryDetail extends AppCompatActivity {

    Cursor res;
    int row_id;
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    public static final String TABLE_NAME = "sento_table";

    TabLayout tabLayout;
    ViewPager viewPager;
    ListView listView;
    ImageView imageView;

    String sento_name = null;
    String title_image_path = null;
    Intent intent;
    FragmentEvaluation fragmentEvaluation = null;
    FragmentInfo fragmentInfo = null;
    FragmentAlbum fragmentAlbum = null;
    FragmentOverview fragmentOverview = null;

    FragmentManager fragmentManager;

    ProgressDialog progressDialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);


        intent = getIntent();
        row_id = intent.getIntExtra("row_id", 0);
        sento_name = intent.getStringExtra("sento_name");
        title_image_path = intent.getStringExtra("title_image_path");


        BackAsyncTask task = new BackAsyncTask();
        task.execute();


        final Toolbar toolbar = (Toolbar)findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        findViewById(android.R.id.content).setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        CollapsingToolbarLayout collapsingToolbarLayout
                = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(sento_name);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);


        ImageView title_image_view = (ImageView)findViewById(R.id.title_image);
        if(title_image_path != null){
            Uri uri = Uri.parse(title_image_path);
            try{
                InputStream is = getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                title_image_view.setImageBitmap(bmp);
            }catch(Exception e){
                Log.i("Error", "EXCEPTIONinDetail", e);
            }
        }

        fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(new CustomAdapter(fragmentManager, getApplicationContext(), row_id));
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.sample_checked_checkbox_2_512);
        tabLayout.getTabAt(1).setIcon(R.drawable.sample_album);
        tabLayout.getTabAt(2).setIcon(R.drawable.sample_overview);

        setProgressDialog();
        setHandler();


    }


    class BackAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            DiaryDetail.this.setCursorFromDatabase();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }


    public void setCursorFromDatabase()
    {
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
        res = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        res.moveToPosition(row_id);
    }


    class CustomAdapter extends FragmentPagerAdapter
    {

        int row_id;

        public static final int PAGE_NUM = 3;

        public CustomAdapter(FragmentManager supportFragmentManager, Context context, int row_id)
        {
            super(supportFragmentManager);
            this.row_id = row_id;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    if(fragmentEvaluation == null){
                        fragmentEvaluation = new FragmentEvaluation();
                        Bundle args = new Bundle();
                        args.putInt("row_id", row_id);
                        fragmentEvaluation.setArguments(args);
                    }
                    return fragmentEvaluation;

                case 1:
                    if(fragmentAlbum == null){
                        fragmentAlbum = new FragmentAlbum();
                        Bundle args = new Bundle();
                        args.putInt("row_id", row_id);
                        fragmentAlbum.setArguments(args);
                    }
                    return fragmentAlbum;
                default:
                    if(fragmentOverview == null){
                        fragmentOverview = new FragmentOverview();
                        Bundle args = new Bundle();
                        args.putInt("row_id", row_id);
                        fragmentOverview.setArguments(args);
                    }
                    return fragmentOverview;
            }

        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem edit_menu_item = menu.findItem(R.id.action_edit);
        edit_menu_item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent it = new Intent(getApplicationContext(), SettingEvaluationDisplay.class);
                String sento_name = res.getString(res.getColumnIndex("NAME"));
                it.putExtra("row_id", row_id);
                it.putExtra("sento_name", sento_name);
                startActivity(it);
                return false;
            }
        });

        MenuItem refresh_menu_item = menu.findItem(R.id.action_refresh_in_detail);
        setReloadButton(refresh_menu_item);
        return  true;
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

    public void setReloadButton(MenuItem reloadItem)
    {
        reloadItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                progressDialog.show();
                ReloadThread thread = new ReloadThread();
                thread.start();
                return false;
            }
        });
    }


    class ReloadThread extends Thread
    {
        public void run()
        {
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){

            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentEvaluation = new FragmentEvaluation();
                    Bundle args1 = new Bundle();
                    args1.putInt("row_id", row_id);
                    fragmentEvaluation.setArguments(args1);
                    fragmentTransaction.replace(R.id.container1, fragmentEvaluation);
                    fragmentTransaction.commit();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void setHandler()
    {
        handler = new Handler();
    }

    public void setProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
