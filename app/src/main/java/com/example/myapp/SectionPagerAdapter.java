package com.example.myapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectioPagerAdapter";
    private final List<Fragment> mFragementList =new ArrayList<>();
    public SectionPagerAdapter(FragmentManager fm)
    {super(fm);}
    @Override
    public Fragment getItem(int position)
    {
        return mFragementList.get(position);
    }
    @Override
    public int getCount()
    {
        return mFragementList.size();
    }

    public void addFragment(Fragment fragment)
    {
        mFragementList.add(fragment);
    }
}
