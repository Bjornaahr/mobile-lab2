package com.example.rssreader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView titleTxt, descTxt;
    ImageView img;
    MyAdapter.OnNoteListener onNoteListener;

    //Populate the viewholder
    public MyViewHolder(View itemView, MyAdapter.OnNoteListener onNoteListener){
        super(itemView);

        titleTxt = (TextView) itemView.findViewById(R.id.titleTxt);
        descTxt = (TextView) itemView.findViewById(R.id.descTxt);
        img = (ImageView) itemView.findViewById(R.id.rssImage);

        this.onNoteListener = onNoteListener;
        itemView.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        onNoteListener.onNoteClick(getAdapterPosition());
    }
}