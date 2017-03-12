package com.example.adhitya.homework9;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillDetailsActivity extends AppCompatActivity {
    SharedPreferences preferences;
    ImageButton favourites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();

        try{
            getSupportActionBar().setTitle("Bill Info");
            final JSONObject bill =  new JSONObject(bundle.getString("bill"));
            setContentView(R.layout.activity_bill_details);

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final SharedPreferences.Editor editor = preferences.edit();
            favourites = (ImageButton) findViewById(R.id.billFavouriteButton);
            String favouriteListString = preferences.getString("bills", "Empty");
            if(!favouriteListString.equals("Empty")) {
                JSONArray favouriteList = new JSONArray(favouriteListString);
                for(int i = 0; i < favouriteList.length(); i++) {
                    if(((JSONObject)favouriteList.get(i)).getString("bill_id").equals(bill.getString("bill_id"))) {
                        favourites.setImageResource(R.mipmap.ic_favourites_checked);
                        break;
                    }
                }
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            TextView billId = (TextView) findViewById(R.id.billId);
            TextView billTitle = (TextView) findViewById(R.id.billTitle);
            TextView billType = (TextView) findViewById(R.id.billType);
            TextView billSponsor = (TextView) findViewById(R.id.billSponsor);
            TextView billChamberName = (TextView) findViewById(R.id.billChamberName);
            TextView billStatus = (TextView) findViewById(R.id.billStatus);
            TextView billIntroducedOn = (TextView) findViewById(R.id.billIntroducedOn);
            TextView billCongressURL = (TextView) findViewById(R.id.billCongressURL);
            TextView billVersionStatus = (TextView) findViewById(R.id.billVersionStatus);
            TextView billURL = (TextView) findViewById(R.id.billURL);

            if(!bill.has("bill_id") || bill.getString("bill_id").equals("null")) {
                billId.setText("N.A");
            }
            else {
                billId.setText(bill.getString("bill_id"));
            }

            if(!bill.has("short_title") || bill.getString("short_title").equals("null")) {
                if(!bill.has("official_title") || bill.getString("official_title").equals("null")){
                    billTitle.setText("N.A");
                }
                else {
                    billTitle.setText(bill.getString("official_title"));
                }
            }
            else {
                billTitle.setText(bill.getString("short_title"));
            }

            if(!bill.has("bill_type") || bill.getString("bill_type").equals("null")) {
                billType.setText("N.A");
            }
            else{
                billType.setText(bill.getString("bill_type"));
            }

            billSponsor.setText(((JSONObject)bill.get("sponsor")).getString("title") + ". "+((JSONObject)bill.get("sponsor")).getString("last_name") +", "+((JSONObject)bill.get("sponsor")).getString("first_name"));

            if(bill.getString("chamber").equals("senate")) {
                billChamberName.setText("Senate");
            }
            else if(bill.getString("chamber").equals("house")) {
                billChamberName.setText("House");
            }
            else {
                billChamberName.setText("Joint");
            }

            if(((JSONObject)bill.get("history")).getString("active").equals("true")) {
                billStatus.setText("Active");
            }
            else{
                billStatus.setText("New");
            }

            if(!bill.has("introduced_on") || bill.getString("introduced_on").equals("null")) {
                billIntroducedOn.setText("N.A");
            }
            else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd,yyyy");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(bill.getString("introduced_on"));
                    billIntroducedOn.setText(targetFormat.format(convertedDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(!bill.has("congress") || ((JSONObject)bill.get("urls")).getString("congress").equals("null")) {
                billCongressURL.setText("N.A");
            }
            else{
                billCongressURL.setText(((JSONObject)bill.get("urls")).getString("congress"));
            }

            if(((JSONObject)bill.get("last_version")).getString("version_name").equals("null")) {
                billVersionStatus.setText("N.A");
            }
            else{
                billVersionStatus.setText(((JSONObject)bill.get("last_version")).getString("version_name"));
            }

            if(((JSONObject)((JSONObject)bill.get("last_version")).get("urls")).getString("pdf").equals("null")) {
                billURL.setText("N.A");
            }
            else{
                billURL.setText(((JSONObject)((JSONObject)bill.get("last_version")).get("urls")).getString("pdf"));
            }

            favourites.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    try{
                        String favouriteListString = preferences.getString("bills", null);
                        if(favouriteListString == null) {
                            JSONArray array = new JSONArray();
                            array.put(bill);
                            editor.putString("bills",array.toString()).apply();
                            favourites.setImageResource(R.mipmap.ic_favourites_checked);
                        }
                        else {
                            Boolean present = false;
                            JSONArray favouriteList = new JSONArray(favouriteListString);
                            for(int i = 0; i < favouriteList.length(); i++) {
                                if(((JSONObject)favouriteList.get(i)).getString("bill_id").equals(bill.getString("bill_id"))) {
                                    present = true;
                                    break;
                                }
                            }
                            if(present) {
                                favourites.setImageResource(R.mipmap.ic_favourites);
                                JSONArray array = new JSONArray();
                                for(int i = 0; i < favouriteList.length(); i++) {
                                    if(!((JSONObject)favouriteList.get(i)).getString("bill_id").equals(bill.getString("bill_id"))) {
                                        array.put(favouriteList.get(i));
                                    }
                                }
                                editor.putString("bills", array.toString()).apply();
                            }
                            else{
                                favourites.setImageResource(R.mipmap.ic_favourites_checked);
                                favouriteList.put(bill);
                                editor.putString("bills", favouriteList.toString()).apply();
                            }
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                   // this function
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
