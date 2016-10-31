package pl.calc_exe.wykop.model.rest.services;

import java.util.List;

import pl.calc_exe.wykop.model.domain.Entry;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Mateusz on 2016-10-14.
 */

public interface FavoritesService {
    @POST("favorites/entries/appkey/{appkey}/userkey/{userkey}")
    Observable<List<Entry>> entries(@Path("appkey") String appkey, @Path("userkey") String userkey);
}
