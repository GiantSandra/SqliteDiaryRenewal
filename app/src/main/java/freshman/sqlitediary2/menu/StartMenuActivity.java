package freshman.sqlitediary2.menu;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import freshman.sqlitediary2.R;
import freshman.sqlitediary2.SqliteDiary2;
import freshman.sqlitediary2.favorite.FavoriteActivity;
import freshman.sqlitediary2.search_near.SearchNearActivity;
import freshman.sqlitediary2.select_by_map.SelectByMapActivity;
import freshman.sqlitediary2.status.StatusActivity;

public class StartMenuActivity extends AppCompatActivity {

    private GridView gridView;
    private MenuGridAdapter menuGridAdapter;

    private List<StartMenuCard> listItem;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);

        final Resources resources = getResources();
        listItem = new ArrayList<StartMenuCard>() {
            {
                add(new StartMenuCard(resources.getString(R.string.card_title_all_list)));
                add(new StartMenuCard(resources.getString(R.string.card_title_favorite_list)));
                add(new StartMenuCard(resources.getString(R.string.card_title_search_near)));
                add(new StartMenuCard(resources.getString(R.string.card_title_select_by_map)));
                add(new StartMenuCard(resources.getString(R.string.card_title_status)));
            }
        };
        Log.i("List Size ",String.valueOf(listItem.size()));

        gridView = (GridView)findViewById(R.id.menu_grid_view);
        gridView.setAdapter(new MenuGridAdapter(listItem, this));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public static final int GRID_POSITION_ALL_LIST = 0;
            public static final int GRID_POSITION_FAVORITE_LIST = 1;
            public static final int GRID_POSITION_SEARCH_NEAR = 2;
            public static final int GRID_POSITION_SELECT_BY_MAP = 3;
            public static final int GRID_POSITION_STATUS = 4;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case GRID_POSITION_ALL_LIST:
                        Intent intent = new Intent(StartMenuActivity.this, SqliteDiary2.class);
                        startActivity(intent);
                        break;
                    case GRID_POSITION_FAVORITE_LIST:
                        Intent intent1 = new Intent(StartMenuActivity.this, FavoriteActivity.class);
                        startActivity(intent1);
                        break;
                    case GRID_POSITION_SEARCH_NEAR:
                        Intent intent2 = new Intent(StartMenuActivity.this, SearchNearActivity.class);
                        startActivity(intent2);
                        break;
                    case GRID_POSITION_SELECT_BY_MAP:
                        Intent intent3 = new Intent(StartMenuActivity.this, SelectByMapActivity.class);
                        startActivity(intent3);
                        break;
                    case GRID_POSITION_STATUS:
                        Intent intent4 = new Intent(StartMenuActivity.this, StatusActivity.class);
                        startActivity(intent4);
                }
            }
        });

    }


}
