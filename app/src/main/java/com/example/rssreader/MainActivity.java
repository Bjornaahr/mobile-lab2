package com.example.rssreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Adapter;
import android.widget.EditText;

import com.squareup.picasso.Downloader;

import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements MyAdapter.OnNoteListener {

    private SharedPreferences mPreferences;
    Handler handler = new Handler();
    RecyclerView rv;
    EditText editText;
    List<RssStruct> items = new ArrayList<>();
    private static Pattern regexPattern;
    String textRegex = "";


    Runnable timedTask = new Runnable(){

        @Override
        public void run() {

            int pos = mPreferences.getInt("FrequencyPos", 1);
            int time;

            switch (pos){
            //Update every 5, 15 , 60min or once a day
                case 0:
                    time = 60000 * 5;
                    break;
                case 1:
                    time = 60000 * 15;
                    break;
                case 2:
                    time = 60000 * 60;
                    break;
                case 3:
                    time = 86400000;
                    break;

                    default:
                        time = 60000 * 5;

            }

            Log.d("TIMER", "UPDATED FEED");


            String url = mPreferences.getString(getString(R.string.rss_url), "https://news.google.com/rss?hl=en-US&gl=US&ceid=US:en");
            handler.postDelayed(timedTask, time);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start a service so that the app runs and updates in the background
        //It does fetch data when you start the app again so in retrospect this is probably useless
        startService(new Intent(this, RssService.class));

        //Get the preferences
        mPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);

        editText = findViewById(R.id.regex);
        //Set the rss feed string, if none is given just show google news feed
        String url = mPreferences.getString(getString(R.string.rss_url), "https://news.google.com/rss?hl=en-US&gl=US&ceid=US:en");

        //Floating button for preferences
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        //Creates the recycle view
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        //Fetches the rss
        FetchRss(url);


        //Updates the feed at set time
        handler.post(timedTask);

        //Click the floating button and change over to preference activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(change);
            }
        });


        //Check when edit text for regex is changed
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textRegex = editText.getText().toString();
                setRv();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





    }

    //When returning to main update the feed
    @Override
    public void onResume() {
        super.onResume();
        String url = mPreferences.getString(getString(R.string.rss_url), "https://news.google.com/rss?hl=en-US&gl=US&ceid=US:en");
        FetchRss(url);
    }


    //Check when item is clicked and go to webview with correct link
    @Override
    public void onNoteClick(int position) {
       String link = items.get(position).link;

       Intent change = new Intent(MainActivity.this, WebActivity.class);
       change.putExtra("Url", link);
       startActivity(change);

    }

    //Gets the specified rss feed
    public void FetchRss(String url){
        new RssFetcher(MainActivity.this, url, this).execute();
    }

    //Set the List of rss articles
    public void setList(List<RssStruct> items){
        this.items = items;
    }

    //Set the recycle view with correct data (articles/filter)
    public void setRv(){
        MyAdapter adapter = new MyAdapter(MainActivity.this, items, this);
        adapter.getFilter().filter(textRegex);
        rv.setAdapter(adapter);
    }



}
