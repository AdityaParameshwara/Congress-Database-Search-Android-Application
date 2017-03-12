package com.example.adhitya.homework9;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class committees_senate extends Fragment {

    ListView committee_senate_listview;
    public committees_senate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_committees_house, container, false);
        try{
            committee_senate_listview = (ListView) view.findViewById(R.id.committeesListView);
            new GetCommitteesSenateData(committee_senate_listview).execute("http://lowcost-env.tzwskpm3qp.us-west-2.elasticbeanstalk.com/?type=committeesSenate");
            committee_senate_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent viewDetailsActivity = new Intent(getContext(), CommitteeDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("committee", committee_senate_listview.getItemAtPosition(position).toString());
                    viewDetailsActivity.putExtras(bundle);
                    startActivity(viewDetailsActivity);
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
        return view;
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

    public class GetCommitteesSenateData extends AsyncTask<String, String, Void> {
        private ListView committee_senate_listview;
        CommitteeListAdapter committeeListAdapter;

        public GetCommitteesSenateData(ListView committee_senate_listview) {
            this.committee_senate_listview = committee_senate_listview;
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString().substring(1,builder.toString().length()-1));
                JSONArray main = topLevel.getJSONArray("results");

                //Sort according to the state_name
                main =sort(main, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject    ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return ja.optString("name", "").toLowerCase().compareTo(jb.optString("name", "").toLowerCase());
                    }
                });


                committeeListAdapter = new CommitteeListAdapter(getContext(), main);

                urlConnection.disconnect();

            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            committee_senate_listview.setAdapter(committeeListAdapter);
        }
    }

}
