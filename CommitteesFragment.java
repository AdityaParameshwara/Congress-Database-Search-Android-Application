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

public class CommitteesFragment extends Fragment {

    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity)activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.committeesfragment, container, false);
        TabLayout tabLayout;
        ViewPager viewPager;
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();
        PagerAdapter pagerAdapter = new PagerAdapter(fragmentManager);

        tabLayout = (TabLayout) view.findViewById(R.id.committeesTabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.committeesViewPager);
        pagerAdapter.addFragments(new committees_house(), "HOUSE");
        pagerAdapter.addFragments(new committees_senate(), "SENATE");
        pagerAdapter.addFragments(new committees_joint(), "JOINT");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
