package pl.calc_exe.wykop.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.extras.Preferences;

/**
 * Adapter for NavigationView (menu at left).
 */
public class NavigationViewAdapter extends BaseAdapter {

    private Context context;
    private Preferences preferences;
    private int selected;
    private String[] rows;

    public NavigationViewAdapter(Context context, Preferences preferences) {
        this.context = context;
        this.preferences = preferences;
        rows = context.getResources().getStringArray(R.array.navigation_items);
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rows.length + 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (position < rows.length) {
            viewHolder.itemTitle.setText(rows[position]);
        } else {
            if (preferences.isLogged())
                viewHolder.itemTitle.setText(context.getString(R.string.sign_out));
            else
                viewHolder.itemTitle.setText(context.getString(R.string.sign_in));
        }

        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.colorPrimaryDark});
        int primary = typedArray.getColor(0, ContextCompat.getColor(context, R.color.colorPrimary));
        int primaryDark = typedArray.getColor(1, ContextCompat.getColor(context, R.color.colorPrimary));
        typedArray.recycle();

        if (position == selected)
            viewHolder.itemTitle.setBackgroundColor(primaryDark);
        else
            viewHolder.itemTitle.setBackgroundColor(primary);

        return view;
    }

    class ViewHolder {

        @BindView(android.R.id.text1)
        TextView itemTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
