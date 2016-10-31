package pl.calc_exe.wykop.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.domain.Link;
import pl.calc_exe.wykop.model.domain.UserVote;
import pl.calc_exe.wykop.model.rest.extras.ApisignInterceptor;
import pl.calc_exe.wykop.model.rest.adapters.DateTimeAdapter;
import pl.calc_exe.wykop.model.rest.adapters.ListEntryAdapter;
import pl.calc_exe.wykop.model.rest.adapters.ListLinkAdapter;
import pl.calc_exe.wykop.model.rest.adapters.UserVoteAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {

        return new OkHttpClient.Builder()
                .addInterceptor(new ApisignInterceptor())
                .build();

    }

    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(new TypeToken<List<Link>>(){}.getType(), new ListLinkAdapter());
        builder.registerTypeAdapter(DateTime.class, new DateTimeAdapter());
        builder.registerTypeAdapter(UserVote.class, new UserVoteAdapter());

        builder.registerTypeAdapter(new TypeToken<List<Entry>>(){}.getType(), new ListEntryAdapter());

        return builder.create();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient client, Gson gson) {


        return new Retrofit.Builder()
                .baseUrl(Extras.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client).build();
    }
}
