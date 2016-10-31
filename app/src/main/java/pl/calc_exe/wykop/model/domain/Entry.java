package pl.calc_exe.wykop.model.domain;

import android.os.Build;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.Objects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import pl.calc_exe.wykop.model.domain.extras.IError;
import pl.calc_exe.wykop.model.domain.extras.IdItem;

@Getter
@EqualsAndHashCode
public class Entry implements IdItem, IError {

    @Expose @SerializedName("error")
    private Error error = new Error();

    @Expose @SerializedName("app")
    private String app;

    @Expose @SerializedName("id")
    private int id;

    @Expose @SerializedName("blocked")
    private boolean blocked;

    @Expose @SerializedName("author_avatar_lo")
    private String authorAvatarLo;

    @Expose @SerializedName("user_favorite")
    private boolean userFavorite;

    @Expose @SerializedName("author")
    private String author;

    @Expose @SerializedName("comment_count")
    private int commentCount;

    @Setter @Expose @SerializedName("vote_count")
    private int voteCount;

    @Expose @SerializedName("author_avatar_med")
    private String authorAvatarMed;

    @Expose @SerializedName("type")
    private String type;

    @Expose @SerializedName("body")
    private String body;

    @Setter @Expose @SerializedName("user_vote")
    private int userVote;

    @Expose @SerializedName("deleted")
    private boolean deleted;

    @Expose @SerializedName("author_group")
    private int authorGroup;

    @Expose @SerializedName("date")
    private DateTime date;

    @Expose @SerializedName("author_avatar_big")
    private String authorAvatarBig;

    @Expose @SerializedName("embed")
    private Embed embed = new Embed();

    @Expose @SerializedName("author_sex")
    private String authorSex;

    @Expose @SerializedName("url")
    private String url;

    @Expose @SerializedName("violation_url")
    private String violationUrl;

    @Expose @SerializedName("author_avatar")
    private String authorAvatar;

}
