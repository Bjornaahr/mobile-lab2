package com.example.rssreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {


    WebView webView;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        //Get the url
        Bundle extras = getIntent().getExtras();
        //If the url is not null show the webpage
        if(extras != null){
            link = extras.getString("Url");

            webView = (WebView) findViewById(R.id.webview);

            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(link);
        } else{
            //ERROR
        }




    }
}
