package com.example.martinrgb.a22photogallery;

/**
 * Created by MartinRGB on 2017/2/24.
 */

//Model
public class GalleryItem {
    private String mTitle;
    private String mId;
    private String mUrl;

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


    @Override
    public String toString(){
        return mTitle;
    }


}
