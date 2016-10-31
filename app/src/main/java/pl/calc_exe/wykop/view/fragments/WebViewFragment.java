package pl.calc_exe.wykop.view.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.di.components.DaggerMainComponent;
import pl.calc_exe.wykop.di.modules.NetworkModule;
import pl.calc_exe.wykop.di.modules.PresentersModule;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.presenter.fragments.WebViewPresenter;
import pl.calc_exe.wykop.view.extras.IView;
import retrofit2.Retrofit;

public class WebViewFragment extends Fragment implements IView {

    @Inject Preferences preferences;
    @Inject Retrofit retrofit;

    @BindView(R.id.loginWebView) WebView webView;

    @Inject WebViewPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, container, false);
        ButterKnife.bind(this, view);

        DaggerMainComponent.builder().networkModule(new NetworkModule()).presentersModule(new PresentersModule()).build().inject(this);

        //presenter = new WebViewPresenter(retrofit);
        presenter.onTakeView(this);


        Uri uri = Uri.parse(getArguments().getString("uri", "http://www.wykop.pl/"));

        boolean login = getArguments().getBoolean("login", false);
        if (login){
            presenter.clearCookie();
            webView.setWebViewClient(presenter.getWebViewClient());
        } else{
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setIndeterminate(true);
            dialog.setTitle("Wczytywanie strony...");
            dialog.setCancelable(false);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.contains("wykop.pl"))
                        return false;

                    else {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        return true;
                    }
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    dialog.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    view.loadUrl("javascript:(function() {$(\"div[id*=bmone2n]\").remove(); document.getElementsByClassName('cookie')[0].style.display = \"none\"; document.getElementsByClassName('clearfix m-reset-position')[0].style.display = \"none\"; document.getElementById('site').style.paddingTop = \"0px\";})();");
                    dialog.hide();
                }
            });
        }

        webView.clearHistory();
        webView.loadUrl(uri.toString());

        return view;
    }

}