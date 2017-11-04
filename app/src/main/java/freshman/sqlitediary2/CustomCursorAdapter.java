package freshman.sqlitediary2;

import android.content.Context;
import android.database.Cursor;
import java.util.Calendar;

import android.graphics.Color;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by gin on 2017/03/19.
 */

public class CustomCursorAdapter extends SimpleCursorAdapter {

    static class ViewHolder {

        public LinearLayout diary_main_list_item_linear_layout;
        public TextView item_sento_name;
        public TextView item_sento_detail;
        public ImageView check_circle_image;

    }

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.diary_main_list_item, null, true);
        ViewHolder holder = new ViewHolder();

        holder.diary_main_list_item_linear_layout = (LinearLayout)view.findViewById(R.id.diary_main_list_item_linear_layout);
        holder.item_sento_name = (TextView)view.findViewById(R.id.item_sento_name);
        holder.item_sento_detail = (TextView)view.findViewById(R.id.item_sento_detail);
        holder.check_circle_image = (ImageView)view.findViewById(R.id.check_circle_image);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = (ViewHolder)view.getTag();

        String sento_name = cursor.getString(cursor.getColumnIndex("NAME"));
        holder.item_sento_name.setText(sento_name);

        String sento_detail = cursor.getString(cursor.getColumnIndex("DETAIL"));
        holder.item_sento_detail.setText(sento_detail);

        int already_visited = cursor.getInt(cursor.getColumnIndex("VISITED"));
        if(already_visited == 1){
            holder.check_circle_image.setImageResource(R.drawable.sample_green_cat_footprint_96);
        }else if(already_visited == 0){
            holder.check_circle_image.setImageResource(R.drawable.unvisible_circle_item);
        }

        Calendar calendar = Calendar.getInstance();
        int today_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        String[] name_of_today_of_week = {"日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"};
        if(sento_detail.contains(name_of_today_of_week[today_of_week])){
            int color = Color.parseColor("#E70101");
            holder.item_sento_name.setTextColor(color);
        }else{
            holder.item_sento_name.setTextColor(Color.BLACK);
        }


    }
}
