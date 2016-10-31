package pl.calc_exe.wykop.model.rest.services;

import java.util.List;

import okhttp3.ResponseBody;
import pl.calc_exe.wykop.model.domain.Link;
import pl.calc_exe.wykop.model.domain.Profile;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mateusz on 2016-08-18.
 */

/**
 * Connect method is little bit different, so I didn't implement it here.
 */
public interface UserService {

    /**
     * * @param appkey API
     * * @param accountkey POST
     **/
    @POST("user/login/appkey/{appkey}")
    Observable<Profile> loginObservable(@Path("appkey") String appkey,
                                        @Header("accountkey") String accountkey);

    @POST("user/login/appkey/{appkey}")
    Observable<ResponseBody> loginObservableResponse(@Path("appkey") String appkey,
                                                     @Header("accountkey") String accountkey);

    /**
     * * @param appkey API
     * * @param accountkey POST
     **/
    @POST("user/login/appkey/{appkey}")
    Call<Profile> loginCall(@Path("appkey") String appkey,
                                  @Header("accountkey") String accountkey);

    /**
     * @param userkey API
     * @param appkey  API
     **/
    @POST("user/favorites/userkey/{userkey}/appkey/{appkey}")
    Observable<List<Link>> favorites(@Path("appkey") String appkey,
                                     @Path("userkey") String userkey);

}
