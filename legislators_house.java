package com.example.adhitya.homework9;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class legislators_house extends Fragment implements View.OnClickListener {


    public legislators_house() {
        // Required empty public constructor
    }

    Map<String, Integer> legislatorHouseMapIndex;
    final ArrayList<TextView> textViewArrayList = new ArrayList<>();
    ListView legislators_house_listview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_legislator_house, container, false);
        try{
            legislators_house_listview = (ListView) view.findViewById(R.id.legislatorsHouseListView);
//            new GetLegislatorHouseData(legislators_house_listview, this).execute("http://104.198.0.197:8080/legislators?per_page=all&chamber=house");
            new GetLegislatorHouseData(legislators_house_listview, this).execute("http://lowcost-env.tzwskpm3qp.us-west-2.elasticbeanstalk.com/?type=legislatorsHouse");
            legislators_house_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent viewDetailsActivity = new Intent(getContext(), LegislatorDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("legislator", legislators_house_listview.getItemAtPosition(position).toString());
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

    public class GetLegislatorHouseData extends AsyncTask<String, String, Void> {
        private ListView legislators_house_listview;
        LegislatorListAdapter listViewAdapter;
        View.OnClickListener onClickListener;
        ArrayList<String> states = new ArrayList<>();

        public GetLegislatorHouseData(ListView legislators_house_listview, View.OnClickListener onClickListener) {
            this.legislators_house_listview = legislators_house_listview;
            this.onClickListener = onClickListener;
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

                for(int i = 0; i < main.length(); i++) {
                    states.add(((JSONObject)main.get(i)).getString("last_name"));
                }

                Collections.sort(states);

                //Sort according to the last_name
                main =sort(main, new Comparator(){
                    public int compare(Object a, Object b){
                        JSONObject    ja = (JSONObject)a;
                        JSONObject    jb = (JSONObject)b;
                        return ja.optString("last_name", "").toLowerCase().compareTo(jb.optString("last_name", "").toLowerCase());
                    }
                });


                listViewAdapter = new LegislatorListAdapter(getContext(), main);

                getIndexList(states);
                displayIndex();
                urlConnection.disconnect();

            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        private void getIndexList(ArrayList<String> states) {
            legislatorHouseMapIndex = new LinkedHashMap<String, Integer>();
            for (int i = 0; i < states.size(); i++) {
                String state = states.get(i);
                String index = state.substring(0, 1);

                if (legislatorHouseMapIndex.get(index) == null)
                    legislatorHouseMapIndex.put(index, i);
            }
        }

        public void displayIndex() {
            textViewArrayList.clear();
            final LinearLayout indexLayout = (LinearLayout) getActivity().findViewById(R.id.legislator_house_side_index);
            TextView textView;
            List<String> indexList = new ArrayList<String>(legislatorHouseMapIndex.keySet());
            for (String index : indexList) {
                textView = (TextView) getActivity().getLayoutInflater().inflate(
                        R.layout.legislator_side_index, null);
                textView.setText(index);
                textView.setOnClickListener(onClickListener);
                textViewArrayList.add(textView);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for(TextView textView1: textViewArrayList){
                        indexLayout.addView(textView1);
                    }
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            legislators_house_listview.setAdapter(listViewAdapter);
        }

    }

    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        legislators_house_listview.setSelection(legislatorHouseMapIndex.get(selectedIndex.getText()));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }
}
