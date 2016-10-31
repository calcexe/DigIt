package pl.calc_exe.wykop.model.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public enum UserVote {

    DIG("dig"), BURY("bury"), CANCEL("cancel"), NONE("none");

    @Getter
    private final String value;

}
