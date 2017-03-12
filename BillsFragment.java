package com.example.adhitya.homework9;

import android.app.Activity;
import android.content.Context;
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

public class BillsFragment extends Fragment {

    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.billsfragment, container, false);
        TabLayout billsFragmentTabLayout;
        ViewPager viewPager;
        PagerAdapter pagerAdapter;
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();

        billsFragmentTabLayout = (TabLayout) view.findViewById(R.id.billsTabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.billsViewPager);
        pagerAdapter = new PagerAdapter(fragmentManager);
        pagerAdapter.addFragments(new active_bills(), "ACTIVE BILLS");
        pagerAdapter.addFragments(new new_bills(), "NEW BILLS");
        viewPager.setAdapter(pagerAdapter);
        billsFragmentTabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
