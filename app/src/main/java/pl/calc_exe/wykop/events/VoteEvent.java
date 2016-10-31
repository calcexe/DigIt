package pl.calc_exe.wykop.events;

import pl.calc_exe.wykop.model.domain.UserVote;

/**
 * Created by Mateusz on 2016-09-21.
 */

public class VoteEvent {
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public enum Page{PROMOTED, UPCOMING, ENTRY};
    public enum Type{DIG, CANCEL, BURRY};

    private Page page;
    private Type type;
    private UserVote userVote;
    private int id;
    private int reason;

    public VoteEvent(Page page, UserVote userVote, int id) {
        this.page = page;
        this.userVote = userVote;
        this.id = id;
    }

    public Page getPage() {
        return page;
    }

    public Type getType() {
        return type;
    }

    public UserVote getUserVote() {
        return userVote;
    }

    public int getId() {
        return id;
    }
}
