package pl.calc_exe.wykop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.di.Pref;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.model.domain.extras.IdItem;

/**
 * Abstract class which allows to show progress spinner at bottom of the page.
 */

public abstract class ProgressRecycler<T extends IdItem, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    private int ITEM = 0;
    private int PROGRESS = 1;

    private int layout;

    protected List<T> list;
    private boolean scrollEnd = false;

    protected Context context;
    protected Preferences preferences;


    ProgressRecycler(int layout, Context context) {
        this.layout = layout;
        list = new ArrayList<>();
        this.context = context;
        preferences = Pref.getInstance().get();
    }

    public abstract void onItemBind(VH viewHolder, int position);

    public abstract VH getItemViewHolder(View view);

    public void setScrollEnd(boolean end) {
        scrollEnd = end;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
            return getItemViewHolder(view);
        } else if (viewType == PROGRESS) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
            return new ProgressViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //Using view holders for progress bar and items.
        if (holder instanceof ProgressViewHolder) {
            if (scrollEnd)
                ((ProgressViewHolder) holder).progressBar.setVisibility(View.GONE);
            else
                ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        } else {
            //noinspection unchecked
            onItemBind((VH) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        //Return +1 element for progress bar on bottom of list.

        if (list.size() == 0)
            return 0;
        if (scrollEnd)
            return list.size();

        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        //Return type of element. If position is bigger than link.size then show progress bar.
        return position >= list.size() ? PROGRESS : ITEM;
    }

    static class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.link_progress) ProgressBar progressBar;

        ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
