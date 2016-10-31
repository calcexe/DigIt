package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import pl.calc_exe.wykop.model.domain.extras.IError;

@Getter
public class LinkVote implements IError{

    @Expose @SerializedName("error")
    private Error error = new Error();

    @Expose @SerializedName("vote")
    private int vote;

    @Expose @SerializedName("report_count")
    private int report_count;

    @Expose @SerializedName("success")
    private boolean success;

}
