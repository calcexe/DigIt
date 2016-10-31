package pl.calc_exe.wykop.extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import pl.calc_exe.wykop.model.domain.BuryReason;
import pl.calc_exe.wykop.model.domain.Profile;

/**
 * Created by Mateusz on 2016-08-23.
 */
public class Preferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    private Profile profile;
    private boolean isLogged = false;

    private boolean plus18 = true; //false == allow, true = forbid
    private int hotPeriod = 6;
    private List<String> buryReasonNames;
    private List<Integer> buryReasonId;

    public static final String PROFILE = "profile";
    public static final String LOGGED = "logged";
    public static final String PLUS18 = "plus18";
    public static final String HOT_PERIOD = "hotperiod";
    public static final String ACCOUNT_KEY = "accountkey";

    private static final String PREF_FILE = "get.pref";
    private BuryReasons buryReasons = new BuryReasons();
    private String accountKey;

    public Preferences(Context context) {
        this.gson = new Gson();

        preferences = context.getSharedPreferences(PREF_FILE, Context.MODE_APPEND);
        editor = preferences.edit();

        isLogged = preferences.getBoolean(LOGGED, false);
        plus18 = preferences.getBoolean(PLUS18, true);
        accountKey = preferences.getString(ACCOUNT_KEY, "");
        hotPeriod = preferences.getInt(HOT_PERIOD, 6);
        if (!isLogged) {
            profile = null;
        } else {
            profile = gson.fromJson(preferences.getString(PROFILE, null), Profile.class);
        }
    }

    public void login(Profile profile) {
        isLogged = true;
        this.profile = profile;

        editor.putBoolean(LOGGED, isLogged)
                .putString(PROFILE, gson.toJson(profile, Profile.class))
                .commit();
    }

    public void logout() {
        isLogged = false;
        accountKey = "";
        profile = null;

        editor.putBoolean(LOGGED, isLogged)
                .putString(PROFILE, gson.toJson(profile, Profile.class))
                .putString(ACCOUNT_KEY, accountKey)
                .putBoolean(PLUS18, true)
                .putInt(HOT_PERIOD, 6)
                .commit();

    }

    public Profile getProfile() {
        return profile;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public String getUserkey() {
        return profile.getUserkey();
    }

    public boolean isPlus18() {
        return plus18;
    }

    public void setPlus18(boolean plus18) {
        this.plus18 = plus18;

        editor.putBoolean(PLUS18, plus18)
                .commit();
    }

    public void setBuryReasons(List<BuryReason> reasons) {
        buryReasons.setReasons(reasons);
    }

    public BuryReasons getBuryReasons() {
        return buryReasons;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
        editor.putString(ACCOUNT_KEY, accountKey).commit();
    }

    public String getAccountKey() {
        return accountKey;
    }

    public int getHotPeriod() {
        return hotPeriod;
    }

    public void setHotPeriod(int hotPeriod) {
        this.hotPeriod = hotPeriod;
        editor.putInt(HOT_PERIOD, hotPeriod)
                .commit();
    }

    public class BuryReasons {
        private List<String> names = new ArrayList<>();
        private List<Integer> ids = new ArrayList<>();

        public BuryReasons(List<BuryReason> reasons) {
            setReasons(reasons);
        }

        public BuryReasons() {
        }

        public void setReasons(List<BuryReason> reasons) {
            for (BuryReason reason : reasons) {
                names.add(reason.getName());
                ids.add(reason.getId());
            }
        }

        public CharSequence[] getNames() {
            CharSequence[] n = new String[names.size()];
            for (int i = 0; i < names.size(); i++)
                n[i] = names.get(i);
            return n;
        }

        public List<Integer> getIds() {
            return ids;
        }
    }
}
