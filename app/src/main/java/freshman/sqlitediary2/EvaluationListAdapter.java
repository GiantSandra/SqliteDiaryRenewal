package freshman.sqlitediary2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gin on 2017/03/14.
 */

public class EvaluationListAdapter extends ArrayAdapter {

    int resource;
    List<EvaluationListItem> items;
    LayoutInflater inflater;

    public EvaluationListAdapter(Context context, int resource, List<EvaluationListItem> items) {
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

        EvaluationListItem item = items.get(position);

        TextView text_view_title = (TextView)view.findViewById(R.id.text_view_title);
        text_view_title.setText(item.getTitle());

        RatingBar evaluation_rating_bar = (RatingBar)view.findViewById(R.id.evaluation_rating_bar);
        evaluation_rating_bar.setNumStars(5);
        evaluation_rating_bar.setStepSize((float)0.5);
        evaluation_rating_bar.setRating(item.getNum());
        evaluation_rating_bar.setIsIndicator(true);

        TextView text_view_num_of_star = (TextView)view.findViewById(R.id.text_view_num_of_star);
        text_view_num_of_star.setText(Float.toString(item.getNum()));

        return view;
    }
}
