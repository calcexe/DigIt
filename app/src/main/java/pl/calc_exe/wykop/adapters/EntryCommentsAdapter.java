package pl.calc_exe.wykop.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.di.components.DaggerMainComponent;
import pl.calc_exe.wykop.di.modules.PreferencesModule;
import pl.calc_exe.wykop.extras.EntryUtils;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.extras.TextUtils;
import pl.calc_exe.wykop.model.domain.Embed;
import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.domain.EntryComment;
import pl.calc_exe.wykop.model.domain.Groups;
import pl.calc_exe.wykop.presenter.fragments.EntryPresenter;

public class EntryCommentsAdapter extends RecyclerView.Adapter<EntryCommentsAdapter.Holder> {

    private Context context;
    private Entry entry;
    @Inject Preferences preferences;
    private EntryPresenter presenter;

    public EntryCommentsAdapter(Context context, EntryPresenter presenter, Entry entry) {
        this.context = context;
        this.presenter = presenter;
        this.entry = entry;
        DaggerMainComponent.builder().preferencesModule(new PreferencesModule()).build().inject(this);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stream, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        int voteCount, userVote, authorGroup;
        String authorAvatar, author, body;
        Embed embed;
        DateTime date;

        if (position == 0) {
            authorAvatar = entry.getAuthorAvatar();
            embed = entry.getEmbed();
            author = entry.getAuthor();
            date = entry.getDate();
            voteCount = entry.getVoteCount();
            body = entry.getBody();
            userVote = entry.getUserVote();
            authorGroup = entry.getAuthorGroup();
        } else {
            EntryComment comment = entry.getComments().get(position - 1);
            authorAvatar = comment.getAuthorAvatar();
            embed = comment.getEmbed();
            author = comment.getAuthor();
            date = comment.getDate();
            voteCount = comment.getVoteCount();
            body = comment.getBody();
            userVote = comment.getUserVote();
            authorGroup = comment.getAuthorGroup();
        }

        holder.position = holder.getAdapterPosition();
        Picasso.with(context).load(authorAvatar).into(holder.entryAuthorAvatar);
        holder.entryTitle.setText(EntryUtils.htmlToSpanned(body));
        holder.entryDate.setText(TextUtils.getDateText(date));
        holder.entryPlusCount.setText(String.valueOf(voteCount));
        holder.entryAuthor.setTextColor(Color.parseColor(Groups.getColor(authorGroup)));
        holder.entryAuthor.setText(String.format(context.getResources().getString(R.string.author_placeholder), author));

        if (isEmbedEmpty(embed)) {
            holder.entryImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(embed.getPreview()).into(holder.entryImage);
        } else {
            holder.entryImage.setImageDrawable(null);
            holder.entryImage.setVisibility(View.GONE);
        }
        Log.e("USERVOTE", String.valueOf(userVote));
        if (userVote == 1) {
            holder.entryUpVote.setImageResource(R.drawable.arrow_up_green);
            holder.entryDownVote.setImageResource(R.drawable.arrow_down_light);
            holder.entryUpVote.setVisibility(View.VISIBLE);
            holder.entryDownVote.setVisibility(View.VISIBLE);
        } else {
            holder.entryDownVote.setVisibility(View.GONE);
            if (preferences.isLogged())
                holder.entryUpVote.setImageResource(R.drawable.arrow_up_light);
            else
                holder.entryUpVote.setImageResource(R.drawable.arrow_up_dark);
        }
    }

    @Override
    public int getItemCount() {
        return entry == null ? 0 : entry.getComments().size() + 1;
    }

    private boolean isEmbedEmpty(Embed embed) {
        return embed != null && !embed.getType().equals("none");
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.entry_author_avatar) CircleImageView entryAuthorAvatar;
        @BindView(R.id.entry_author) TextView entryAuthor;
        @BindView(R.id.entry_menu) ImageView entryMenu;
        @BindView(R.id.entry_date) TextView entryDate;
        @BindView(R.id.entry_comments_count) TextView entryCommentsCount;
        @BindView(R.id.entry_up_vote) ImageView entryUpVote;
        @BindView(R.id.entry_plus_count) AutofitTextView entryPlusCount;
        @BindView(R.id.entry_down_vote) ImageView entryDownVote;
        @BindView(R.id.entry_title) TextView entryTitle;
        @BindView(R.id.entry_image) ImageView entryImage;
        @BindView(R.id.entry_layout) CardView entryLayout;
        @BindView(R.id.entry_date_separator) TextView entryDateSeparator;
        int position;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            entryMenu.setVisibility(View.GONE);
            entryCommentsCount.setVisibility(View.GONE);
            entryDownVote.setVisibility(View.GONE);
            entryDateSeparator.setVisibility(View.GONE);

            entryTitle.setMovementMethod(LinkMovementMethod.getInstance());
        }

        @OnClick(R.id.entry_up_vote)
        public void upVote() {
            presenter.upVote(position);
        }

        @OnClick(R.id.entry_down_vote)
        public void downVote() {
            presenter.downVote(position);
        }

        @OnClick({R.id.entry_author_avatar, R.id.entry_author})
        public void showUser(){
            presenter.showUser(position);
        }
    }
}