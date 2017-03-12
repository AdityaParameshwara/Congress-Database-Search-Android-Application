package com.example.adhitya.homework9;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class CommitteeDetailsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    ImageButton favourites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            Bundle bundle = this.getIntent().getExtras();
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_committee_details);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Committee Info");
            final JSONObject committee =  new JSONObject(bundle.getString("committee"));

            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            final SharedPreferences.Editor editor = preferences.edit();
            favourites = (ImageButton) findViewById(R.id.committeeFavouriteButton);
            String favouriteListString = preferences.getString("committees", "Empty");
            if(!favouriteListString.equals("Empty")) {
                JSONArray favouriteList = new JSONArray(favouriteListString);
                for(int i = 0; i < favouriteList.length(); i++) {
                    if(((JSONObject)favouriteList.get(i)).getString("committee_id").equals(committee.getString("committee_id"))) {
                        favourites.setImageResource(R.mipmap.ic_favourites_checked);
                        break;
                    }
                }
            }

            TextView committeeId = (TextView) findViewById(R.id.committeeDetailsId);
            TextView committeeName = (TextView) findViewById(R.id.committeeDetailsName);
            final ImageView chamberImage = (ImageView) findViewById(R.id.committeeChamberImage);
            TextView chamberName = (TextView) findViewById(R.id.committeeChamberName);
            TextView parentCommittee = (TextView) findViewById(R.id.parentCommittee);
            TextView committeeContact = (TextView) findViewById(R.id.committeeContact);
            TextView committeeOffice = (TextView) findViewById(R.id.committeeOffice);

            if(!committee.has("committee_id") || committee.getString("committee_id").equals("null")) {
                committeeId.setText("N.A");
            }
            else {
                committeeId.setText(committee.getString("committee_id"));
            }

            if(!committee.has("name") || committee.getString("name").equals("null")) {
                committeeName.setText("N.A");
            }
            else{
                committeeName.setText(committee.getString("name"));
            }

            if(committee.getString("chamber").equals("senate")) {
                chamberImage.setImageResource(R.mipmap.ic_senate);
                chamberName.setText("Senate");
            }
            else if(committee.getString("chamber").equals("house")) {
                Picasso.with(getApplicationContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/h.png").resize(100,100).into(chamberImage);
                chamberName.setText("House");
            }
            else {
                chamberImage.setImageResource(R.mipmap.ic_senate);
                chamberName.setText("Joint");
            }

            if(!committee.has("parent_committee_id") || committee.getString("parent_committee_id").equals("null")) {
                parentCommittee.setText("N.A");
            }
            else{
                parentCommittee.setText(committee.getString("parent_committee_id"));
            }

            if(!committee.has("phone") || committee.getString("phone").equals("null")) {
                committeeContact.setText("N.A");
            }
            else{
                committeeContact.setText(committee.getString("phone"));
            }


            if(!committee.has("office") || committee.getString("office").equals("null")) {
                committeeOffice.setText("N.A");
            }
            else {
                committeeOffice.setText(committee.getString("office"));
            }

            favourites.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    try{
                        String favouriteListString = preferences.getString("committees", null);
                        if(favouriteListString == null) {
                            JSONArray array = new JSONArray();
                            array.put(committee);
                            editor.putString("committees",array.toString()).apply();
                            favourites.setImageResource(R.mipmap.ic_favourites_checked);
                        }
                        else {
                            Boolean present = false;
                            JSONArray favouriteList = new JSONArray(favouriteListString);
                            for(int i = 0; i < favouriteList.length(); i++) {
                                if(((JSONObject)favouriteList.get(i)).getString("committee_id").equals(committee.getString("committee_id"))) {
                                    present = true;
                                    break;
                                }
                            }
                            if(present) {
                                favourites.setImageResource(R.mipmap.ic_favourites);
                                JSONArray array = new JSONArray();
                                for(int i = 0; i < favouriteList.length(); i++) {
                                    if(!((JSONObject)favouriteList.get(i)).getString("committee_id").equals(committee.getString("committee_id"))) {
                                        array.put(favouriteList.get(i));
                                    }
                                }
                                editor.putString("committees", array.toString()).apply();
                            }
                            else{
                                favourites.setImageResource(R.mipmap.ic_favourites_checked);
                                favouriteList.put(committee);
                                editor.putString("committees", favouriteList.toString()).apply();
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
