package com.example.adhitya.homework9;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class favourite_committees extends Fragment {

    ListView favourite_committees_listview;
    public favourite_committees() {
        // Required empty public constructor
    }

    //Sort the values
    public static JSONArray sort(JSONArray array, Comparator c){
        List asList = new ArrayList(array.length());
        for (int i=0; i<array.length(); i++){
            asList.add(array.opt(i));
        }
        Collections.sort(asList, c);
        JSONArray  res = new JSONArray();
        for (Object o : asList){
            res.put(o);
        }
        return res;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favourite_committees, container, false);
        try{
            favourite_committees_listview = (ListView) view.findViewById(R.id.favouriteCommitteesListView);
            CommitteeListAdapter committeeListAdapter = null;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String favouriteCommitteesList = preferences.getString("committees",null);
            if(favouriteCommitteesList != null){
                JSONArray favouriteCommittees = new JSONArray(favouriteCommitteesList);
                //Sort according to the state_name
                favouriteCommittees =sort(favouriteCommittees, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return ja.optString("name", "").toLowerCase().compareTo(jb.optString("name", "").toLowerCase());
                    }
                });
                committeeListAdapter = new CommitteeListAdapter(getContext(), favouriteCommittees);
            }
            favourite_committees_listview.setAdapter(committeeListAdapter);
            favourite_committees_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent viewDetailsActivity = new Intent(getContext(), CommitteeDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("committee", favourite_committees_listview.getItemAtPosition(position).toString());
                    viewDetailsActivity.putExtras(bundle);
                    startActivity(viewDetailsActivity);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            ArrayList<String> states = new ArrayList<>();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String favouriteCommitteesList = preferences.getString("committees", null);
            if(favouriteCommitteesList != null) {
                JSONArray favouriteCommittees = new JSONArray(favouriteCommitteesList);
                //Sort according to the state_name
                favouriteCommittees =sort(favouriteCommittees, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return ja.optString("name", "").toLowerCase().compareTo(jb.optString("name", "").toLowerCase());
                    }
                });
                CommitteeListAdapter favourite_committees_listviewAdapter = new CommitteeListAdapter(getContext(), favouriteCommittees);
                favourite_committees_listview.setAdapter(favourite_committees_listviewAdapter);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
