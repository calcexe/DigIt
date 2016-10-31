package pl.calc_exe.wykop.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.extras.TextUtils;
import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.domain.Groups;
import pl.calc_exe.wykop.model.domain.Sexes;
import pl.calc_exe.wykop.presenter.extras.EntryTagHandler;
import pl.calc_exe.wykop.presenter.fragments.StreamPresenter;

/**
 * Controls RecyclerView for StreamService(mikroblog).
 * Extends {@link ProgressRecycler} and allows to display a loading element at bottom of the list.*/
public class StreamListAdapter extends ProgressRecycler<Entry, StreamListAdapter.ItemHolder> {

    private String body = "Wo\u0142am przez <a href=\"http://mirkolisty.pvu.pl\" rel=\"nofollow\">MirkoListy</a> plusuj\u0105cych <a href=\"http://wykop.pl/wpis/20305557\" rel=\"nofollow\">ten wpis</a> (858)<br />\n<br />\n<code class=\"dnone\">  @<a href=\"@quzipl\">quzipl</a> @<a href=\"@kotym\">kotym</a> @<a href=\"@meql\">meql</a> @<a href=\"@Liesbaum\">Liesbaum</a> @<a href=\"@RuskiAgent1917\">RuskiAgent1917</a> @<a href=\"@gibpotatoe\">gibpotatoe</a> @<a href=\"@Herbatnik\">Herbatnik</a> @<a href=\"@Pshemeck\">Pshemeck</a> @<a href=\"@adam-skowyt\">adam-skowyt</a> @<a href=\"@tymol14\">tymol14</a> @<a href=\"@sty\">sty</a> @<a href=\"@Refusek\">Refusek</a> @<a href=\"@Diamonddog\">Diamonddog</a> @<a href=\"@QBA__\">QBA__</a> @<a href=\"@Macieq_ja\">Macieq_ja</a> @<a href=\"@Tomm16\">Tomm16</a> @<a href=\"@flavisoto\">flavisoto</a> @<a href=\"@KrowaMleczna\">KrowaMleczna</a> @<a href=\"@IgorS\">IgorS</a> @<a href=\"@Migfirefox\">Migfirefox</a> @<a href=\"@elim\">elim</a> @<a href=\"@Tasanga\">Tasanga</a> @<a href=\"@TomiliDzonsWSciganym\">TomiliDzonsWSciganym</a> @<a href=\"@Mikoma\">Mikoma</a> @<a href=\"@MJToshi\">MJToshi</a> @<a href=\"@xaawer\">xaawer</a> @<a href=\"@kuskoman\">kuskoman</a> @<a href=\"@merk\">merk</a> @<a href=\"@muszelec\">muszelec</a> @<a href=\"@aso824\">aso824</a> @<a href=\"@2PacShakur\">2PacShakur</a> @<a href=\"@DNL_YOUH\">DNL_YOUH</a> @<a href=\"@Borsuk673\">Borsuk673</a> @<a href=\"@derylio1\">derylio1</a> @<a href=\"@kasaqwerty\">kasaqwerty</a> @<a href=\"@oktaNITRO\">oktaNITRO</a> @<a href=\"@zielony_kurczak\">zielony_kurczak</a> @<a href=\"@ZjedliMiRogale\">ZjedliMiRogale</a> @<a href=\"@D3v0\">D3v0</a> @<a href=\"@Doleginho\">Doleginho</a> @<a href=\"@beduaz\">beduaz</a> @<a href=\"@oreze\">oreze</a> @<a href=\"@Herubin\">Herubin</a> @<a href=\"@FrauWolf\">FrauWolf</a> @<a href=\"@jasiek12321\">jasiek12321</a> @<a href=\"@Elitek\">Elitek</a> @<a href=\"@ziomeczek\">ziomeczek</a> @<a href=\"@rkzm2012\">rkzm2012</a> @<a href=\"@gizio46\">gizio46</a> @<a href=\"@xDyzio\">xDyzio</a><br />\n</code>";

    /**TagHandler which allows to handle custom tags (not supported by Html.fromHtml()).*/
    private EntryTagHandler tagHandler = new EntryTagHandler();
    private StreamPresenter presenter;

    public StreamListAdapter(Context context, StreamPresenter presenter) {
        super(R.layout.item_stream, context);
        this.presenter = presenter;
    }

    @Override
    public void onItemBind(ItemHolder viewHolder, int position) {

        Entry entry = list.get(position);
        viewHolder.entry = entry;

        Picasso.with(context).load(entry.getAuthorAvatarMed()).into(viewHolder.entryAuthorAvatar);

        if (entry.getEmbed() != null && entry.getEmbed().getPreview() != null && !entry.getEmbed().getPreview().equals("")) {
            viewHolder.entryImage.setVisibility(View.VISIBLE);
            Picasso.with(context).load(entry.getEmbed().getPreview()).into(viewHolder.entryImage);

        } else
            viewHolder.entryImage.setVisibility(View.GONE);

        viewHolder.entryAuthorAvatar.setBorderColor(Color.parseColor(Sexes.getMaleColor(entry.getAuthorSex())));
        viewHolder.entryAuthorAvatar.setBorderWidth(3);

        viewHolder.entryAuthor.setText(
                String.format(context.getResources().getString(R.string.author_placeholder),
                        entry.getAuthor()));
        viewHolder.entryAuthor.setTextColor(Color.parseColor(Groups.getColor(entry.getAuthorGroup())));

        viewHolder.entryTitle.setText(getSpannedBody(entry.getBody()));
        viewHolder.entryTitle.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.entryDate.setText(TextUtils.getDateText(entry.getDate()));
        viewHolder.entryCommentsCount.setText(TextUtils.getCommentsText(entry.getCommentCount()));
        viewHolder.entryPlusCount.setText(String.valueOf(entry.getVoteCount()));

        switch (entry.getUserVote()) {
            case 1:
                viewHolder.entryUpVote.setImageResource(R.drawable.arrow_up_green);
                viewHolder.entryDownVote.setImageResource(R.drawable.arrow_down_light);
                viewHolder.entryUpVote.setVisibility(View.VISIBLE);
                viewHolder.entryDownVote.setVisibility(View.VISIBLE);
                break;
            case -1:
                viewHolder.entryUpVote.setImageResource(R.drawable.arrow_down_light);
                viewHolder.entryDownVote.setImageResource(R.drawable.arrow_down_red);
                viewHolder.entryUpVote.setVisibility(View.VISIBLE);
                viewHolder.entryDownVote.setVisibility(View.VISIBLE);
                break;
            default:
                viewHolder.entryDownVote.setVisibility(View.GONE);
                if (preferences.isLogged())
                    viewHolder.entryUpVote.setImageResource(R.drawable.arrow_up_light);
                else
                    viewHolder.entryUpVote.setImageResource(R.drawable.arrow_up_dark);
        }
    }

    @Override
    public ItemHolder getItemViewHolder(View view) {
        return new ItemHolder(view);
    }

    public void onTakeList(List<Entry> entries) {
        this.list = entries;
    }

    /**
     * @param body Element to transform into {@link Spanned}
     * @return Returns {@link Spanned} with formatted and clickable elements.*/
    private Spanned getSpannedBody(String body) {

        //Sometimes fromHtml is crashing, so I need to add this two lines.
        //Don't ask why, copy - paste from stackoverflow.
        body = body.replace("<^[stu]", " <");
        body = body.replace("  ", " ");

        //Replace APIs spoiler code to my own.

        Pattern pattern = Pattern.compile("(?s)<code class=\"dnone\">(.*?)</code>");
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()){
            body = matcher.replaceAll("<spoiler>" + Html.fromHtml(matcher.group(1)) + "</spoiler>");
        }

        //Replace APIs user link code to my own.
        body = body.replaceAll("(?s)<a href=\"@(.*?)\">.*?</a>", "<user>$1</user>");

        //Replace APIs hashtag link code to my own.
        body = body.replaceAll("(?s)<a href=\"#(.*?)\">.*?</a>", "<tag>$1</tag>");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, null, tagHandler);
        } else{
            return Html.fromHtml(body, null, tagHandler);
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

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
        Entry entry;

        ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            entryMenu.setVisibility(View.INVISIBLE);
        }

        @OnClick(R.id.entry_image)
        public void onAttachmentClick() {
            presenter.attachmentClick(entry.getEmbed().getType(), entry.getEmbed().getUrl());
        }

        @OnClick({R.id.entry_author_avatar, R.id.entry_author})
        public void onUserClick(){
            presenter.showUser(entry.getAuthor());
        }

        @OnClick({R.id.entry_date, R.id.entry_comments_count, R.id.entry_layout})
        public void onEntryClick(){
            presenter.showEntry(entry);
        }

        @OnClick(R.id.entry_down_vote)
        public void onDownVote(){
            presenter.downVote(entry);
        }

        @OnClick(R.id.entry_up_vote)
        public void onUpVote(){
            presenter.onUpVote(entry);
        }
    }
}
