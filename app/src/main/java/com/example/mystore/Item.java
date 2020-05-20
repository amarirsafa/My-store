package com.example.mystore;

import android.media.Image;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.util.ArrayList;

public class Item implements Parcelable {
    private static int count =87664;
    private int id;
    private String title,category,who_ma_it,what_is_it,description;
    private Double price,quantity;
    private int amount=0;
    private ArrayList<String> pictures = new ArrayList<>();

    public Item(){
        setId(++count);
    }
    public Item(Item item){
        setId(item.getId());
        setTitle(item.getTitle());
        setCategory(item.getCategory());
        setDescription(item.getDescription());
        setQuantity(item.getQuantity());
        setPrice(item.getPrice());
        setWhat_is_it(item.getWhat_is_it());
        setWho_ma_it(item.getWho_ma_it());
        setPictures(item.getPictures());
    }

    protected Item(Parcel in) {
        id = in.readInt();
        title = in.readString();
        category = in.readString();
        who_ma_it = in.readString();
        what_is_it = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readDouble();
        }
        pictures = in.createStringArrayList();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public ArrayList<String> getPictures(){return this.pictures;}
    public void setPictures(ArrayList<String> pictures){this.pictures=pictures;}

    public String getTitle() {return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getPrice() {return price;}
    public void setPrice(Double price) {this.price = price; }

    public Double getQuantity() {return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getCategory() {return category; }
    public void setCategory(String category) {this.category = category;}

    public String getDescription() {return description; }
    public void setDescription(String description) { this.description = description; }

    public String getWhat_is_it() { return what_is_it; }
    public void setWhat_is_it(String what_is_it) { this.what_is_it = what_is_it; }

    public String getWho_ma_it() { return who_ma_it;}
    public void setWho_ma_it(String who_ma_it) { this.who_ma_it = who_ma_it; }

    public int getAmount() {return amount;}
    public void setAmount(int amount) { this.amount = amount; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(who_ma_it);
        dest.writeString(what_is_it);
        dest.writeString(description);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(quantity);
        }
        dest.writeStringList(pictures);
    }
}
