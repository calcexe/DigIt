package pl.calc_exe.wykop.presenter.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.greenrobot.eventbus.EventBus;

import pl.calc_exe.wykop.di.Pref;
import pl.calc_exe.wykop.events.ReplaceFragmentEvent;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.model.rest.services.LinkService;
import pl.calc_exe.wykop.model.rest.services.UserService;
import pl.calc_exe.wykop.presenter.extras.IPresenter;
import pl.calc_exe.wykop.view.fragments.ViewPagerFragment;
import pl.calc_exe.wykop.view.fragments.WebViewFragment;
import retrofit2.Retrofit;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

//TODO: Completely change implementation of WebViewPresenter:
//- rename presenter and view class
//- remove boilerplate
// FIXME: 2016-10-30
// STOPSHIP: 2016-10-30
public class WebViewPresenter implements IPresenter<WebViewFragment> {

    private WebViewFragment view;
    private Retrofit retrofit;
    private Preferences preferences;

    public WebViewPresenter(Retrofit retrofit, Preferences preferences) {
        this.retrofit = retrofit;
        this.preferences = preferences;
    }

    @Override
    public void onTakeView(WebViewFragment view) {
        this.view = view;
    }

    public WebViewClient getWebViewClient() {
        return new LoginWebViewClient();
    }

    private class LoginWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {

            Uri uri = Uri.parse(url);
            //To allow only api request.
            if (!uri.getHost().trim().contains(Extras.BASE_HOST)) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri);
                view.startActivity(i);
            }

            if (uri.getPath().contains("ConnectSuccess")) {
                String accountkey = uri.getLastPathSegment();
                Pref.getInstance().get().setAccountKey(accountkey);

                UserService userService = retrofit.create(UserService.class);
                userService.loginObservable(Extras.APP_KEY, accountkey)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(p -> {
                            //TODO: finish activity with code (if error corupts).
                            if (p.getError().getCode() == 0) {
                                preferences.login(p);

                                if (preferences.getBuryReasons().getIds().size() == 0) {
                                    LinkService linkService = retrofit.create(LinkService.class);
                                    linkService.buryReasonsO(Extras.APP_KEY)
                                            .subscribeOn(Schedulers.newThread())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(buryReasons -> {
                                                preferences.setBuryReasons(buryReasons);
                                                loginEnd();
                                            });
                                } else {
                                    loginEnd();
                                }

                            } else {
                                loginEnd();
                            }
                        });
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private void loginEnd() {
        Fragment fragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Pages.PAGE, Pages.INDEX);
        bundle.putBoolean(Extras.IS_LOGGED, preferences.isLogged());
        fragment.setArguments(bundle);
        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment, Pages.INDEX, Pages.Index.PROMOTED));
    }

    public void clearCookie() {
        if (view == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager manager = CookieManager.getInstance();
            manager.removeAllCookies(null);
            manager.flush();
        } else {
            CookieSyncManager cookieSync = CookieSyncManager.createInstance(view.getContext());
            cookieSync.startSync();
            CookieManager manager = CookieManager.getInstance();
            manager.removeAllCookie();
            manager.removeSessionCookie();
            cookieSync.stopSync();
            cookieSync.sync();
        }
    }
}
