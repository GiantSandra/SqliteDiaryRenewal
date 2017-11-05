package freshman.sqlitediary2.menu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import freshman.sqlitediary2.R;

/**
 * Created by gin on 2017/11/04.
 */

public class MenuGridAdapter extends BaseAdapter{

    private List<StartMenuCard> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public class ViewHolder{
        public ImageView imageView;
        public TextView textView;
    }

    public MenuGridAdapter(List<StartMenuCard> list, Context context){
        this.list = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.start_menu_selection, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.card_view_image);
            viewHolder.textView = (TextView)convertView.findViewById(R.id.card_view_title);
            viewHolder.textView.setText(list.get(position).getTitle());
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        return convertView;
    }
}
