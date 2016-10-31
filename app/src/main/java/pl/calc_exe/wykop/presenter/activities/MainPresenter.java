package pl.calc_exe.wykop.presenter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.adapters.NavigationViewAdapter;
import pl.calc_exe.wykop.adapters.ToolbarSpinnerAdapter;
import pl.calc_exe.wykop.events.MainMenuEvent;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.presenter.extras.IPresenter;
import pl.calc_exe.wykop.view.activities.MainActivity;
import pl.calc_exe.wykop.view.fragments.ViewPagerFragment;
import pl.calc_exe.wykop.view.fragments.WebViewFragment;

/**
 * MainPresenter controls MainActivity.
 */
public class MainPresenter implements IPresenter<MainActivity> {

    private Preferences preferences;

    private MainActivity view;
    private NavigationViewAdapter adapter;

    @Inject
    public MainPresenter(Preferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void onTakeView(MainActivity view) {
        this.view = view;

        adapter = new NavigationViewAdapter(view, preferences);
        view.setNavigationAdapter(adapter);
        view.setupActionBar();
        setupProfile();
        onNavigationClick(Pages.INDEX);
    }

    /**
     * Handles clicks in main menu inside toolbar.
     */
    public boolean onMenuClick(int itemId) {
        switch (itemId) {
            case android.R.id.home:
                view.showDrawerLayout(true);
                break;
            default:
                EventBus.getDefault().post(new MainMenuEvent(itemId));
        }
        return true;
    }

    /**
     * Handles clicks inside navigation menu.
     */
    public void onNavigationClick(int position) {
        switch (position) {
            case Pages.INDEX:
                adapter.setSelected(position);
                view.displayFragment(getFragment(position), false);
                break;

            case Pages.STREAM:
                adapter.setSelected(position);
                view.displayFragment(getFragment(position), false);

                break;
            case Pages.SETTINGS:
                adapter.setSelected(position);
                view.displayFragment(getFragment(position), false);
                break;

            case Pages.LOGIN:
                if (preferences.isLogged()) {
                    adapter.setSelected(Pages.INDEX);
                    preferences.logout();
                    view.displayFragment(getFragment(Pages.INDEX), false);
                    position = Pages.INDEX;
                    clearCookie(view);
                } else {
                    adapter.setSelected(Pages.LOGIN);
                    view.displayFragment(getFragment(position), false);
                }
                break;
        }

        setupProfile();
        view.setActivityTitle(Pages.getTitle(position));
        view.setSpinnerVisibility(View.GONE);
        view.showDrawerLayout(false);
    }

    /**
     * Handles requests for change the action bar.
     */
    public void onActionBarChange(int page, int subpage) {
        if (page == Pages.STREAM && subpage == Pages.Stream.HOT) {
            ToolbarSpinnerAdapter adapter = new ToolbarSpinnerAdapter(view, "Gorące", R.array.hot_range);
            view.setSpinnerAdapter(adapter);

            int position;
            switch (preferences.getHotPeriod()) {
                case 6:
                    position = 0;
                    break;
                case 12:
                    position = 1;
                    break;
                case 24:
                    position = 2;
                    break;
                default:
                    position = 0;
            }
            view.setActivityTitle(null);
            view.setSpinnerSelection(position);
            view.setSpinnerVisibility(View.VISIBLE);
        } else {
            view.setActivityTitle(Pages.getTitle(page));
            view.setSpinnerVisibility(View.GONE);
        }
    }

    /**
     * Calls when share event comes.
     */
    public void onShare(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        view.startActivity(intent);
    }

    private void setupProfile() {
        if (preferences.isLogged()) {
            view.setUserLogin(preferences.getProfile().getLogin());
            view.setUserAvatar(preferences.getProfile().getAvatarMed());
        } else {
            view.setUserLogin("");
            view.setUserAvatar("");
        }
    }

    //TODO: returning another fragments according to page number.
    private Fragment getFragment(int page) {

        Fragment fragment;
        Bundle bundle;
        switch (page) {
            case Pages.SETTINGS:
                //TODO: Change fragment when settings view will be created.
                fragment = new Fragment();
                break;

            case Pages.LOGIN:
                //TODO: Configure login page when presenter will be created.
                fragment = new WebViewFragment();
                bundle = new Bundle();
                bundle.putString("uri", Extras.LOGIN_URL);
                bundle.putBoolean("login", true);
                fragment.setArguments(bundle);
                break;

            default:
                fragment = new ViewPagerFragment();

                bundle = new Bundle();
                bundle.putInt(Pages.PAGE, page);
                bundle.putBoolean(Extras.IS_LOGGED, preferences.isLogged());
                fragment.setArguments(bundle);
                break;
        }

        return fragment;
    }

    // TODO: 2016-10-30 Move to separate class.
    private void clearCookie(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager manager = CookieManager.getInstance();
            manager.removeAllCookies(null);
            manager.flush();
        } else {
            CookieSyncManager cookieSync = CookieSyncManager.createInstance(context);
            cookieSync.startSync();
            CookieManager manager = CookieManager.getInstance();
            manager.removeAllCookie();
            manager.removeSessionCookie();
            cookieSync.stopSync();
            cookieSync.sync();
        }
    }
}
