package pl.calc_exe.wykop.di.modules;

import dagger.Module;
import dagger.Provides;
import pl.calc_exe.wykop.di.Pref;
import pl.calc_exe.wykop.extras.Preferences;

@Module
public class PreferencesModule {

    @Provides
    Preferences providePreferences(){
        return Pref.getInstance().get();
    }

}
