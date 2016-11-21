package pl.calc_exe.wykop.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.view.fragments.IndexFragment;

public class IndexViewPagerAdapter extends FragmentPagerAdapter {

    private boolean isLogged;
    private String[] titles;

    public IndexViewPagerAdapter(Fragment fragment, boolean isLogged) {
        super(fragment.getChildFragmentManager());
        titles = fragment.getResources().getStringArray(R.array.index_view_pager);
        this.isLogged = isLogged;
    }
    @Override
    public Fragment getItem(int position) {
        return IndexFragment.getInstance(position);
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
