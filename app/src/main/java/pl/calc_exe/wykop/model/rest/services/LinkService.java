package pl.calc_exe.wykop.model.rest.services;

import java.util.List;

import pl.calc_exe.wykop.model.domain.BuryReason;
import pl.calc_exe.wykop.model.domain.Link;
import pl.calc_exe.wykop.model.domain.LinkVote;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mateusz on 2016-09-04.
 */
public interface LinkService{

    @POST("link/index/id/{id}")
    public Call<Link> link();

    //TODO: describe method parameters
    @POST("link/dig/{id}/userkey/{userkey}/appkey/{appkey}")
    public Observable<LinkVote> dig(@Path("id") int id,
                              @Path("userkey") String userkey,
                              @Path("appkey") String appkey);

    @POST("link/bury/{id}/{reason}/userkey/{userkey}/appkey/{appkey}")
    public Observable<LinkVote> bury(@Path("id") int id,
                               @Path("reason") int reason,
                               @Path("userkey") String userkey,
                               @Path("appkey") String appkey);


    @POST("link/cancel/{id}/userkey/{userkey}/appkey/{appkey}")
    public Observable<LinkVote> cancel(@Path("id") int id,
                                 @Path("userkey") String userkey,
                                 @Path("appkey") String appkey);

    @POST("link/buryreasons/appkey/{appkey}")
    public Call<List<BuryReason>> buryReasons(@Path("appkey") String appkey);

    @POST("link/buryreasons/appkey/{appkey}")
    public Observable<List<BuryReason>> buryReasonsO(@Path("appkey") String appkey);

    @POST("link/favorite/{id}/appkey/{appkey}/userkey/{userkey}")
    public Observable<Boolean[]> favorite(@Path("id") int id,
                                                 @Path("appkey") String appkey,
                                                 @Path("userkey") String userkey);



}
