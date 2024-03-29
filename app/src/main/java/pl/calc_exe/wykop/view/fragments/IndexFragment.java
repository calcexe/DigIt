package pl.calc_exe.wykop.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.adapters.IndexListAdapter;
import pl.calc_exe.wykop.di.components.DaggerMainComponent;
import pl.calc_exe.wykop.di.modules.NetworkModule;
import pl.calc_exe.wykop.di.modules.PresentersModule;
import pl.calc_exe.wykop.events.LoadEvent;
import pl.calc_exe.wykop.events.MainMenuEvent;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.presenter.fragments.IndexPresenter;
import pl.calc_exe.wykop.view.extras.IView;
import retrofit2.Retrofit;


public class IndexFragment extends Fragment implements IView {

    @Inject Retrofit retrofit;
    @Inject IndexPresenter presenter;

    private int pageType;

    private final int LOAD_THRESHOLD = 2;

    @BindView(R.id.main_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;

    public static IndexFragment getInstance(int position) {
        IndexFragment fragment = new IndexFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Pages.PAGE, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainComponent.builder()
                .networkModule(new NetworkModule())
                .presentersModule(new PresentersModule())
                .build().inject(this);

        getBundle();
        presenter.setPageType(pageType);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        ButterKnife.bind(this, view);
        presenter.onTakeView(this);


        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (pageType == Pages.Stream.FAVORITE) {
            refreshLayout.setEnabled(false);
            presenter.disableLoading();
        } else {
            refreshLayout.setOnRefreshListener(() -> {
                presenter.refresh();
                layoutManager.scrollToPosition(0);
            });

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if ((last >= recyclerView.getLayoutManager().getItemCount() - LOAD_THRESHOLD)) {
                        presenter.loadMore();
                    }
                }
            });
        }

        if (getUserVisibleHint())
            presenter.load();

//        if (startPage)
//            presenter.load();

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().unregister(this);
        } else{
            if (!EventBus.getDefault().isRegistered(this))
                EventBus.getDefault().register(this);
        }
    }

    /**
     * Handles events from menu in MainActivity toolbar.
     */
    @Subscribe
    public void onMainMenuEvent(MainMenuEvent event) {
        switch (event.getItem()) {
            case R.id.menu_refresh:
                layoutManager.scrollToPosition(0);
                presenter.refresh();
                break;
            case R.id.menu_to_up:
                layoutManager.scrollToPosition(0);
                break;
        }
    }

    /**
     * Event to load elements for first time only after selection this page in ViewPage.
     */
//    @Subscribe
//    public void onLoadEvent(LoadEvent event) {
//        if (event.getPage() == pageType) {
//            presenter.load();
//        }
//    }

    public void setAdapter(IndexListAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void getBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt(Pages.PAGE, Pages.Stream.INDEX);
        }
    }

    public void scroll(int position) {
        layoutManager.scrollToPosition(position);
    }

    public void setRefreshing(boolean loading) {
        refreshLayout.setRefreshing(loading);
    }
}
