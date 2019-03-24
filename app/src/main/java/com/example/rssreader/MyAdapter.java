package com.example.rssreader;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> implements Filterable
{

    private SharedPreferences mPreferences;
    Context c;
    List<RssStruct> items;
    private  OnNoteListener onNoteListener;
    List<RssStruct> itemsFull;


    //Creates and populate the adapter
    public  MyAdapter(Context c, List<RssStruct> items, OnNoteListener onNoteListener){
        this.c = c;
        this.items = new ArrayList<>(items);

        this.onNoteListener =  onNoteListener;
        this.itemsFull = new ArrayList<>(items);;
    }

    //Creates a viewholder
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(c).inflate(R.layout.model ,parent,false);
        return new MyViewHolder(v, onNoteListener);
    }


    //Populates the view holder with relevant data
    @Override
    public void onBindViewHolder(MyViewHolder holder, int pos){
        RssStruct rss = items.get(pos);

        String title = rss.getTitle();
        String desc = rss.getDescription();
        String imageUrl = rss.getImage();

        holder.titleTxt.setText(title);
        holder.descTxt.setText(Html.fromHtml(desc).toString());
        //Temp picture cause I can be bother to acutally parse all the diffrent ways people do pictures
        Picasso.get().load("https://morphict.co.uk/wp-content/uploads/2018/11/How.png").into(holder.img);

    }


    //Returns number in preferences if it is smaller than the size of the rss feed
    @Override
    public int getItemCount() {

        mPreferences = c.getSharedPreferences("myData", Context.MODE_PRIVATE);

        int number = mPreferences.getInt("ItemstoLoad", 1);

        if(items.size() < number) {
            return  items.size();
        }
        else return number;
    }

    //Filters the view
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            //Set a list as the new list to show after filtering
            List<RssStruct> filterdList = new ArrayList<>();

            //If the regex field is empty show the full list
            if(constraint == null | constraint.length() == 0){
                filterdList.addAll(itemsFull);
            } else {
                try {
                    Pattern filterPattern = Pattern.compile(constraint.toString());

                    //If the syntax is valid show the matching items
                    for (RssStruct item : items) {
                        if (filterPattern.matcher(item.title).matches()) {
                            filterdList.add(item);
                        }
                    }
                    //If the syntax is invalid show full list
                }catch (PatternSyntaxException exception) {
                    filterdList.addAll(itemsFull);
                }


            }
            //Set the result to filtered items
            FilterResults results = new FilterResults();
            results.values = filterdList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //Clear all items
            items.clear();
            //Add new list to old list
            items.addAll((List) results.values);
            //Update
            notifyDataSetChanged();
        }
    };


    public interface OnNoteListener{
        void onNoteClick(int position);
    }





}
