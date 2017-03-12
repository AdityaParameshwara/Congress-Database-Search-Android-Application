package com.example.adhitya.homework9;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Adhitya on 17-11-2016.
 */

public class FavouritesFragment extends Fragment {

    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favouritesfragment, container, false);
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();
        TabLayout tabLayout;
        final ViewPager viewPager;
        PagerAdapter pagerAdapter;

        tabLayout = (TabLayout)view.findViewById(R.id.favouritesTabLayout);
        viewPager = (ViewPager)view.findViewById(R.id.favouritesViewPager);
        pagerAdapter = new PagerAdapter(fragmentManager);
        pagerAdapter.addFragments(new favourite_legislators(), "LEGISLATORS");
        pagerAdapter.addFragments(new favourite_bills(), "BILLS");
        pagerAdapter.addFragments(new favourite_committees(), "COMMITTEES");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
