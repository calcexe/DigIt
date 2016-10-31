package pl.calc_exe.wykop.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.extras.TextUtils;
import pl.calc_exe.wykop.model.domain.Groups;
import pl.calc_exe.wykop.model.domain.Link;
import pl.calc_exe.wykop.model.domain.UserVote;
import pl.calc_exe.wykop.presenter.fragments.IndexPresenter;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.NORMAL;

public class IndexListAdapter extends ProgressRecycler<Link, IndexListAdapter.ItemHolder> {

    private IndexPresenter presenter;

    public IndexListAdapter(Context context, IndexPresenter presenter) {
        super(R.layout.item_link_new, context);
        this.presenter = presenter;
    }

    @Override
    public void onItemBind(IndexListAdapter.ItemHolder holder, int position) {
        Link link = list.get(position);

        if (link.getAuthorAvatarMed() != null)
            Picasso.with(context).load(link.getAuthorAvatarMed()).into(holder.linkAuthorAvatar);

        holder.linkAuthor.setTextColor(Color.parseColor(Groups.getColor(link.getAuthorGroup())));
        holder.linkAuthor.setText(String.format(context.getResources().getString(R.string.author_placeholder), link.getAuthor()));
        holder.linkTitle.setText(link.getTitle());
        holder.linkBody.setText(link.getDescription());
        holder.linkDate.setText(TextUtils.getDateText(link.getDate()));
        holder.linkCommentsCount.setText(TextUtils.getCommentsText(link.getCommentCount()));

        if (link.getIsHot()) {
            holder.linkPlusCount.setTextColor(ContextCompat.getColor(context, R.color.textHot));
            holder.linkPlusCount.setTypeface(null, BOLD);
        } else {
            holder.linkPlusCount.setTextColor(ContextCompat.getColor(context, R.color.darkTextColor));
            holder.linkPlusCount.setTypeface(null, NORMAL);
        }

        if (!preferences.isLogged() || !link.getCanVote()) {
            holder.linkUpVote.setImageResource(R.drawable.arrow_up_dark);
            holder.linkDownVote.setImageResource(R.drawable.arrow_down_dark);
        } else {
            if (link.getVote() == UserVote.DIG) {
                holder.linkUpVote.setImageResource(R.drawable.arrow_up_green);
                holder.linkDownVote.setImageResource(R.drawable.arrow_down_light);
            } else if (link.getVote() == UserVote.BURY) {
                holder.linkUpVote.setImageResource(R.drawable.arrow_up_light);
                holder.linkDownVote.setImageResource(R.drawable.arrow_down_red);
            } else {
                holder.linkUpVote.setImageResource(R.drawable.arrow_up_light);
                holder.linkDownVote.setImageResource(R.drawable.arrow_down_light);
            }
        }

        if (link.getUserFavorite())
            holder.popupMenu.getMenu().findItem(R.id.menu_link_favourite).setTitle(R.string.remove_favourite);
        else
            holder.popupMenu.getMenu().findItem(R.id.menu_link_favourite).setTitle(R.string.favourite);

        holder.linkPlusCount.setText(String.valueOf(link.getVoteCount()));


        holder.link = link;
    }

    @Override
    public IndexListAdapter.ItemHolder getItemViewHolder(View view) {
        return new ItemHolder(view);
    }

    public void onTakeList(List<Link> entries) {
        this.list = entries;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener {

        @BindView(R.id.link_author) TextView linkAuthor;
        @BindView(R.id.link_date) TextView linkDate;
        @BindView(R.id.link_comments_count) TextView linkCommentsCount;
        @BindView(R.id.link_down_vote) ImageView linkDownVote;
        @BindView(R.id.link_plus_count) TextView linkPlusCount;
        @BindView(R.id.link_up_vote) ImageView linkUpVote;
        @BindView(R.id.link_title) TextView linkTitle;
        @BindView(R.id.link_author_avatar) CircleImageView linkAuthorAvatar;
        @BindView(R.id.link_body) TextView linkBody;
        @BindView(R.id.link_menu) View linkMenu;
        Link link;
        private PopupMenu popupMenu;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            popupMenu = new PopupMenu(itemView.getContext(), linkMenu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_link, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(this);
            linkMenu.setVisibility(View.INVISIBLE);
        }

        @OnClick({R.id.link_date, R.id.link_comments_count, R.id.link_title, R.id.link_body, R.id.link_layout})
        public void onLinkClick(View view) {
            presenter.showLink(link);
        }

        @OnClick({R.id.link_author, R.id.link_author_avatar})
        public void onUserClick(View view) {
            presenter.showUser(link.getAuthor());
        }

        @OnClick(R.id.link_up_vote)
        public void onUpVoteClick(View view) {
            presenter.upVote(link);
        }

        @OnClick(R.id.link_menu)
        public void onMenuClick() {
            popupMenu.show();
        }

        @OnClick(R.id.link_down_vote)
        public void onDownVoteClick(View view) {
            presenter.downVote(link);
        }

//        @OnLongClick({R.id.link_author, R.id.link_date, R.id.link_comments_count, R.id.link_title, R.id.link_body, R.id.link_author_avatar})
//        boolean onMenuLongClick() {
//            popupMenu.show();
//            return true;
//        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            presenter.itemMenuClick(menuItem.getItemId());
            return true;
        }
    }
}