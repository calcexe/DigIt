package pl.calc_exe.wykop.presenter.fragments;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.adapters.EntryCommentsAdapter;
import pl.calc_exe.wykop.events.ShowMessage;
import pl.calc_exe.wykop.extras.EntryUtils;
import pl.calc_exe.wykop.extras.ErrorManager;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.model.domain.Entry;
import pl.calc_exe.wykop.model.domain.EntryVote;
import pl.calc_exe.wykop.model.domain.extras.VoteItem;
import pl.calc_exe.wykop.model.rest.services.EntriesService;
import pl.calc_exe.wykop.presenter.extras.IPresenter;
import pl.calc_exe.wykop.view.activities.MainActivity;
import pl.calc_exe.wykop.view.fragments.EntryFragment;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EntryPresenter implements IPresenter<EntryFragment> {

    private Preferences mPreferences;
    private Retrofit mRetrofit;
    private EntryFragment mView;
    private Entry mEntry;
    private EntryCommentsAdapter mAdapter;
    private boolean mVoting;

    @Inject
    public EntryPresenter(Retrofit retrofit, Preferences preferences) {
        mPreferences = preferences;
        mRetrofit = retrofit;
    }

    @Override
    public void onTakeView(EntryFragment view) {
        mView = view;
        if (view.getActivity() instanceof MainActivity) {
            ((MainActivity) view.getActivity()).setSpinnerVisibility(View.GONE);
            ((MainActivity) view.getActivity()).setActivityTitle("Mikroblog");
        }
    }

    public void getEntry(int entryId, boolean overrideIfExists) {

        if (!overrideIfExists && mEntry != null) {
            mView.setAdapter(mAdapter);
            return;
        }

        mView.setRefreshing(true);
        EntriesService entriesService = mRetrofit.create(EntriesService.class);
        Observable<Entry> observable;
        if (!mPreferences.isLogged()) {
            observable = entriesService.index(Extras.APP_KEY, entryId);
        } else {
            observable = entriesService.index(Extras.APP_KEY, mPreferences.getUserkey(), entryId);
        }
        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EntrySubscriber<Entry>() {
                    @Override
                    public void onNext(Entry e) {
                        mEntry = e;
                        mAdapter = new EntryCommentsAdapter(mView.getContext(), EntryPresenter.this, mEntry);
                        mView.setAdapter(mAdapter);
                    }
                });
    }

    public void upVote(int position) {

        if (!canVote(position, 1))
            return;

        mVoting = true;
        EntriesService entriesService = mRetrofit.create(EntriesService.class);
        VoteItem item;
        Observable<EntryVote> observable;

        if (position == 0) {
            item = mEntry;
            observable = entriesService.voteEntry(Extras.APP_KEY, mPreferences.getUserkey(), mEntry.getId());
        } else {
            item = mEntry.getComments().get(position - 1);
            observable = entriesService.voteComment(Extras.APP_KEY, mPreferences.getUserkey(), mEntry.getId(), mEntry.getComments().get(position - 1).getId());
        }

        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EntrySubscriber<EntryVote>() {
                    @Override
                    public void onNext(EntryVote entryVote) {
                        if (entryVote.getError().getCode() == 0) {
                            item.setUserVote(1);
                            item.setVoteCount(entryVote.getVote());
                            mAdapter.notifyItemChanged(position);
                        } else {
                            EventBus.getDefault().post(new ShowMessage(entryVote.getError().toString()));
                        }
                    }
                });
    }

    public void downVote(int position) {

        if (!canVote(position, 0))
            return;
        mVoting = true;

        EntriesService entriesService = mRetrofit.create(EntriesService.class);
        VoteItem item;
        Observable<EntryVote> observable;

        if (position == 0) {
            item = mEntry;
            observable = entriesService.unVoteEntry(Extras.APP_KEY, mPreferences.getUserkey(), mEntry.getId());

        } else {
            item = mEntry.getComments().get(position - 1);
            observable = entriesService.unVoteComment(Extras.APP_KEY, mPreferences.getUserkey(), mEntry.getId(), mEntry.getComments().get(position - 1).getId());
        }

        observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new EntrySubscriber<EntryVote>() {
                    @Override
                    public void onNext(EntryVote entryVote) {
                        if (entryVote.getError().getCode() == 0) {
                            item.setUserVote(0);
                            item.setVoteCount(entryVote.getVote());
                            mAdapter.notifyItemChanged(position);
                        } else {
                            EventBus.getDefault().post(new ShowMessage(entryVote.getError().toString()));
                        }
                    }
                });
    }

    private boolean canVote(int position, int vote) {

        int userVote = position == 0 ? mEntry.getUserVote() : mEntry.getComments().get(position - 1).getUserVote();

        return mPreferences.isLogged() && !mVoting && userVote != vote;

    }


    public void showUser(int position) {
        String user = position == 0 ? mEntry.getAuthor() : mEntry.getComments().get(position - 1).getAuthor();
        EntryUtils.showUser(user);
    }

    public void onMainMenuClick(int item) {
        switch (item) {
            case R.id.menu_refresh:
                getEntry(mEntry.getId(), true);
                break;
            case R.id.menu_to_up:
                mView.scrollToPosition(0);
                break;
        }
    }

    private abstract class EntrySubscriber<T> extends Subscriber<T> {

        @Override
        public void onCompleted() {
            mVoting = false;
            mView.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            EventBus.getDefault().post(new ShowMessage(ErrorManager.throwableToError(e).toString()));
        }
    }
}
