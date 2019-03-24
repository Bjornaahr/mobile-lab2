package com.example.rssreader;

public class RssStruct {
    public String title;
    public String description;
    public String link;
    public String image;


    public void setTitle(String title){
        this.title = title;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link){
        this.link = link;

    }

    public void setImage(String image){
        this.image = image;

    }

    public String getTitle(){
        return this.title;

    }

    public String getDescription() {
        return description;
    }

    public String getLink(){
        return this.link;

    }

    public String getImage(){
        return this.image;
    }

}
