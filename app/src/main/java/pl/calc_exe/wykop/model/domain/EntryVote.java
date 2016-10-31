package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class EntryVote {

    @Expose @SerializedName("error")
    private Error error = new Error();

    @Expose @SerializedName("vote")
    private int vote;

}
