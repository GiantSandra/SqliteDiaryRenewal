package freshman.sqlitediary2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by gin on 2017/03/15.
 */

public class SettingEvaluationListAdapter extends ArrayAdapter {

    int resource;
    List<SettingEvaluationListItem> items;
    LayoutInflater inflater;

    public SettingEvaluationListAdapter(Context context, int resource, List<SettingEvaluationListItem> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView != null){
            view = convertView;
        }else{
            view = inflater.inflate(resource, null);
        }

        SettingEvaluationListItem item = items.get(position);



        return view;
    }
}
