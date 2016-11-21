package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.calc_exe.wykop.model.domain.extras.IdItem;
import pl.calc_exe.wykop.model.domain.extras.VoteItem;

@Getter
public class EntryComment implements IdItem, VoteItem{
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("author_avatar_lo")
    @Expose
    private String authorAvatarLo;
    @SerializedName("author_sex")
    @Expose
    private String authorSex;
    @SerializedName("violation_url")
    @Expose
    private Object violationUrl;
    @SerializedName("user_vote")
    @Expose
    @Setter private int userVote;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("deleted")
    @Expose
    private boolean deleted;
    @SerializedName("app")
    @Expose
    private Object app;
    @SerializedName("voters")
    @Expose
    private List<Dig> voters = new ArrayList<>();
    @SerializedName("vote_count")
    @Expose
    @Setter private int voteCount;
    @SerializedName("source")
    @Expose
    private Object source;
    @SerializedName("author_avatar")
    @Expose
    private String authorAvatar;
    @SerializedName("entry_id")
    @Expose
    private int entryId;
    @SerializedName("author_group")
    @Expose
    private int authorGroup;
    @SerializedName("author_avatar_med")
    @Expose
    private String authorAvatarMed;
    @SerializedName("date")
    @Expose
    private DateTime date;
    @SerializedName("author_avatar_big")
    @Expose
    private String authorAvatarBig;
    @SerializedName("embed")
    @Expose
    private Embed embed = new Embed();
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("blocked")
    @Expose
    private boolean blocked;
}