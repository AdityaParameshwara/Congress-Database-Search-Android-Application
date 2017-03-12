package com.example.adhitya.homework9;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Adhitya on 19-11-2016.
 */

public class BillsListAdapter extends BaseAdapter{
    Context context;
    JSONArray data;
    private static LayoutInflater inflater = null;

    public BillsListAdapter(Context context, JSONArray data) {
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
            convertView = inflater.inflate(R.layout.billsrow, null);


        TextView billId = (TextView) convertView.findViewById(R.id.billId);
        TextView shortTitle  = (TextView) convertView.findViewById(R.id.shortTitle);
        TextView introducedOn = (TextView) convertView.findViewById(R.id.billIntroducedOn);

        JSONObject json_data = getItem(position);
        if (null != json_data) {
            try {
                billId.setText(json_data.getString("bill_id"));
                if(json_data.getString("short_title").equals("null")) {
                    if(json_data.getString("official_title").equals("null")){
                        shortTitle.setText("N.A");
                    }
                    else {
                        shortTitle.setText(json_data.getString("official_title"));
                    }
                }
                else {
                    shortTitle.setText(json_data.getString("short_title"));
                }
                if(json_data.getString("introduced_on").equals("null")) {
                    introducedOn.setText("N.A");
                }
                else{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(json_data.getString("introduced_on"));
                        introducedOn.setText(targetFormat.format(convertedDate));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
