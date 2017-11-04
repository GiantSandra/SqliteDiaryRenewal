package freshman.sqlitediary2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SqliteDiary2 extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    ListView listView;
    Cursor res;
    SearchView searchView;
    ListAdapter adapter;

    Handler handler;

    String[] option_menu_str = new String[]{"お気に入り", "露天風呂",
                                            "京都駅エリア", "三条・四条エリア", "祇園・東山エリア",
                                            "御所周辺エリア", "岡崎・銀閣寺エリア", "金閣寺・西陣エリア",
                                            "壬生エリア", "嵯峨・嵐山エリア", "洛北エリア", "伏見・山科エリア",
                                            "風呂★4.0以上", "脱衣所★4.0以上", "休憩所★4.0以上", "温度★4.0以上", "総合★4.0以上",
                                            "24:00閉店", "25:00閉店", "26:00閉店", "27:00閉店",
                                            "銭湯一覧"};

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_diary2);

        final Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("銭湯一覧");

        setDatabase();
        setListViewInBackground();
        setProgressDialog();
        setItemClickListener();
        setHandler();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }

    class BackAsyncTask extends AsyncTask<Void, Void, Void>
    {

        ListView listView;
        Cursor res;
        ListAdapter adapter;
        Context context;

        public BackAsyncTask(ListView listView, Cursor res, ListAdapter adapter, Context context)
        {
            this.listView = listView;
            this.res = res;
            this.adapter = adapter;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            adapter = getCustomCursorAdapter();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.setAdapter(adapter);
        }
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
                    database.close();
                    setDatabase();
                    adapter = getCustomCursorAdapter();
                    listView.setAdapter(adapter);
                    progressDialog.dismiss();
                }
            });
        }
    }

    class DiaryItemClickListener implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = (ListView)parent;
            Cursor cursor = (Cursor)listView.getItemAtPosition(position);
            int row_id = cursor.getInt(0) - 1;
            String sento_name = cursor.getString(1);
            String title_image_path = null;
            try{
                title_image_path = cursor.getString(cursor.getColumnIndex("PIC0"));
            }catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(getApplicationContext(), DiaryDetail.class);
            intent.putExtra("row_id", row_id);
            intent.putExtra("sento_name", sento_name);
            intent.putExtra("title_image_path", title_image_path);
            startActivity(intent);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.toolbar_search);
        setSearchView(menuItem);

        MenuItem reloadItem = menu.findItem(R.id.action_reload);
        setReloadButton(reloadItem);

        for(int i = 0; i<option_menu_str.length; i++){
            menu.add(Menu.NONE, i, i, option_menu_str[i]);
        }

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case 0:
                adapter = getFilteredCursorAdapterWithYesOrNo("FAVORITE", true);
                listView.setAdapter(adapter);
                break;

            case 1:
                adapter = getFilteredCursorAdapterWithYesOrNo("OPEN_AIR", true);
                listView.setAdapter(adapter);
                break;

            case 2:
                adapter = getFilterdCursorAdapterWithArea("京都駅");
                listView.setAdapter(adapter);
                break;

            case 3:
                adapter = getFilterdCursorAdapterWithArea("三条・四条");
                listView.setAdapter(adapter);
                break;

            case 4:
                adapter = getFilterdCursorAdapterWithArea("祇園・東山");
                listView.setAdapter(adapter);
                break;

            case 5:
                adapter = getFilterdCursorAdapterWithArea("御所周辺");
                listView.setAdapter(adapter);
                break;

            case 6:
                adapter = getFilterdCursorAdapterWithArea("岡崎・銀閣寺");
                listView.setAdapter(adapter);
                break;

            case 7:
                adapter = getFilterdCursorAdapterWithArea("金閣寺・西陣");
                listView.setAdapter(adapter);
                break;

            case 8:
                adapter = getFilterdCursorAdapterWithArea("壬生");
                listView.setAdapter(adapter);
                break;

            case 9:
                adapter = getFilterdCursorAdapterWithArea("嵯峨・嵐山");
                listView.setAdapter(adapter);
                break;

            case 10:
                adapter = getFilterdCursorAdapterWithArea("洛北");
                listView.setAdapter(adapter);
                break;

            case 11:
                adapter = getFilterdCursorAdapterWithArea("伏見・山科");
                listView.setAdapter(adapter);
                break;

            case 12:
                adapter = getFilteredCursorAdapterWithEvaluationMoreThan(8, "FEEL");
                listView.setAdapter(adapter);
                break;

            case 13:
                adapter = getFilteredCursorAdapterWithEvaluationMoreThan(8, "BREADTH");
                listView.setAdapter(adapter);
                break;

            case 14:
                adapter = getFilteredCursorAdapterWithEvaluationMoreThan(8, "CALM");
                listView.setAdapter(adapter);
                break;

            case 15:
                adapter = getFilteredCursorAdapterWithEvaluationMoreThan(8, "TEMPERATURE");
                listView.setAdapter(adapter);
                break;

            case 16:
                adapter = getFilteredCursorAdapterWithEvaluationMoreThan(8, "TOTAL");
                listView.setAdapter(adapter);
                break;

            case 17:
                adapter = getFilteredCursorAdapterWithClosedTime(24);
                listView.setAdapter(adapter);
                break;

            case 18:
                adapter = getFilteredCursorAdapterWithClosedTime(25);
                listView.setAdapter(adapter);
                break;

            case 19:
                adapter = getFilteredCursorAdapterWithClosedTime(26);
                listView.setAdapter(adapter);
                break;

            case 20:
                adapter = getFilteredCursorAdapterWithClosedTime(27);
                listView.setAdapter(adapter);
                break;

            case 21:
                adapter = getCustomCursorAdapter();
                listView.setAdapter(adapter);
                break;
        }

        return true;

    }


    public void setDatabase()
    {
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();
    }

    public void setListViewInBackground()
    {
        listView = (ListView) findViewById(R.id.main_list_view);
        BackAsyncTask task = new BackAsyncTask(listView, res, adapter, this);
        task.execute();
    }

    public void setProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading...");
    }

    public void setItemClickListener()
    {
        listView.setOnItemClickListener(new DiaryItemClickListener());
    }

    public void setHandler()
    {
        handler = new Handler();
    }

    public void setSearchView(MenuItem menuItem)
    {
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                res = database.query(true, "sento_table", new String[]{"rowid _id",
                                "NAME", "DETAIL", "PIC0", "VISITED"}, "NAME" + " LIKE ?",
                        new String[]{"%"+newText+"%"}, null, null, null, null);
                adapter = new CustomCursorAdapter(SqliteDiary2.this, R.layout.diary_main_list_item,
                        res,
                        new String[]{"NAME", "DETAIL"},
                        new int[]{R.id.item_sento_name, R.id.item_sento_detail},
                        0);
                listView.setAdapter(adapter);
                return false;
            }
        });
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

    public ListAdapter getFilteredCursorAdapterWithClosedTime(int closedTime)
    {
        res = database.query(true, "sento_table", new String[]{"rowid _id",
                        "NAME", "DETAIL", "PIC0", "VISITED"}, "DETAIL" + " LIKE ?",
                new String[]{"%"+"～" + closedTime +"%"}, null, null, null, null);
        adapter = new CustomCursorAdapter(SqliteDiary2.this, R.layout.diary_main_list_item,
                res,
                new String[]{"NAME", "DETAIL"},
                new int[]{R.id.item_sento_name, R.id.item_sento_detail},
                0);
        return adapter;
    }

    public ListAdapter getFilteredCursorAdapterWithEvaluationMoreThan(int evaluation_star_num, String columnName)
    {
        res = database.query(true, "sento_table", new String[]{"rowid _id",
                        "NAME", "DETAIL", "PIC0", "VISITED"}, columnName + " >= ?",
                new String[]{Integer.toString(evaluation_star_num)}, null, null, null, null);
        adapter = new CustomCursorAdapter(SqliteDiary2.this, R.layout.diary_main_list_item,
                res,
                new String[]{"NAME", "DETAIL"},
                new int[]{R.id.item_sento_name, R.id.item_sento_detail},
                0);
        return adapter;
    }

    public ListAdapter getFilterdCursorAdapterWithArea(String area)
    {
        res = database.query(true, "sento_table", new String[]{"rowid _id",
                        "NAME", "DETAIL", "PIC0", "VISITED"}, "AREA" + " LIKE ?",
                new String[]{area}, null, null, null, null);
        adapter = new CustomCursorAdapter(SqliteDiary2.this, R.layout.diary_main_list_item,
                res,
                new String[]{"NAME", "DETAIL"},
                new int[]{R.id.item_sento_name, R.id.item_sento_detail},
                0);
        return adapter;
    }

    public ListAdapter getFilteredCursorAdapterWithYesOrNo(String columnName, boolean flag)
    {
        int column_data = 0;
        if(flag){
            column_data = 1;
        }
        res = database.query(true, "sento_table", new String[]{"rowid _id",
                        "NAME", "DETAIL", "PIC0", "VISITED"}, columnName + " LIKE ?",
                new String[]{Integer.toString(column_data)}, null, null, null, null);
        adapter = new CustomCursorAdapter(SqliteDiary2.this, R.layout.diary_main_list_item,
                res,
                new String[]{"NAME", "DETAIL"},
                new int[]{R.id.item_sento_name, R.id.item_sento_detail},
                0);
        return adapter;
    }

    public ListAdapter getCustomCursorAdapter()
    {

        res = database.query("sento_table", new String[]{"rowid _id", "NAME", "DETAIL", "PIC0", "VISITED"},
                null, null, null, null, null);
        adapter = new CustomCursorAdapter(SqliteDiary2.this,
                R.layout.diary_main_list_item,
                res,
                new String[]{"NAME", "DETAIL"},
                new int[]{R.id.item_sento_name, R.id.item_sento_detail}, 0);
        return adapter;
    }

}
