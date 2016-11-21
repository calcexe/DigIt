package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.calc_exe.wykop.model.domain.extras.ErrorItem;
import pl.calc_exe.wykop.model.domain.extras.IdItem;
import pl.calc_exe.wykop.model.domain.extras.VoteItem;

@Getter
@EqualsAndHashCode
public class Entry implements IdItem, ErrorItem, VoteItem {

    @SerializedName("error")
    @Expose
    private Error error = new Error();

    @SerializedName("voters")
    @Expose
    private List<Dig> voters = new ArrayList<>();

    @SerializedName("app")
    @Expose
    private String app;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("blocked")
    @Expose
    private boolean blocked;

    @SerializedName("author_avatar_lo")
    @Expose
    private String authorAvatarLo;

    @SerializedName("user_favorite")
    @Expose
    private boolean userFavorite;

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("comment_count")
    @Expose
    private int commentCount;

    @SerializedName("comments")
    @Expose
    private List<EntryComment> comments = new ArrayList<>();

    @SerializedName("source")
    @Expose
    private Object source;

    @Setter @SerializedName("vote_count")
    @Expose
    private int voteCount;

    @SerializedName("author_avatar_med")
    @Expose
    private String authorAvatarMed;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("user_vote")
    @Expose
    @Setter private int userVote;

    @SerializedName("deleted")
    @Expose
    private boolean deleted;

    @SerializedName("author_group")
    @Expose
    private int authorGroup;

    @SerializedName("date")
    @Expose
    private DateTime date;

    @SerializedName("author_avatar_big")
    @Expose
    private String authorAvatarBig;

    @SerializedName("embed")
    @Expose
    private Embed embed = new Embed();

    @SerializedName("author_sex")
    @Expose
    private String authorSex;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("violation_url")
    @Expose
    private String violationUrl;

    @SerializedName("author_avatar")
    @Expose
    private String authorAvatar;

    @SerializedName("can_comment")
    @Expose
    private boolean canComment;


//    @Expose @SerializedName("id")
//    private int id;
//
//    @Expose @SerializedName("blocked")
//    private boolean blocked;
//
//    @Expose @SerializedName("author_avatar_lo")
//    private String authorAvatarLo;
//
//    @Expose @SerializedName("user_favorite")
//    private boolean userFavorite;
//
//    @Expose @SerializedName("author")
//    private String author;
//
//    @Expose @SerializedName("comment_count")
//    private int commentCount;
//
//    @Setter @Expose @SerializedName("vote_count")
//    private int voteCount;
//
//    @Expose @SerializedName("author_avatar_med")
//    private String authorAvatarMed;
//
//    @Expose @SerializedName("type")
//    private String type;
//
//    @Expose @SerializedName("body")
//    private String body;
//
//    @Setter @Expose @SerializedName("user_vote")
//    private int userVote;
//
//    @Expose @SerializedName("deleted")
//    private boolean deleted;
//
//    @Expose @SerializedName("author_group")
//    private int authorGroup;
//
//    @Expose @SerializedName("date")
//    private DateTime date;
//
//    @Expose @SerializedName("author_avatar_big")
//    private String authorAvatarBig;
//
//    @Expose @SerializedName("embed")
//    private Embed embed = new Embed();
//
//    @Expose @SerializedName("author_sex")
//    private String authorSex;
//
//    @Expose @SerializedName("url")
//    private String url;
//
//    @Expose @SerializedName("violation_url")
//    private String violationUrl;
//
//    @Expose @SerializedName("author_avatar")
//    private String authorAvatar;

}
