package pl.calc_exe.wykop.di.modules;

import dagger.Module;
import dagger.Provides;
import pl.calc_exe.wykop.di.Pref;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.presenter.activities.MainPresenter;
import pl.calc_exe.wykop.presenter.fragments.EntryPresenter;
import pl.calc_exe.wykop.presenter.fragments.IndexPresenter;
import pl.calc_exe.wykop.presenter.fragments.StreamPresenter;
import pl.calc_exe.wykop.presenter.fragments.WebViewPresenter;
import retrofit2.Retrofit;

@Module
public class PresentersModule {

    @Provides
    MainPresenter provideMainPresenter(Preferences preferences) {
        return new MainPresenter(preferences);
    }

    @Provides
    IndexPresenter provideIndexPresenter(Retrofit retrofit, Preferences preferences){
        return new IndexPresenter(retrofit, preferences);
    }

    @Provides
    StreamPresenter provideStreamPresenter(Retrofit retrofit, Preferences preferences){
        return new StreamPresenter(retrofit, preferences);
    }

    @Provides
    EntryPresenter provideEntryPresenter(Retrofit retrofit, Preferences preferences){
        return new EntryPresenter(retrofit, preferences);
    }

    @Provides
    WebViewPresenter provideWebViewPresenter(Retrofit retrofit, Preferences preferences){
        return new WebViewPresenter(retrofit, preferences);
    }
}