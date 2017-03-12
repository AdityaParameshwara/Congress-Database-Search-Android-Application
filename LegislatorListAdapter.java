package com.example.adhitya.homework9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Adhitya on 18-11-2016.
 */

class LegislatorListAdapter extends BaseAdapter {

    Context context;
    JSONArray data;
    private static LayoutInflater inflater = null;

    public LegislatorListAdapter(Context context, JSONArray data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.length();
    }

    @Override
    public JSONObject getItem(int position) {
        // TODO Auto-generated method stub
        return data.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);
        return jsonObject.optLong("id");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.legislatorrow, null);


        TextView legislatorName = (TextView) convertView.findViewById(R.id.legislatorName);
        TextView district = (TextView) convertView.findViewById(R.id.legislatorDistrict);
        ImageView legislatorImage = (ImageView) convertView.findViewById(R.id.legislatorImage);

        JSONObject json_data = getItem(position);
        if (null != json_data) {
            try {
                Picasso.with(context).load("https://theunitedstates.io/images/congress/original/" + json_data.getString("bioguide_id") + ".jpg").resize(50,50).into(legislatorImage);
                legislatorName.setText(json_data.getString("last_name") + ", " + json_data.getString("first_name"));
                String party, state, districtStr;
                if(json_data.getString("party").equals("null")) {
                    party = "N.A";
                }
                else {
                    party = json_data.getString("party");
                }
                if(json_data.getString("state_name").equals("null")) {
                    state = "N.A";
                }
                else{
                    state = json_data.getString("state_name");
                }
                if(json_data.getString("district").equals("null")) {
                    districtStr = "0";
                }
                else {
                    districtStr = json_data.getString("district");
                }

                district.setText("("+party+")"+state+" - District "+districtStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
