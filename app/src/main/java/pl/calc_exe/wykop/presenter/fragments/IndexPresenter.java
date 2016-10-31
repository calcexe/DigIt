package pl.calc_exe.wykop.presenter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.adapters.IndexListAdapter;
import pl.calc_exe.wykop.events.ReplaceFragmentEvent;
import pl.calc_exe.wykop.events.ShowMessage;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.extras.Preferences;
import pl.calc_exe.wykop.model.domain.LinkVote;
import pl.calc_exe.wykop.model.domain.UserVote;
import pl.calc_exe.wykop.model.rest.extras.ListReceiver;
import pl.calc_exe.wykop.model.domain.Link;
import pl.calc_exe.wykop.model.rest.services.LinkService;
import pl.calc_exe.wykop.model.rest.services.LinksService;
import pl.calc_exe.wykop.model.rest.services.UserService;
import pl.calc_exe.wykop.presenter.extras.IPresenter;
import pl.calc_exe.wykop.view.fragments.IndexFragment;
import pl.calc_exe.wykop.view.fragments.WebViewFragment;
import retrofit2.Retrofit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Universal implementation of presenter which controls StreamService (mikroblog).
 * */
public class IndexPresenter implements IPresenter<IndexFragment> {

    private LinksService linksService;
    private Retrofit retrofit;
    private Preferences preferences;

    //Lead which page this presenter controls.
    private int pageType = Pages.Index.PROMOTED;

    //Iterator for next pages to loading.
    private int currentPage = 1;
    private boolean loading = false;
    private boolean voting = false;

    private List<Link> links;

    //Contains elements id. It's used for not duplicate elements.
    private HashSet<Integer> ids = new HashSet<>();

    private IndexFragment view;
    private IndexListAdapter adapter;

    @Inject
    public IndexPresenter(Retrofit retrofit, Preferences preferences) {
        this.retrofit = retrofit;
//        this.preferences = Pref.getInstance().get();
        this.preferences = preferences;
        this.linksService = retrofit.create(LinksService.class);
        this.links = new ArrayList<>();
    }

    @Override
    public void onTakeView(IndexFragment view) {
        this.view = view;
        setupAdapter();
    }

    /**
     * To get new links at the top of list.
     * */
    public void refresh() {
        getList(1, true);
    }

    /**
     * To load links first time when view is opened (not when view in ViewPager is created).
     * */
    public void load() {
        if (links.size() == 0) {
            getList(1, false);
        }
    }

    /**
     * To load next page at the bottom of list
     * */
    public void loadMore() {
        getList(currentPage, false);
    }

    /**
     * Getting links in specific page and appending them at the bottom / top of list.
     * @param page page to get
     * @param appendOnTop to append links at the bottom / top of list*/
    private void getList(int page, boolean appendOnTop) {
        if (loading)
            return;

        loading = true;
        view.setRefreshing(true);

        ListReceiver<Link> receiver = new ListReceiver<>(() -> getObservable(page), l -> {
            loading = false;
            view.setRefreshing(false);

            if (pageType == Pages.Stream.HOT && l.size() == 0)
                adapter.setScrollEnd(true);

            if (page == 1 && links.size() > 0) {
                int size = links.size();
                links.clear();
                ids.clear();
                adapter.notifyItemRangeRemoved(0, size);
            }

            removeRepetition(l);

            if (appendOnTop) {
                IndexPresenter.this.links.addAll(0, l);
                if (links.size() == 0) {
                    adapter.notifyItemRangeInserted(0, l.size());
                } else {
                    adapter.notifyDataSetChanged();
                    view.scroll(0);
                }

            } else {
                int size = IndexPresenter.this.links.size();
                IndexPresenter.this.links.addAll(l);

                adapter.notifyItemRangeInserted(size, l.size());
                currentPage++;
            }
        });
        receiver.receive();

    }

    /**
     * Setup adapter of the RecyclerView in the view.*/
    private void setupAdapter() {
        if (view == null)
            return;
        adapter = new IndexListAdapter(view.getContext(), this);
        adapter.onTakeList(links);
        view.setAdapter(adapter);
    }

    /**
     * Getting observable for {@link StreamPresenter#pageType}
     * @param page page to get
     * @return Observable assigned to {@link StreamPresenter#pageType}*/
    private Observable<List<Link>> getObservable(int page) {
        switch (pageType) {
            case Pages.Index.PROMOTED:
                return preferences.isLogged() ?
                        linksService.promotedObservable(Extras.APP_KEY, preferences.getUserkey(), page) :
                        linksService.promotedObservable(Extras.APP_KEY, page);

            case Pages.Index.UPCOMING:
                return preferences.isLogged() ?
                        linksService.upcomingObservable(Extras.APP_KEY, preferences.getUserkey(), page) :
                        linksService.upcomingObservable(Extras.APP_KEY, page);

            case Pages.Index.FAVORITE:
                if (!preferences.isLogged())
                    return null;
                UserService service = retrofit.create(UserService.class);
                return service.favorites(Extras.APP_KEY, preferences.getUserkey());

            default:
                return linksService.promotedObservable(Extras.APP_KEY, page);
        }
    }

    public void disableLoading() {
        adapter.setScrollEnd(true);
    }

    /**
     * Removing repetitions in lists.
     * @param list list to compare*/
    private void removeRepetition(List<Link> list) {
        for (Iterator<Link> iterator = list.iterator(); iterator.hasNext(); ) {
            Link item = iterator.next();
            if (ids.contains(item.getId()))
                iterator.remove();
            else
                ids.add(item.getId());
        }
    }

    public void showUser(String author) {
        view.setUserVisibleHint(false);
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uri", "http://www.wykop.pl/ludzie/" + author);
        fragment.setArguments(bundle);
        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment));
    }

    public void showLink(Link link) {
        view.setUserVisibleHint(false);
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uri", link.getUrl());
        fragment.setArguments(bundle);
        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment));
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    //TODO: Implement menu.
    public void itemMenuClick(int itemId) {
        
    }

    public void downVote(Link link) {

        if (!preferences.isLogged())
            return;

        if (link.getVote() == UserVote.DIG){
            cancel(link);
        } else if (link.getVote() == UserVote.NONE){
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(R.string.bury_title);
            Preferences.BuryReasons buryReasons = preferences.getBuryReasons();
            builder.setItems(buryReasons.getNames(),
                    (dialogInterface, i) -> bury(link, buryReasons.getIds().get(i)));
            builder.create().show();
        }
    }

    public void upVote(Link link) {
        if (link.getVote() == UserVote.BURY){
            cancel(link);
        } else {
            dig(link);
        }
    }

    private void bury(Link link, Integer reason){
        if (!preferences.isLogged()
                || link.getVote() == UserVote.BURY
                || voting)
            return;

        voting = true;

        LinkService linkService = retrofit.create(LinkService.class);
        linkService
                .bury(link.getId(), reason, preferences.getUserkey(), Extras.APP_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(linkVote -> updateLinkVote(link, linkVote, UserVote.BURY),
                        t -> {}, () -> voting = false);
    }

    private void dig(Link link){
        if (!preferences.isLogged()
                || link.getVote() == UserVote.DIG
                || voting)
            return;

        voting = true;

        LinkService linkService = retrofit.create(LinkService.class);
        linkService
                .dig(link.getId(), preferences.getUserkey(), Extras.APP_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(linkVote -> updateLinkVote(link, linkVote, UserVote.DIG),
                        t -> {}, () -> voting = false);
    }

    private void cancel(Link link){

        if (!preferences.isLogged()
                || link.getVote() == UserVote.NONE
                || voting)
            return;

        voting = true;

        LinkService linkService = retrofit.create(LinkService.class);
        linkService
                .cancel(link.getId(), preferences.getUserkey(), Extras.APP_KEY)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(linkVote -> updateLinkVote(link, linkVote, UserVote.NONE),
                        t -> {}, () -> voting = false);
    }

    private void updateLinkVote(Link link, LinkVote vote, UserVote userVote){
        if (vote.getError().getCode() != 0 || !vote.isSuccess()){
            EventBus.getDefault().post(new ShowMessage(vote.getError().toString()));
        } else {
            int index = links.indexOf(link);
            links.get(index).setVote(userVote);
            links.get(index).setVoteCount(vote.getVote());
            links.get(index).setReportCount(vote.getReport_count());
            adapter.notifyItemChanged(index);
        }
    }

}