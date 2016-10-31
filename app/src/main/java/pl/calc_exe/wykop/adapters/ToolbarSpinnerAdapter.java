package pl.calc_exe.wykop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.calc_exe.wykop.R;

public class ToolbarSpinnerAdapter extends BaseAdapter {

    private Context context;
    private String[] elements;
    private String hint;

    public ToolbarSpinnerAdapter(Context context, String hint, int textViewResourceId) {
        this.context = context;
        this.hint = hint;
        elements = context.getResources().getStringArray(textViewResourceId);
    }

    @Override
    public int getCount() {
        return elements.length;
    }

    @Override
    public Object getItem(int i) {
        return elements[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_toolbar_spinner, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.dropdown.setVisibility(View.GONE);

        holder.hint.setVisibility(View.VISIBLE);
        holder.hint.setText(hint);

        holder.text.setVisibility(View.VISIBLE);
        holder.text.setText(elements[position]);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_toolbar_spinner, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.dropdown.setVisibility(View.VISIBLE);
        holder.dropdown.setText(elements[position]);

        holder.hint.setVisibility(View.GONE);
        holder.text.setVisibility(View.GONE);

        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.spinner_hint) TextView hint;
        @BindView(R.id.spinner_text) TextView text;
        @BindView(R.id.spinner_dropdown) TextView dropdown;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
