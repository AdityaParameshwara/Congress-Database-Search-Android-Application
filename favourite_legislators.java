package com.example.adhitya.homework9;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class favourite_legislators extends Fragment implements View.OnClickListener{


    public favourite_legislators() {
        // Required empty public constructor
    }


    Map<String, Integer> mapIndex;
    final ArrayList<TextView> textViewArrayList = new ArrayList<>();
    ListView favourite_legislators_listview;
    View view;

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
        view =  inflater.inflate(R.layout.fragment_favourite_legislators, container, false);
        try{
            favourite_legislators_listview = (ListView) view.findViewById(R.id.favouriteLegislatorsListView);
            LegislatorListAdapter listViewAdapter;
            View.OnClickListener onClickListener;
            ArrayList<String> states = new ArrayList<>();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String favouriteLegislatorList = preferences.getString("legislators", null);
            if(favouriteLegislatorList != null) {
                JSONArray favouriteLegislators = new JSONArray(favouriteLegislatorList);
                for(int i = 0; i < favouriteLegislators.length(); i++) {
                    states.add(((JSONObject)favouriteLegislators.get(i)).getString("last_name"));
                }
                Collections.sort(states);
                //Sort according to the last_name
                favouriteLegislators =sort(favouriteLegislators, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject    ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return ja.optString("last_name", "").toLowerCase().compareTo(jb.optString("last_name", "").toLowerCase());
                    }
                });
                listViewAdapter = new LegislatorListAdapter(getContext(), favouriteLegislators);
                favourite_legislators_listview.setAdapter(listViewAdapter);

                getIndexList(states);
                displayIndex();

                favourite_legislators_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent viewDetailsActivity = new Intent(getContext(), LegislatorDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("legislator", favourite_legislators_listview.getItemAtPosition(position).toString());
                        viewDetailsActivity.putExtras(bundle);
                        startActivity(viewDetailsActivity);
                    }
                });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    public void getIndexList(ArrayList<String> states) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < states.size(); i++) {
            String state = states.get(i);
            String index = state.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }

    public void displayIndex() {
        textViewArrayList.clear();
        LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.favourite_legislator_side_index);
        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getActivity().getLayoutInflater().inflate(
                    R.layout.legislator_side_index, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        favourite_legislators_listview.setSelection(mapIndex.get(selectedIndex.getText()));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public void onResume(){
        super.onResume();
        try{
            ArrayList<String> states = new ArrayList<>();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String favouriteLegislatorList = preferences.getString("legislators", null);
            if(favouriteLegislatorList != null) {
                JSONArray favouriteLegislators = new JSONArray(favouriteLegislatorList);
                for(int i = 0; i < favouriteLegislators.length(); i++) {
                    states.add(((JSONObject)favouriteLegislators.get(i)).getString("last_name"));
                }
                Collections.sort(states);
                favouriteLegislators =sort(favouriteLegislators, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject    ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return ja.optString("last_name", "").toLowerCase().compareTo(jb.optString("last_name", "").toLowerCase());
                    }
                });
                LegislatorListAdapter listViewAdapter = new LegislatorListAdapter(getContext(), favouriteLegislators);
                favourite_legislators_listview.setAdapter(listViewAdapter);

                mapIndex = new LinkedHashMap<String, Integer>();
                for (int i = 0; i < states.size(); i++) {
                    String state = states.get(i);
                    String index = state.substring(0, 1);

                    if (mapIndex.get(index) == null)
                        mapIndex.put(index, i);
                }

                LinearLayout indexLayout = (LinearLayout) view.findViewById(R.id.favourite_legislator_side_index);
                indexLayout.removeAllViews();
                TextView textView;
                List<String> indexList = new ArrayList<String>(mapIndex.keySet());
                for (String index : indexList) {
                    textView = (TextView) getActivity().getLayoutInflater().inflate(
                            R.layout.legislator_side_index, null);
                    textView.setText(index);
                    textView.setOnClickListener(this);
                    indexLayout.addView(textView);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
