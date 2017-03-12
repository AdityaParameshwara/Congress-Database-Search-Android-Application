package com.example.adhitya.homework9;


import android.content.Intent;
import android.content.SharedPreferences;
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
public class favourite_bills extends Fragment {

    ListView favourite_bills_listview;
    public favourite_bills() {
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
        View view =  inflater.inflate(R.layout.fragment_favourite_bills, container, false);
        try{
            favourite_bills_listview = (ListView) view.findViewById(R.id.favouriteBillsListView);
            BillsListAdapter billsListAdapter = null;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String favouriteBillsList = preferences.getString("bills",null);
            if(favouriteBillsList != null){
                JSONArray favouriteBills = new JSONArray(favouriteBillsList);
                //Sort according to the state_name
                favouriteBills =sort(favouriteBills, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return jb.optString("introduced_on", "").toLowerCase().compareTo(ja.optString("introduced_on", "").toLowerCase());
                    }
                });
                billsListAdapter = new BillsListAdapter(getContext(), favouriteBills);
            }

            favourite_bills_listview.setAdapter(billsListAdapter);

            favourite_bills_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent viewDetailsActivity = new Intent(getContext(), BillDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("bill", favourite_bills_listview.getItemAtPosition(position).toString());
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
            String favouriteBillsList = preferences.getString("bills", null);
            if(favouriteBillsList != null) {
                JSONArray favouriteBills = new JSONArray(favouriteBillsList);
                //Sort according to the state_name
                favouriteBills =sort(favouriteBills, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return jb.optString("introduced_on", "").toLowerCase().compareTo(ja.optString("introduced_on", "").toLowerCase());
                    }
                });
                BillsListAdapter favourite_bills_listviewAdapter = new BillsListAdapter(getContext(), favouriteBills);
                favourite_bills_listview.setAdapter(favourite_bills_listviewAdapter);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

}
