package pl.calc_exe.wykop.model.rest.services;

import java.util.List;

import pl.calc_exe.wykop.model.domain.Link;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mateusz on 2016-08-28.
 */
public interface LinksService {
    /**
     * @param appkey API
     * @param page   API
     * @param sort   API - day / week / month(główna - najnowsze)
     **/
    @POST("links/promoted/appkey/{appkey}/page/{page}/sort/{sort}/output/clear")
    public Call<List<Link>> promoted(@Path("appkey") String appkey,
                                     @Path("page") int page,
                                     @Path("sort") String sort);

    /**
     * @param appkey API
     * @param page   API
     * @param userkey API
     * @param sort   API - day / week / month(główna - najnowsze)
     **/
    @POST("links/promoted/appkey/{appkey}/userkey/{userkey}/page/{page}/sort/{sort}/output/clear")
    public Call<List<Link>> promoted(@Path("appkey") String appkey,
                                     @Path("userkey") String userkey,
                                     @Path("page") int page,
                                     @Path("sort") String sort);

    @POST("links/promoted/appkey/{appkey}/userkey/{userkey}/page/{page}/output/clear")
    public Call<List<Link>> promoted(@Path("appkey") String appkey,
                                     @Path("userkey") String userkey,
                                     @Path("page") int page);

    @POST("links/promoted/appkey/{appkey}/page/{page}/output/clear")
    public Call<List<Link>> promoted(@Path("appkey") String appkey,
                                     @Path("page") int page);

    @POST("links/promoted/appkey/{appkey}/page/{page}/output/clear")
    public Observable<List<Link>> promotedObservable(@Path("appkey") String appkey,
                                                     @Path("page") int page);

    @POST("links/promoted/appkey/{appkey}/userkey/{userkey}/page/{page}/output/clear")
    public Observable<List<Link>> promotedObservable(@Path("appkey") String appkey,
                                                     @Path("userkey") String userkey,
                                                     @Path("page") int page);

    /**
     * @param appkey API
     * @param page   API
     * @param sort   API - day / week / month(główna - najnowsze)
     **/
    @POST("links/upcoming/appkey/{appkey}/page/{page}/sort/{sort}/output/clear")
    public Call<List<Link>> upcoming(@Path("appkey") String appkey,
                                     @Path("page") int page,
                                     @Path("sort") String sort);
    /**
     * @param appkey API
     * @param page   API
//     * @param sort   API - day / week / month(główna - najnowsze)
     **/
    @POST("links/upcoming/appkey/{appkey}/page/{page}/sort/{sort}/output/clear")
    public Observable<List<Link>> upcomingObservable(@Path("appkey") String appkey,
                                                     @Path("page") int page);

    @POST("links/upcoming/appkey/{appkey}/userkey/{userkey}/page/{page}/sort/{sort}/output/clear")
    public Observable<List<Link>> upcomingObservable(@Path("appkey") String appkey,
                                                     @Path("userkey") String userkey,
                                                     @Path("page") int page);


}
