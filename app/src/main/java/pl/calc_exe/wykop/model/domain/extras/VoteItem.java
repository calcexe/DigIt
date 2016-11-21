package pl.calc_exe.wykop.model.domain.extras;

public interface VoteItem {
    void setUserVote(int userVote);
    void setVoteCount(int voteCount);
    int getUserVote();
    int getVoteCount();
}
