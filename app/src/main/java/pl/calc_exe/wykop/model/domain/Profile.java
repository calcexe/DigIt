package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Profile {

    @Expose @SerializedName("error")
    private Error error = new Error();

    @Expose @SerializedName("login")
    private String login;

    @Expose @SerializedName("email")
    private String email;

    @Expose @SerializedName("public_email")
    private String publicEmail;

    @Expose @SerializedName("name")
    private String name;

    @Expose @SerializedName("www")
    private String www;

    @Expose @SerializedName("jabber")
    private String jabber;

    @Expose @SerializedName("gg")
    private String gg;

    @Expose @SerializedName("city")
    private String city;

    @Expose @SerializedName("about")
    private String about;

    @Expose @SerializedName("author_group")
    private Integer authorGroup;

    @Expose @SerializedName("links_added")
    private Integer linksAdded;

    @Expose @SerializedName("links_published")
    private Integer linksPublished;

    @Expose @SerializedName("comments")
    private Integer comments;

    @Expose @SerializedName("rank")
    private Integer rank;

    @Expose @SerializedName("followers")
    private Integer followers;

    @Expose @SerializedName("following")
    private Integer following;

    @Expose @SerializedName("entries")
    private Integer entries;

    @Expose @SerializedName("entries_comments")
    private Integer entriesComments;

    @Expose @SerializedName("diggs")
    private Integer diggs;

    @Expose @SerializedName("buries")
    private Integer buries;

    @Expose @SerializedName("groups")
    private Integer groups;

    @Expose @SerializedName("related_links")
    private Integer relatedLinks;

    @Expose @SerializedName("signup_date")
    private String signupDate;

    @Expose @SerializedName("avatar")
    private String avatar;

    @Expose @SerializedName("avatar_big")
    private String avatarBig;

    @Expose @SerializedName("avatar_med")
    private String avatarMed;

    @Expose @SerializedName("avatar_lo")
    private String avatarLo;

    @Expose @SerializedName("is_observed")
    private Object isObserved;

    @Expose @SerializedName("is_blocked")
    private Object isBlocked;

    @Expose @SerializedName("sex")
    private String sex;

    @Expose @SerializedName("url")
    private String url;

    @Expose @SerializedName("violation_url")
    private Object violationUrl;

    @Expose @SerializedName("userkey")
    private String userkey;

    @Expose @SerializedName("accountkey")
    private String accountkey;

}