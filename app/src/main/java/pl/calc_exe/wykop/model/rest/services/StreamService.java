package pl.calc_exe.wykop.model.rest.services;

import java.util.List;

import pl.calc_exe.wykop.model.domain.Entry;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mateusz on 2016-09-30.
 */

public interface StreamService {
    @POST("stream/index/appkey/{appkey}/userkey/{userkey}/page/{page}")
    Observable<List<Entry>> index(@Path("appkey") String appkey,
                                  @Path("userkey") String userkey,
                                  @Path("page") int page);

    @POST("stream/index/appkey/{appkey}/page/{page}")
    Observable<List<Entry>> index(@Path("appkey") String appkey,
                                  @Path("page") int page);

    @POST("stream/hot/appkey/{appkey}/userkey/{userkey}/page/{page}/period/{period}")
    Observable<List<Entry>> hot(@Path("appkey") String appkey,
                              @Path("userkey") String userkey,
                              @Path("period") int period,
                              @Path("page") int page);

    @POST("stream/hot/appkey/{appkey}/page/{page}/period/{period}")
    Observable<List<Entry>> hot(@Path("appkey") String appkey,
                                @Path("period") int period,
                                @Path("page") int page);

    @Deprecated
    @POST("stream/index/appkey/{appkey}/userkey/{userkey}/page/{page}/period/{period}")
    Observable<List<Entry>> favorite(@Path("appkey") String appkey,
                                @Path("userkey") String userkey,
                                @Path("period") int period,
                                @Path("page") int page);
}
