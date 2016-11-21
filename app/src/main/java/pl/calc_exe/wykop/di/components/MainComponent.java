package pl.calc_exe.wykop.di.components;

import javax.inject.Singleton;

import dagger.Component;
import pl.calc_exe.wykop.adapters.EntryCommentsAdapter;
import pl.calc_exe.wykop.di.App;
import pl.calc_exe.wykop.di.modules.NetworkModule;
import pl.calc_exe.wykop.di.modules.PreferencesModule;
import pl.calc_exe.wykop.di.modules.PresentersModule;
import pl.calc_exe.wykop.model.rest.extras.ListReceiver;
import pl.calc_exe.wykop.model.rest.extras.Receiver;
import pl.calc_exe.wykop.view.fragments.EntryFragment;
import pl.calc_exe.wykop.view.activities.MainActivity;
import pl.calc_exe.wykop.view.fragments.IndexFragment;
import pl.calc_exe.wykop.view.fragments.StreamFragment;
import pl.calc_exe.wykop.view.fragments.WebViewFragment;

@SuppressWarnings("WeakerAccess")
@Component(modules = {PresentersModule.class, NetworkModule.class, PreferencesModule.class})
@Singleton
public interface MainComponent {

    void inject(ListReceiver.Injector injector);
    void inject(Receiver.Injector injector);
    void inject(App app);
    void inject(MainActivity activity);
    void inject(IndexFragment fragment);
    void inject(StreamFragment fragment);
    void inject(WebViewFragment fragment);
    void inject(EntryFragment fragment);
    void inject(EntryCommentsAdapter adapter);

}
