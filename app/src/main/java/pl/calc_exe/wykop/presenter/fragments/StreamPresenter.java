package pl.calc_exe.wykop.presenter.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.calc_exe.wykop.adapters.StreamListAdapter;
import pl.calc_exe.wykop.events.ReplaceFragmentEvent;
import pl.calc_exe.wykop.events.ShowMessage;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.model.rest.extras.ListReceiver;
import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.rest.services.EntriesService;
import pl.calc_exe.wykop.model.rest.services.FavoritesService;
import pl.calc_exe.wykop.model.rest.services.StreamService;
import pl.calc_exe.wykop.presenter.extras.IPresenter;
import pl.calc_exe.wykop.view.fragments.StreamFragment;
import pl.calc_exe.wykop.view.fragments.WebViewFragment;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Universal implementation of presenter which controls StreamService (mikroblog).
 */
public class StreamPresenter implements IPresenter<StreamFragment> {

    private StreamService streamService;
    private Retrofit retrofit;
    private Preferences preferences;

    //Lead which page this presenter controls.
    private int pageType;

    //Iterator for next pages to loading.
    private int currentPage = 1;

    //If loading or voting is in progress this variables are true.
    private boolean loading = false;
    private boolean voting = false;

    private List<Entry> entries;

    //Contains elements id. It's used for not duplicate elements.
    private HashSet<Integer> ids = new HashSet<>();

    private StreamFragment view;
    private StreamListAdapter adapter;

    public StreamPresenter(Retrofit retrofit, Preferences preferences) {
        this.retrofit = retrofit;
        this.preferences = preferences;
        this.streamService = retrofit.create(StreamService.class);
        this.entries = new ArrayList<>();
    }

    @Override
    public void onTakeView(StreamFragment view) {
        this.view = view;
        setupAdapter();
    }

    /**
     * To get new links at the top of list.
     */
    public void refresh() {
        getList(1, true);
    }

    /**
     * To load entries first time when view is opened (not when view in ViewPager is created).
     */
    public void load() {
        if (entries.size() == 0) {
            getList(1, false);
        }
    }

    /**
     * To load next page at the bottom of list
     */
    public void loadMore() {
        getList(currentPage, false);
    }

    /**
     * Getting entries in specific page and appending them at the bottom / top of list.
     *
     * @param page        page to get
     * @param appendOnTop to append entries at the bottom / top of list
     */
    private void getList(int page, boolean appendOnTop) {
        if (loading)
            return;

        loading = true;
        view.setRefreshing(true);

        ListReceiver<Entry> receiver = new ListReceiver<>(() -> getObservable(page), e -> {
            loading = false;
            view.setRefreshing(false);

            if (pageType == Pages.Stream.HOT && e.size() == 0) {
                adapter.setScrollEnd(true);
                return;
            }

            if (page == 1 && entries.size() > 0) {
                int size = entries.size();
                entries.clear();
                ids.clear();
                adapter.notifyItemRangeRemoved(0, size);
            }

            removeRepetition(e);

            if (appendOnTop) {
                StreamPresenter.this.entries.addAll(0, e);
                if (entries.size() == 0) {
                    adapter.notifyItemRangeInserted(0, e.size());
                } else {
                    adapter.notifyDataSetChanged();
                    view.scroll(0);
                }

            } else {
                int size = StreamPresenter.this.entries.size();
                StreamPresenter.this.entries.addAll(e);

                adapter.notifyItemRangeInserted(size, e.size());
                currentPage++;
            }
        });
        receiver.receive();

    }

    /**
     * Setup adapter of the RecyclerView in the view.
     */
    private void setupAdapter() {
        if (view == null)
            return;
        adapter = new StreamListAdapter(view.getContext(), this);
        adapter.onTakeList(entries);
        view.setAdapter(adapter);
    }

    /**
     * Getting observable for {@link StreamPresenter#pageType}
     *
     * @param page page to get
     * @return Observable assigned to {@link StreamPresenter#pageType}
     */
    private Observable<List<Entry>> getObservable(int page) {

        switch (pageType) {
            case Pages.Stream.INDEX:
                return preferences.isLogged() ?
                        streamService.index(Extras.APP_KEY, preferences.getUserkey(), page) :
                        streamService.index(Extras.APP_KEY, page);

            case Pages.Stream.HOT:
                return preferences.isLogged() ?
                        streamService.hot(Extras.APP_KEY, preferences.getUserkey(), preferences.getHotPeriod(), page) :
                        streamService.hot(Extras.APP_KEY, preferences.getHotPeriod(), page);

            case Pages.Stream.FAVORITE:
                if (!preferences.isLogged())
                    return null;
                FavoritesService service = retrofit.create(FavoritesService.class);
                return service.entries(Extras.APP_KEY, preferences.getUserkey());

            default:
                return streamService.index(Extras.APP_KEY, page);
        }
    }

    public void disableLoading() {
        adapter.setScrollEnd(true);
    }

    /**
     * Removing repetitions in lists.
     *
     * @param list list to compare
     */
    private void removeRepetition(List<Entry> list) {
        for (Iterator<Entry> iterator = list.iterator(); iterator.hasNext(); ) {
            Entry item = iterator.next();
            if (ids.contains(item.getId()))
                iterator.remove();
            else
                ids.add(item.getId());
        }
    }

    /**
     * Method is called if {@link pl.calc_exe.wykop.events.MainSpinnerEvent} is called
     */
    public void onChangePeriod(int position) {

        if (pageType == Pages.Stream.HOT) {
            int period;
            switch (position) {
                case 0:
                    period = 6;
                    break;
                case 1:
                    period = 12;
                    break;
                case 2:
                    period = 24;
                    break;
                default:
                    period = 6;
            }

            if (period == preferences.getHotPeriod())
                return;

            preferences.setHotPeriod(period);
            currentPage = 1;
            getList(currentPage, true);
        }
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    //TODO: Change implementation.
    public void attachmentClick(String type, String source) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(source));
        view.startActivity(i);
    }

    public void showUser(String author) {
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uri", "http://www.wykop.pl/ludzie/" + author);
        fragment.setArguments(bundle);
        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment));
    }

    public void showEntry(Entry entry) {
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uri", entry.getUrl());
        fragment.setArguments(bundle);
        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment));
    }

    public void downVote(Entry entry) {
        if (!preferences.isLogged() || entry.getUserVote() == 0 || voting)
            return;

        voting = true;
        EntriesService entriesService = retrofit.create(EntriesService.class);
        entriesService
                .unVoteEntry(Extras.APP_KEY, preferences.getUserkey(), entry.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count.getError().getCode() == 0) {
                        int index = entries.indexOf(entry);
                        entries.get(index).setVoteCount(count.getVote());
                        entries.get(index).setUserVote(0);
                        adapter.notifyItemChanged(index);
                    } else {
                        EventBus.getDefault().post(new ShowMessage(count.getError().toString()));
                    }
                }, t->{}, () -> voting = false);

    }

    public void onUpVote(Entry entry) {
        if (!preferences.isLogged() || entry.getUserVote() == 1 || voting)
            return;

        voting = true;
        EntriesService entriesService = retrofit.create(EntriesService.class);
        entriesService
                .voteEntry(Extras.APP_KEY, preferences.getUserkey(), entry.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count.getError().getCode() == 0) {
                        int index = entries.indexOf(entry);
                        entries.get(index).setVoteCount(count.getVote());
                        entries.get(index).setUserVote(1);
                        adapter.notifyItemChanged(index);
                    } else {
                        EventBus.getDefault().post(new ShowMessage(count.getError().toString()));
                    }
                }, t->{}, () -> voting = false);
    }
}