package com.example.rssreader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class RssFetcher extends AsyncTask<Void,Void,List<RssStruct>> {
    URL url;
    InputStream inputStream;
    Context c;
    List<RssStruct> items = new ArrayList<>();
    String urlString;
    private MainActivity activity;



    //Set the relevant data
    public RssFetcher(Context c, String urlString, MainActivity activity) {
        this.c = c;
        this.urlString = urlString;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {

    }


    //Actually parsing the rss feed
    @Override
    protected List<RssStruct> doInBackground(Void... arg0){
        boolean isItem = false;
        String tagValue = null;
        RssStruct rss = new RssStruct();

        //Bool for checking if the data that we read is just metadata for the site
        boolean isSiteMeta = false;

        try{
            //Set the url with the url from preferences or standard
            url = new URL(urlString);
            //Open the site
            inputStream = url.openConnection().getInputStream();

            try {
                //Create a XML parser
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStream, null); // pass input whatever xml you have

                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String name = xpp.getName();



                    //Start of document
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        Log.d(TAG,"Start document");
                    }






                    //Check for opening tag
                    else if(eventType == XmlPullParser.START_TAG) {
                        Log.d(TAG,"Start tag "+xpp.getName());
                        //If the xml tag is item, not site meta and create a new RSS
                        if(name.equalsIgnoreCase("item"))
                        {
                            rss = new RssStruct();
                            isSiteMeta = false;
                        }

                    }

                    //Get the text in tag
                    else if(eventType == XmlPullParser.TEXT) {
                        tagValue = xpp.getText();
                    }



                    else if(eventType == XmlPullParser.END_TAG) {

                        //Do stuff is not meta data
                        if(!isSiteMeta) {
                            Log.d(TAG, "End tag " + xpp.getName());
                            //Set title
                            if (name.equalsIgnoreCase("title")) {
                                rss.setTitle(tagValue);

                                //Set Link
                            } else if (name.equalsIgnoreCase("link")) {
                                rss.setLink(tagValue);

                                //Set description
                            } else if (name.equalsIgnoreCase("description")) {
                                rss.setDescription(tagValue);

                                //Set image (Not working correctly since alot of RSS feeds have diffrent standards)
                            } else if (name.equalsIgnoreCase("image")) {
                                rss.setImage(tagValue);
                            }

                            //Set the rss if the closing tag is item, also set metadata true
                            if (name.equalsIgnoreCase("item")) {
                                items.add(rss);
                                isSiteMeta = true;
                            }
                        }

                    }
                    //Next tag
                    eventType = xpp.next();
                }

                Log.d(TAG,"End document");

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Size of rss", Integer.toString(items.size()));
        //Returns the items
        return items;
    }


    protected void onPostExecute(List<RssStruct> items) {
        //Set the list in main
        activity.setList(items);
        //Set the recycleview with relevant data
        activity.setRv();
    }

}
