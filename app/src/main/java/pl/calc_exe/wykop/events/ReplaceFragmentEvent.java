package pl.calc_exe.wykop.events;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import pl.calc_exe.wykop.extras.Pages;

/**
 * Created by Mateusz on 2016-10-16.
 */
public class ReplaceFragmentEvent {
    private Fragment fragment;
    private int page;
    private int pageType;

    public ReplaceFragmentEvent(Fragment fragment, int page, int pageType) {
        this.fragment = fragment;
        this.page = page;
        this.pageType = pageType;
    }

    public ReplaceFragmentEvent(Fragment fragment){
        this.fragment = fragment;
        page = Pages.NONE;
        pageType = Pages.NONE;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getPageType() {
        return pageType;
    }

    public int getPage() {
        return page;
    }
}
