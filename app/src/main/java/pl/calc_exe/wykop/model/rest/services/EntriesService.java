package pl.calc_exe.wykop.model.rest.services;

import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.domain.EntryVote;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface EntriesService {
    @POST("entries/vote/entry/{entryId}/appkey/{appkey}/userkey/{userkey}")
    Observable<EntryVote> voteEntry(@Path("appkey") String appkey,
                                    @Path("userkey") String userkey,
                                    @Path("entryId") int entryId);

    @POST("entries/vote/comment/{entryId}/{commentId}/appkey/{appkey}/userkey/{userkey}")
    Observable<EntryVote> voteComment(@Path("appkey") String appkey,
                                      @Path("userkey") String userkey,
                                      @Path("entryId") int entryId,
                                      @Path("commentId") int commentId);

    @POST("entries/unvote/entry/{entryId}/appkey/{appkey}/userkey/{userkey}")
    Observable<EntryVote> unVoteEntry(@Path("appkey") String appkey,
                                      @Path("userkey") String userkey,
                                      @Path("entryId") int entryId);

    @POST("entries/unvote/comment/{entryId}/{commentId}/appkey/{appkey}/userkey/{userkey}")
    Observable<EntryVote> unVoteComment(@Path("appkey") String appkey,
                                        @Path("userkey") String userkey,
                                        @Path("entryId") int entryId,
                                        @Path("commentId") int commentId);

    @POST("entries/index/{entryId}/appkey/{appkey}/userkey/{userkey}")
    Observable<Entry> index(@Path("appkey") String appkey,
                            @Path("userkey") String userkey,
                            @Path("entryId") int entryId);

    @POST("entries/index/{entryId}/appkey/{appkey}")
    Observable<Entry> index(@Path("appkey") String appkey,
                            @Path("entryId") int entryId);
}
