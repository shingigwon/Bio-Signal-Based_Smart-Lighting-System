package com.example.bio_sginal_smart_lighting.SocketIO;

import android.net.Uri;
import com.google.gson.annotations.SerializedName;


public class Profile {
    @SerializedName("Image")
    private String imageUri;

    @SerializedName("Name")
    private String name;

    @SerializedName("Date")
    private String date;



    public Profile(String imageUri, String name, String date) {
        this.imageUri = imageUri;
        this.name = name;
        this.date = date;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

}


