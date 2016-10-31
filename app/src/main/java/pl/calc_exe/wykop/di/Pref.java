package pl.calc_exe.wykop.di;

import android.content.Context;

import pl.calc_exe.wykop.extras.Preferences;

/**
 * Small singleton, wrapper and provider of Preferences.
 */
public class Pref {

    private static Pref ourInstance = new Pref();
    private static Preferences preferences;

    public static Pref getInstance() {
        return ourInstance;
    }

    private Pref() {
    }

    static void init(Context context){
        preferences = new Preferences(context);
    }

    public Preferences get(){
        if (preferences == null)
            throw new IllegalArgumentException("Preferences are not init!");
        return preferences;
    }
}
