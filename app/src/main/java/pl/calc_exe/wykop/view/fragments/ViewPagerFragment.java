package pl.calc_exe.wykop.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.calc_exe.wykop.R;
import pl.calc_exe.wykop.adapters.IndexViewPagerAdapter;
import pl.calc_exe.wykop.adapters.StreamViewPagerAdapter;
import pl.calc_exe.wykop.events.ActionBarEvent;
import pl.calc_exe.wykop.events.LoadEvent;
import pl.calc_exe.wykop.extras.Extras;
import pl.calc_exe.wykop.extras.Pages;

public class ViewPagerFragment extends Fragment {
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.tab_layout) TabLayout tabLayout;

    private int page;

    FragmentPagerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(Pages.PAGE);
        boolean isLogged = getArguments().getBoolean(Extras.IS_LOGGED);
        switch (page){
            case Pages.INDEX:
                adapter = new IndexViewPagerAdapter(this, isLogged, Pages.Index.PROMOTED);
                break;
            case Pages.STREAM:
                adapter = new StreamViewPagerAdapter(this, isLogged, Pages.Stream.INDEX);
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        ButterKnife.bind(this, view);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(new LoadEvent(position));
                EventBus.getDefault().post(new ActionBarEvent(page, position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return view;
    }
}
