package com.example.rssreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private EditText rssFeed;
    private Spinner frequency, itemsToLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        mPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();


        rssFeed = (EditText) findViewById(R.id.rssFeed);
        frequency = (Spinner) findViewById(R.id.frequency);
        itemsToLoad = (Spinner) findViewById(R.id.itemstoLoad);

        //Poplulate the list list with update times
        final List<String> frequencies = new ArrayList<>();
        frequencies.add("5 min");
        frequencies.add("15 min");
        frequencies.add("60 min");
        frequencies.add("24 hours");





        //Populate spinner
        ArrayAdapter<CharSequence> adapter;
        adapter = new ArrayAdapter(this, R.layout.frequencyspinner, frequencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequency.setAdapter(adapter);


        //Populate itemsload list
        final List<String> itemsLoaded = new ArrayList<>();
        itemsLoaded.add("10");
        itemsLoaded.add("20");
        itemsLoaded.add("50");
        itemsLoaded.add("100");

        //Populate spinner
        ArrayAdapter<CharSequence> adapterItems;
        adapterItems = new ArrayAdapter(this, R.layout.itemloadspinner, itemsLoaded);
        adapterItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemsToLoad.setAdapter(adapterItems);

        checkSharedPreferences();



        frequency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //Save the selected frequency and url
               int spinnerpos = frequency.getSelectedItemPosition();
               mEditor.putInt("FrequencyPos", spinnerpos);
               mEditor.apply();

               mEditor.putString(getString(R.string.rss_url), rssFeed.getText().toString());
               mEditor.commit();

            }


            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });





        itemsToLoad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                //Save the items to load and url
                int spinnerpos = itemsToLoad.getSelectedItemPosition();
                mEditor.putInt("ItemstoLoadPos", spinnerpos);
                mEditor.apply();

                int spinnerItem = Integer.parseInt(itemsToLoad.getSelectedItem().toString());
                mEditor.putInt("ItemstoLoad", spinnerItem);


                mEditor.putString(getString(R.string.rss_url), rssFeed.getText().toString());
                mEditor.commit();

            }


            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });






        rssFeed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                //Save URL after the text has been edited
                mEditor.putString(getString(R.string.rss_url), rssFeed.getText().toString());
                mEditor.commit();
            }
        });


    }


    //Loads the saved preferences
    private void checkSharedPreferences() {
        String rssURL = mPreferences.getString(getString(R.string.rss_url), "");
        int pos = mPreferences.getInt("FrequencyPos" ,1);
        int posItem = mPreferences.getInt("ItemstoLoadPos", 1);
        rssFeed.setText(rssURL);
        frequency.setSelection(pos);
        itemsToLoad.setSelection(posItem);
    }
}