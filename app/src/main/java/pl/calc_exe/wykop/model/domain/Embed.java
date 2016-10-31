package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class Embed {

    @Expose @SerializedName("url")
    private String url;

    @Expose @SerializedName("source")
    private String source;

    @Expose @SerializedName("preview")
    private String preview;

    @Expose @SerializedName("type")
    private String type;

    @Expose @SerializedName("plus18")
    private Boolean plus18;

}
