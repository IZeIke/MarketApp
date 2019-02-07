package com.example.harit.marketapp.ui.adapter;


import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class MainPageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentList = new ArrayList();
    private ArrayList<String> titleList = new ArrayList();
    private ArrayList<Integer> indexList = new ArrayList();


    public MainPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragmentItem(Fragment fragment, Integer position, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
        indexList.add(position);
    }

    @Override
    public int getCount() {
        if (fragmentList == null)
            return 0;
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(getFragmentPosition(position));
    }


    private Integer getFragmentPosition(Integer position) {
        return position;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(getFragmentPosition(position));
    }


    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void addFragment(Integer position, Integer index, Fragment fragment) {
        fragmentList.add(position, fragment);
        indexList.add(position, index);
        notifyDataSetChanged();
    }
}


