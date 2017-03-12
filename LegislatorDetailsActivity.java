package com.example.adhitya.homework9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LegislatorDetailsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    ImageButton favourites;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legislator_details);
        Bundle bundle = this.getIntent().getExtras();
        try{
            preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
             final SharedPreferences.Editor editor = preferences.edit();
            final JSONObject legislator =  new JSONObject(bundle.getString("legislator"));
            String favouriteListString = preferences.getString("legislators", "Empty");
            favourites = (ImageButton) findViewById(R.id.legislatorFavouriteButton);
            if(!favouriteListString.equals("Empty")) {
                JSONArray favouriteList = new JSONArray(favouriteListString);
                for(int i = 0; i < favouriteList.length(); i++) {
                    if(((JSONObject)favouriteList.get(i)).getString("bioguide_id").equals(legislator.getString("bioguide_id"))) {
                        favourites.setImageResource(R.mipmap.ic_favourites_checked);
                        break;
                    }
                }
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Legislator Info");
            ImageButton facebook = (ImageButton) findViewById(R.id.legislatorFacebook);
            ImageButton twitter = (ImageButton) findViewById(R.id.legislatorTwitter);
            ImageButton website = (ImageButton) findViewById(R.id.legislatorWebsite);
            ImageView profilePic = (ImageView) findViewById(R.id.legislatorDetailsProfileImage);
            ImageView partyImage = (ImageView) findViewById(R.id.legislatorDetailsPartyImage);
            TextView partyName = (TextView) findViewById(R.id.legislatorDetailsPartyName);
            TextView name = (TextView) findViewById(R.id.legislatorName);
            TextView email = (TextView) findViewById(R.id.legislatorEmail);
            TextView chamber = (TextView) findViewById(R.id.legislatorChamber);
            TextView contact = (TextView) findViewById(R.id.legislatorContact);
            TextView startTerm = (TextView) findViewById(R.id.legislatorStartTerm);
            TextView endTerm = (TextView) findViewById(R.id.legislatorEndTerm);
            ProgressBar term = (ProgressBar) findViewById(R.id.legislatorTerm);
            TextView office = (TextView) findViewById(R.id.legislatorOfiice);
            TextView state = (TextView) findViewById(R.id.legislatorState);
            TextView fax = (TextView) findViewById(R.id.legislatorFax);
            TextView bday = (TextView) findViewById(R.id.legislatorBirthday);
            Picasso.with(getApplicationContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/f.png").resize(100,100).into(facebook);
            Picasso.with(getApplicationContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/t.png").resize(100,100).into(twitter);
            Picasso.with(getApplicationContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/w.png").resize(100,100).into(website);
            Picasso.with(getApplicationContext()).load("https://theunitedstates.io/images/congress/original/" + legislator.getString("bioguide_id") + ".jpg").resize(400,400).into(profilePic);

            if(legislator.getString("party").equalsIgnoreCase("R")) {
                Picasso.with(getApplicationContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/r.png").resize(100,100).into(partyImage);
                partyName.setText("Republican");
            }
            else if(legislator.getString("party").equalsIgnoreCase("D")) {
                Picasso.with(getApplicationContext()).load("http://cs-server.usc.edu:45678/hw/hw8/images/d.png").resize(100,100).into(partyImage);
                partyName.setText("Democrat");
            }
            else if(legislator.getString("party").equalsIgnoreCase("I")) {
                Picasso.with(getApplicationContext()).load(" http://independentamericanparty.org/wp-content/themes/v/images/logo-american-heritage-academy.png").resize(100,100).into(partyImage);
                partyName.setText("Independent");
            }

            name.setText(legislator.getString("title")+". "+legislator.getString("last_name")+", "+legislator.getString("first_name"));

            if(!legislator.has("oc_email")||legislator.getString("oc_email").equals("null")) {
                email.setText("N.A");
            }
            else{
                email.setText(legislator.getString("oc_email"));
            }

            if(!legislator.has("chamber") || legislator.getString("chamber").equals("null")) {
                chamber.setText("N.A");
            }
            else{
                chamber.setText(legislator.getString("chamber").substring(0,1).toUpperCase() + legislator.getString("chamber").substring(1));
            }

            if(!legislator.has("phone")||legislator.getString("phone").equals("null")) {
                contact.setText("N.A");
            }
            else {
                contact.setText(legislator.getString("phone"));
            }

            if(!legislator.has("term_start")||legislator.getString("term_start").equals("null")) {
                startTerm.setText("N.A");
            }
            else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd,yyyy");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(legislator.getString("term_start"));
                    startTerm.setText(targetFormat.format(convertedDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(!legislator.has("term_end")||legislator.getString("term_end").equals("null")) {
                startTerm.setText("N.A");
            }
            else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd,yyyy");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(legislator.getString("term_end"));
                    endTerm.setText(targetFormat.format(convertedDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date s = dateFormat.parse(legislator.getString("term_start"));
            Date e = dateFormat.parse(legislator.getString("term_end"));
            Date n = new Date();
            long progress = (n.getTime() - s.getTime()) * 100 / (e.getTime() - s.getTime());
            term.setProgress((int)progress);
            TextView legislatorTermTextProgress = (TextView)findViewById(R.id.legislatorTermTextProgress);
            legislatorTermTextProgress.setText(progress+"%");

            if(!legislator.has("office")||legislator.getString("office").equals("null")) {
                office.setText("N.A");
            }
            else {
                office.setText(legislator.getString("office"));
            }

            if(!legislator.has("state")||legislator.getString("state").equals("null")) {
                state.setText("N.A");
            }
            else{
                state.setText(legislator.getString("state"));
            }

            if(!legislator.has("fax")||legislator.getString("fax").equals("null")) {
                fax.setText("N.A");
            }
            else{
                fax.setText(legislator.getString("fax"));
            }

            if(!legislator.has("birthday")||legislator.getString("birthday").equals("null")) {
                bday.setText("N.A");
            }
            else{
                SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd,yyyy");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(legislator.getString("birthday"));
                    bday.setText(targetFormat.format(convertedDate));
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }

            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(!legislator.has("facebook_id") ||legislator.getString("facebook_id").equals("null")) {
                            Toast.makeText(getBaseContext(), "No Facebook id", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Uri uri = Uri.parse("http://facebook.com/"+legislator.getString("facebook_id")); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(!legislator.has("twitter_id") ||legislator.getString("twitter_id").equals("null")) {
                            Toast.makeText(getBaseContext(), "No Twitter id", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Uri uri = Uri.parse("http://twitter.com/"+legislator.getString("twitter_id")); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(!legislator.has("website") ||legislator.getString("website").equals("null")) {
                            Toast.makeText(getApplicationContext(), "No Website", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Uri uri = Uri.parse(legislator.getString("website")); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            favourites.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    try{
                        String favouriteListString = preferences.getString("legislators", null);
                        if(favouriteListString == null) {
                            JSONArray array = new JSONArray();
                            array.put(legislator);
                            editor.putString("legislators",array.toString()).apply();
                            favourites.setImageResource(R.mipmap.ic_favourites_checked);
                        }
                        else {
                            Boolean present = false;
                            JSONArray favouriteList = new JSONArray(favouriteListString);
                            for(int i = 0; i < favouriteList.length(); i++) {
                                if(((JSONObject)favouriteList.get(i)).getString("bioguide_id").equals(legislator.getString("bioguide_id"))) {
                                    present = true;
                                    break;
                                }
                            }
                            if(present) {
                                favourites.setImageResource(R.mipmap.ic_favourites);
                                JSONArray array = new JSONArray();
                                for(int i = 0; i < favouriteList.length(); i++) {
                                    if(!((JSONObject)favouriteList.get(i)).getString("bioguide_id").equals(legislator.getString("bioguide_id"))) {
                                        array.put(favouriteList.get(i));
                                    }
                                }
                                editor.putString("legislators", array.toString()).apply();
                            }
                            else{
                                favourites.setImageResource(R.mipmap.ic_favourites_checked);
                                favouriteList.put(legislator);
                                editor.putString("legislators", favouriteList.toString()).apply();
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
