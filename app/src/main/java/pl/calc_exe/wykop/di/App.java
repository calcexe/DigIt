package pl.calc_exe.wykop.di;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import pl.calc_exe.wykop.di.components.DaggerMainComponent;
import pl.calc_exe.wykop.di.modules.NetworkModule;
import pl.calc_exe.wykop.di.modules.PreferencesModule;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.model.rest.services.LinkService;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class App extends Application {

    @Inject Preferences preferences;
    @Inject Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        JodaTimeAndroid.init(this);
        Pref.init(this);

        DaggerMainComponent.builder()
                .networkModule(new NetworkModule())
                .preferencesModule(new PreferencesModule())
                .build().inject(this);

        if (preferences.isLogged()) {
            LinkService linkService = retrofit.create(LinkService.class);
            linkService.buryReasonsO(Extras.APP_KEY)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(buryReasons -> preferences.setBuryReasons(buryReasons));
        }
    }


}
