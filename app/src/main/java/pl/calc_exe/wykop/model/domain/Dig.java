package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class Dig {

    @SerializedName("author_group")
    @Expose
    private int authorGroup;
    @SerializedName("author_sex")
    @Expose
    private String authorSex;
    @SerializedName("author_avatar_lo")
    @Expose
    private String authorAvatarLo;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("author_avatar")
    @Expose
    private String authorAvatar;
    @SerializedName("author_avatar_big")
    @Expose
    private String authorAvatarBig;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("author_avatar_med")
    @Expose
    private String authorAvatarMed;

}
