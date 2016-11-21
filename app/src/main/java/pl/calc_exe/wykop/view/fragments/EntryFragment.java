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
import pl.calc_exe.wykop.adapters.EntryCommentsAdapter;
import pl.calc_exe.wykop.di.components.DaggerMainComponent;
import pl.calc_exe.wykop.di.modules.PresentersModule;
import pl.calc_exe.wykop.events.MainMenuEvent;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.presenter.fragments.EntryPresenter;
import pl.calc_exe.wykop.view.extras.IView;

/**
 * Created by Mateusz on 2016-11-01.
 */

public class EntryFragment extends Fragment implements IView {

    @Inject EntryPresenter presenter;

    @BindView(R.id.entry_comments) RecyclerView mEntryComments;
    @BindView(R.id.refresh_layout) SwipeRefreshLayout mRefreshLayout;

    private LinearLayoutManager mLayoutManager;

    public static EntryFragment getInstance(int entryId){
        EntryFragment entryFragment = new EntryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Extras.ENTRY_ID, entryId);
        entryFragment.setArguments(bundle);
        return entryFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMainComponent.builder().presentersModule(new PresentersModule()).build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry, container, false);
        ButterKnife.bind(this, view);

        presenter.onTakeView(this);
        mLayoutManager = new LinearLayoutManager(getContext());
        mEntryComments.setLayoutManager(mLayoutManager);

        int entryId = getArguments().getInt(Extras.ENTRY_ID);
        presenter.getEntry(entryId, false);

        mRefreshLayout.setOnRefreshListener(() -> presenter.getEntry(entryId, true));

        return view;
    }

    public void setAdapter(EntryCommentsAdapter adapter){
        mEntryComments.setAdapter(adapter);
    }

    public void setRefreshing(boolean refreshing){
        mRefreshLayout.setRefreshing(refreshing);
    }

    @Subscribe
    public void mainMenuEvent(MainMenuEvent event){
        presenter.onMainMenuClick(event.getItem());
    }

    public void scrollToPosition(int position) {
        mLayoutManager.scrollToPosition(position);
    }
}
