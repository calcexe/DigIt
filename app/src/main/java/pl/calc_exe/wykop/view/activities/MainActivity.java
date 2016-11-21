package pl.calc_exe.wykop.view.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.adapters.NavigationViewAdapter;
import pl.calc_exe.wykop.adapters.ToolbarSpinnerAdapter;
import pl.calc_exe.wykop.di.components.DaggerMainComponent;
import pl.calc_exe.wykop.di.modules.PresentersModule;
import pl.calc_exe.wykop.events.ActionBarEvent;
import pl.calc_exe.wykop.events.MainSpinnerEvent;
import pl.calc_exe.wykop.events.ReplaceFragmentEvent;
import pl.calc_exe.wykop.events.ShareEvent;
import pl.calc_exe.wykop.events.ShowMessage;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.presenter.activities.MainPresenter;
import pl.calc_exe.wykop.view.extras.IView;

/**
 * Main activity which contains main elements: navigation menu, toolbar and fragments container.
 * All new fragments are placed into {@link #fragmentsContainer}.
 * */
public class MainActivity extends AppCompatActivity implements IView, AdapterView.OnItemSelectedListener {

    @BindView(R.id.drawerLayout) DrawerLayout navigationLayout;
    @BindView(R.id.login) TextView navigationLogin;
    @BindView(R.id.avatar) ImageView navigationAvatar;
    @BindView(R.id.navigatonview_list_view) ListView navigationListView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    Spinner toolbarSpinner;
    @BindView(R.id.fragments_container) FrameLayout fragmentsContainer;

    @Inject MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaggerMainComponent.builder().presentersModule(new PresentersModule()).build().inject(this);
//        ((App) getApplication()).component().inject(this);

        ButterKnife.bind(this);

        toolbarSpinner = ButterKnife.findById(toolbar, R.id.toolbar_spinner);
        toolbarSpinner.setOnItemSelectedListener(this);
        EventBus.getDefault().register(this);

        presenter.onTakeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return presenter.onMenuClick(item.getItemId());
    }

    public void setupActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setNavigationAdapter(NavigationViewAdapter adapter) {
        navigationListView.setAdapter(adapter);
    }

    public void displayFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            fragmentManager
                    .replace(fragmentsContainer.getId(), fragment)
                    .addToBackStack(null);
        } else {
            fragmentManager.replace(fragmentsContainer.getId(), fragment);
        }

        fragmentManager.commit();
    }

    public void setActivityTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null)
            return;

        if (isEmpty(title)){
            actionBar.setDisplayShowTitleEnabled(false);
        } else{
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(title);
        }
    }

    public void setSpinnerAdapter(ToolbarSpinnerAdapter adapter) {
        if (adapter != null)
            toolbarSpinner.setAdapter(adapter);
    }

    public void setSpinnerSelection(int position){
        toolbarSpinner.setSelection(position);
    }

    /**
     * Setting user login inside navigation layout.
     * */
    public void setUserLogin(String name){
        if (isEmpty(name)){
            navigationLogin.setText("");
        } else{
            String format = String.format(getResources().getString(R.string.author_placeholder), name);
            navigationLogin.setText(format);
        }
    }

    /**
     * Setting user avatar inside navigation layout.
     * */
    public void setUserAvatar(String url){
        if (isEmpty(url)){
            navigationAvatar.setImageDrawable(null);
        }else{
            Picasso.with(this).load(url).into(navigationAvatar);
        }
    }

    public void showDrawerLayout(boolean show){
        if (show){
            navigationLayout.openDrawer(GravityCompat.START);
        }else {
            navigationLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void setSpinnerVisibility(int visibility){
        toolbarSpinner.setVisibility(visibility);
    }

    private boolean isEmpty(String value){
        return value == null || value.trim().isEmpty();
    }

    @Subscribe
    public void actionBarEvent(ActionBarEvent event) {
        presenter.onActionBarChange(event.getPage(), event.getPageType());
    }

    @Subscribe
    public void replaceFragment(ReplaceFragmentEvent event) {
        displayFragment(event.getFragment(), true);

        if (event.getPage() != Pages.NONE)
            presenter.onNavigationClick(event.getPage());
    }

    @Subscribe
    public void showMessage(ShowMessage event) {
        Snackbar.make(fragmentsContainer, event.getMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Subscribe
    public void shareLink(ShareEvent event) {
        presenter.onShare(event.getUrl());
    }

    /**
     * Listener for {@link #toolbarSpinner}.
     * */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        EventBus.getDefault().post(new MainSpinnerEvent(position));
    }

    /**
     * Listener for {@link #toolbarSpinner}. {@link #onNothingSelected(AdapterView)} is not used, overriding is required.
     * */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnItemClick(R.id.navigatonview_list_view)
    public void navigationViewClick(int position) {
        presenter.onNavigationClick(position);
    }

}