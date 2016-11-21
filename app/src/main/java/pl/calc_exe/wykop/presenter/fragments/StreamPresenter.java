package pl.calc_exe.wykop.presenter.fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import pl.calc_exe.wykop.adapters.StreamListAdapter;
import pl.calc_exe.wykop.events.ReplaceFragmentEvent;
import pl.calc_exe.wykop.events.ShowMessage;
import pl.calc_exe.wykop.extras.EntryUtils;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.model.rest.extras.ListReceiver;
import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.rest.services.EntriesService;
import pl.calc_exe.wykop.model.rest.services.FavoritesService;
import pl.calc_exe.wykop.model.rest.services.StreamService;
import pl.calc_exe.wykop.presenter.extras.IPresenter;
import pl.calc_exe.wykop.view.fragments.EntryFragment;
import pl.calc_exe.wykop.view.fragments.StreamFragment;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Universal implementation of presenter which controls StreamService (mikroblog).
 */
public class StreamPresenter implements IPresenter<StreamFragment> {

    private StreamService mStreamService;
    private Retrofit mRetrofit;
    private Preferences mPreferences;

    //Lead which page this presenter controls.
    private int mPageType;

    //Iterator for next pages to mLoading.
    private int mCurrentPage = 1;

    //If mLoading or mVoting is in progress this variables are true.
    private boolean mLoading = false;
    private boolean mVoting = false;

    private List<Entry> mEntries;

    //Contains elements id. It's used for not duplicate elements.
    private HashSet<Integer> mIds = new HashSet<>();

    private StreamFragment mView;
    private StreamListAdapter mAdapter;


    public StreamPresenter(Retrofit retrofit, Preferences preferences) {
        mRetrofit = retrofit;
        mPreferences = preferences;
        mStreamService = retrofit.create(StreamService.class);
        mEntries = new ArrayList<>();
    }

    @Override
    public void onTakeView(StreamFragment view) {
        mView = view;
        setupAdapter();
    }

    /**
     * To get new links at the top of list.
     */
    public void refresh() {
        getList(1, true);
    }

    /**
     * To load mEntries first time when mView is opened (not when mView in ViewPager is created).
     */
    public void load() {
        if (mEntries.size() == 0) {
            getList(1, false);
        }
    }

    /**
     * To load next page at the bottom of list
     */
    public void loadMore() {
        getList(mCurrentPage, false);
    }

    /**
     * Getting mEntries in specific page and appending them at the bottom / top of list.
     *
     * @param page        page to get
     * @param appendOnTop to append mEntries at the bottom / top of list
     */
    private void getList(int page, boolean appendOnTop) {
        if (mLoading)
            return;

        mLoading = true;
        mView.setRefreshing(true);

        ListReceiver<Entry> receiver = new ListReceiver<>(() -> getObservable(page), e -> {
            mLoading = false;
            mView.setRefreshing(false);

            if (mPageType == Pages.Stream.HOT && e.size() == 0) {
                mAdapter.setScrollEnd(true);
                return;
            }

            if (page == 1 && mEntries.size() > 0) {
                int size = mEntries.size();
                mEntries.clear();
                mIds.clear();
                mAdapter.notifyItemRangeRemoved(0, size);
            }

            removeRepetition(e);

            if (appendOnTop) {
                mEntries.addAll(0, e);
                if (mEntries.size() == 0) {
                    mAdapter.notifyItemRangeInserted(0, e.size());
                } else {
                    mAdapter.notifyDataSetChanged();
                    mView.scroll(0);
                }

            } else {
                int size = mEntries.size();
                mEntries.addAll(e);

                mAdapter.notifyItemRangeInserted(size, e.size());
                mCurrentPage++;
            }
        });
        receiver.receive();

    }

    /**
     * Setup mAdapter of the RecyclerView in the mView.
     */
    private void setupAdapter() {
        if (mView == null)
            return;
        mAdapter = new StreamListAdapter(mView.getContext(), this);
        mAdapter.onTakeList(mEntries);
        mView.setAdapter(mAdapter);
    }

    /**
     * Getting observable for {@link StreamPresenter#mPageType}
     *
     * @param page page to get
     * @return Observable assigned to {@link StreamPresenter#mPageType}
     */
    private Observable<List<Entry>> getObservable(int page) {

        switch (mPageType) {
            case Pages.Stream.INDEX:
                return mPreferences.isLogged() ?
                        mStreamService.index(Extras.APP_KEY, mPreferences.getUserkey(), page) :
                        mStreamService.index(Extras.APP_KEY, page);

            case Pages.Stream.HOT:
                return mPreferences.isLogged() ?
                        mStreamService.hot(Extras.APP_KEY, mPreferences.getUserkey(), mPreferences.getHotPeriod(), page) :
                        mStreamService.hot(Extras.APP_KEY, mPreferences.getHotPeriod(), page);

            case Pages.Stream.FAVORITE:
                if (!mPreferences.isLogged())
                    return null;
                FavoritesService service = mRetrofit.create(FavoritesService.class);
                return service.entries(Extras.APP_KEY, mPreferences.getUserkey());

            default:
                return mStreamService.index(Extras.APP_KEY, page);
        }
    }

    public void disableLoading() {
        mAdapter.setScrollEnd(true);
    }

    /**
     * Removing repetitions in lists.
     *
     * @param list list to compare
     */
    private void removeRepetition(List<Entry> list) {
        for (Iterator<Entry> iterator = list.iterator(); iterator.hasNext(); ) {
            Entry item = iterator.next();
            if (mIds.contains(item.getId()))
                iterator.remove();
            else
                mIds.add(item.getId());
        }
    }

    /**
     * Method is called if {@link pl.calc_exe.wykop.events.MainSpinnerEvent} is called
     */
    public void onChangePeriod(int position) {

        if (mPageType == Pages.Stream.HOT) {
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

            if (period == mPreferences.getHotPeriod())
                return;

            mPreferences.setHotPeriod(period);
            mCurrentPage = 1;
            getList(mCurrentPage, true);
        }
    }

    public void setPageType(int pageType) {
        mPageType = pageType;
    }

    //TODO: Change implementation.
    public void attachmentClick(String type, String source) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(source));
        mView.startActivity(i);
    }

    public void showUser(String author) {
        EntryUtils.showUser(author);
    }

    public void showEntry(Entry entry) {
        Fragment fragment = EntryFragment.getInstance(entry.getId());
        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment));
    }

    public void downVote(Entry entry) {
        if (!mPreferences.isLogged() || entry.getUserVote() == 0 || mVoting)
            return;

        mVoting = true;
        EntriesService entriesService = mRetrofit.create(EntriesService.class);
        entriesService
                .unVoteEntry(Extras.APP_KEY, mPreferences.getUserkey(), entry.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count.getError().getCode() == 0) {
                        int index = mEntries.indexOf(entry);
                        mEntries.get(index).setVoteCount(count.getVote());
                        mEntries.get(index).setUserVote(0);
                        mAdapter.notifyItemChanged(index);
                    } else {
                        EventBus.getDefault().post(new ShowMessage(count.getError().toString()));
                    }
                }, t->{}, () -> mVoting = false);

    }

    public void onUpVote(Entry entry) {
        if (!mPreferences.isLogged() || entry.getUserVote() == 1 || mVoting)
            return;

        mVoting = true;
        EntriesService entriesService = mRetrofit.create(EntriesService.class);
        entriesService
                .voteEntry(Extras.APP_KEY, mPreferences.getUserkey(), entry.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(count -> {
                    if (count.getError().getCode() == 0) {
                        int index = mEntries.indexOf(entry);
                        mEntries.get(index).setVoteCount(count.getVote());
                        mEntries.get(index).setUserVote(1);
                        mAdapter.notifyItemChanged(index);
                    } else {
                        EventBus.getDefault().post(new ShowMessage(count.getError().toString()));
                    }
                }, t->{}, () -> mVoting = false);
    }
}