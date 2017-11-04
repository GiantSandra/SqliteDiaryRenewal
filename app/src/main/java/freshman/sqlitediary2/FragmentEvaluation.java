package freshman.sqlitediary2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.ArrayList;

/**
 * Created by gin on 2017/03/14.
 */

public class FragmentEvaluation extends Fragment {

    View view;

    Cursor res;
    int row_id;
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    public static final String TABLE_NAME = "sento_table";

    ListView listView;
    EvaluationListAdapter adapter;

    String address = null;
    String types = null;
    String detail = null;
    String memo = null;

    TextView memo_text_view;
    TextView address_text_view;
    TextView detail_text_view;
    TextView types_text_view;

    ProgressDialog progressDialog;

    Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_evaluation, container, false);

        Bundle bundle = getArguments();
        row_id = bundle.getInt("row_id");

        setDatabase();
        setCursorAndMoveToPosition();
        setViews();
        setAllDataInFragmentEvaluation();

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        database.close();
    }

    public void setDatabase()
    {
        databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getWritableDatabase();
    }

    public void setCursorAndMoveToPosition()
    {
        res = database.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);
        res.moveToPosition(row_id);
    }

    public void setViews()
    {
        listView = (ListView) view.findViewById(R.id.evaluation_listview);
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);

        memo_text_view = (TextView) view.findViewById(R.id.memo_text_view);
        address_text_view = (TextView) view.findViewById(R.id.address_text_view);
        detail_text_view = (TextView) view.findViewById(R.id.detail_text_view);
        types_text_view = (TextView) view.findViewById(R.id.types_text_view);
    }

    public void setAllDataInFragmentEvaluation()
    {
        ArrayList<EvaluationListItem> listItems = new ArrayList<EvaluationListItem>();
        String[] titles = {"風呂　 : ", "脱衣所 : ", "休憩所 : ", "温度　 : ", "総合　 : "};
        for (int i = 0; i < titles.length; i++) {
            int num;
            try {
                num = res.getInt(i + 5);
            } catch (Exception e) {
                num = 0;
            }
            float num_star = (float) num / 2;
            EvaluationListItem item = new EvaluationListItem(titles[i], num_star, num_star);
            listItems.add(item);
        }
        adapter = new EvaluationListAdapter(getContext(), R.layout.evaluation_list_item, listItems);
        listView.setAdapter(adapter);

        final String NO_INFO = "※情報がありません。";
        try {
            if ((address = res.getString(2)) == null) {
                address = NO_INFO;
            }
        } catch (Exception e) {
            address = NO_INFO;
        }
        try {
            if ((detail = res.getString(3)) == null) {
                detail = NO_INFO;
            }
        } catch (Exception e) {
            detail = NO_INFO;
        }
        try {
            if ((types = res.getString(4)) == null) {
                types = NO_INFO;
            }
        } catch (Exception e) {
            types = NO_INFO;
        }
        try {
            if ((memo = res.getString(15)) == null) {
                memo = NO_INFO;
            }
        } catch (Exception e) {
            memo = NO_INFO;
        }

        memo_text_view.setText(memo);
        address_text_view.setText(address);
        detail_text_view.setText(detail);
        types_text_view.setText(types);
    }

}
