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
 * Created by Adhitya on 20-11-2016.
 */

public class CommitteeListAdapter extends BaseAdapter {
    Context context;
    JSONArray data;
    private static LayoutInflater inflater = null;

    public CommitteeListAdapter(Context context, JSONArray data) {
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
            convertView = inflater.inflate(R.layout.committeerow, null);


        TextView committeeId = (TextView) convertView.findViewById(R.id.committeeId);
        TextView committeeName = (TextView) convertView.findViewById(R.id.committeeName);
        TextView committeeChamber = (TextView) convertView.findViewById(R.id.committeeChamber);

        JSONObject json_data = getItem(position);
        if (null != json_data) {
            try {
                committeeId.setText(json_data.getString("committee_id"));
                if(json_data.getString("name").equals("null")) {
                    committeeName.setText("N.A");
                }
                else{
                    committeeName.setText(json_data.getString("name"));
                }
                if(json_data.getString("chamber").equals("null")) {
                    committeeChamber.setText("N.A");
                }
                else {
                    String chamber = json_data.getString("chamber");
                    committeeChamber.setText(chamber.substring(0,1).toUpperCase() + chamber.substring(1));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
