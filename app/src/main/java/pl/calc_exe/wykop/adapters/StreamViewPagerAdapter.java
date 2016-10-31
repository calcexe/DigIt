package pl.calc_exe.wykop.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.view.fragments.StreamFragment;

public class StreamViewPagerAdapter extends FragmentPagerAdapter {

    private String[] titles;

    private boolean isLogged;
    private int startPage;

    public StreamViewPagerAdapter(Fragment fragment, boolean isLogged, int startPage) {
        super(fragment.getChildFragmentManager());
        titles = fragment.getResources().getStringArray(R.array.stream_view_pager);
        this.isLogged = isLogged;
        this.startPage = startPage;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new StreamFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Pages.PAGE, position);
        bundle.putBoolean(Extras.START_PAGE, startPage == position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return isLogged ? titles.length : titles.length - 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
