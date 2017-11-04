package freshman.sqlitediary2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by gin on 2017/03/14.
 */

public class FragmentOverview extends Fragment {

    Cursor res;
    int row_id;
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;

    TextView overview_content_text_view;
    String overview_content_str = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);


        Bundle bundle = getArguments();
        row_id = bundle.getInt("row_id");


        databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getWritableDatabase();
        res = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        res.moveToPosition(row_id);


        overview_content_str = res.getString(14);


        overview_content_text_view = (TextView)view.findViewById(R.id.overview_content_text_view);
        overview_content_text_view.setText(overview_content_str);

        return view;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
