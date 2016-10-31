package pl.calc_exe.wykop.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

public class BuryReason {

    @Getter @Expose @SerializedName("id")
    private int id;

    @Getter @Expose @SerializedName("name")
    private String name;

}
