package com.example.adhitya.homework9;

import android.app.Activity;
import android.os.AsyncTask;
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
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Adhitya on 17-11-2016.
 */

public class LegislatorsFragment extends Fragment {

    private FragmentActivity myContext;

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.legislatorsfragment, container, false);
        FragmentManager fragmentManager = myContext.getSupportFragmentManager();
        TabLayout tabLayout;
        final ViewPager viewPager;
        PagerAdapter pagerAdapter;

        tabLayout = (TabLayout)view.findViewById(R.id.legislatorTabLayout);
        viewPager = (ViewPager)view.findViewById(R.id.legislatorsViewPager);
        pagerAdapter = new PagerAdapter(fragmentManager);
        pagerAdapter.addFragments(new legislators_bystate(), "BY STATE");
        pagerAdapter.addFragments(new legislators_house(), "HOUSE");
        pagerAdapter.addFragments(new legislators_senate(), "SENATE");
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

}
