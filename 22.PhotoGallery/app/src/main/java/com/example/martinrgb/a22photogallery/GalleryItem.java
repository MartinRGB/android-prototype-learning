package com.example.martinrgb.a22photogallery;

import android.net.Uri;

/**
 * Created by MartinRGB on 2017/2/24.
 */

//Model
public class GalleryItem {
    private String mTitle;
    private String mId;
    private String mUrl;
    private String mOwner;

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmUrl() {
        return mUrl;
    }

    public String getmId() {
        return mId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOwner() {
        return mOwner;
    }

    public void setmOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public Uri getPhotoPageUri() {
        return Uri.parse("http://www.flickr.com/photos/")
                .buildUpon()
                .appendPath(mOwner)
                .appendPath(mId)
                .build();
    }


    @Override
    public String toString(){
        return mTitle;
    }


}
