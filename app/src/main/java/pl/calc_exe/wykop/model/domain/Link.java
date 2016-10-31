package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import pl.calc_exe.wykop.model.domain.extras.IError;
import pl.calc_exe.wykop.model.domain.extras.IdItem;

@Getter
public class Link implements IdItem, IError {

    @Expose @SerializedName("error")
    private Error error = new Error();

    @Expose @SerializedName("app")
    private Object app;

    @Expose @SerializedName("author_avatar_lo")
    private String authorAvatarLo;

    @Expose @SerializedName("has_own_content")
    private Boolean hasOwnContent;

    @Expose @SerializedName("id")
    private int id;

    @Expose @SerializedName("category")
    private String category;

    @Expose @SerializedName("author_group")
    private Integer authorGroup;

    @Expose @SerializedName("group")
    private String group;

    @Expose @SerializedName("user_favorite")
    private Boolean userFavorite;

    @Expose @SerializedName("title")
    private String title;

    @Setter @Expose @SerializedName("vote_count")
    private Integer voteCount;

    @Setter @Expose @SerializedName("report_count")
    private Integer reportCount;

    @Setter @Expose @SerializedName("user_vote")
    private UserVote vote;

    @Expose @SerializedName("comment_count")
    private Integer commentCount;

    @Expose @SerializedName("author_avatar_med")
    private String authorAvatarMed;

    @Expose @SerializedName("preview")
    private String preview;

    @Expose @SerializedName("type")
    private String type;

    @Expose @SerializedName("can_vote")
    private Boolean canVote;

    @Expose @SerializedName("status")
    private String status;

    @Expose @SerializedName("category_name")
    private String categoryName;

    @Expose @SerializedName("description")
    private String description;

    @Expose @SerializedName("tags")
    private String tags;

    @Expose @SerializedName("source_url")
    private String sourceUrl;

    @Expose @SerializedName("violation_url")
    private String violationUrl;

    @Expose @SerializedName("date")
    private DateTime date;

    @Expose @SerializedName("author_avatar_big")
    private String authorAvatarBig;

    @Expose @SerializedName("plus18")
    private Boolean plus18;

    @Expose @SerializedName("user_lists")
    private List<Object> userLists = new ArrayList<>();

    @Expose @SerializedName("info")
    private Object info;

    @Expose @SerializedName("author_sex")
    private String authorSex;

    @Expose @SerializedName("user_observe")
    private Boolean userObserve;

    @Expose @SerializedName("url")
    private String url;

    @Expose @SerializedName("related_count")
    private Integer relatedCount;

    @Expose @SerializedName("author")
    private String author;

    @Expose @SerializedName("author_avatar")
    private String authorAvatar;

    @Expose @SerializedName("is_hot")
    private Boolean isHot;

}
