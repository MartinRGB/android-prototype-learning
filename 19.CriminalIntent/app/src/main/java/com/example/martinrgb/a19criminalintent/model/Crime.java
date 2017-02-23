package com.example.martinrgb.a19criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by MartinRGB on 2017/2/23.
 */

public class Crime {

    private String mTitle;
    private UUID mID;
    private Date mDate;
    private boolean mSolved;

    //Constructor
    public Crime(){
        //使用随机ID
        mID = UUID.randomUUID();
        //使用当前日期
        mDate = new Date();
    }

    //Getter & Setter
    public UUID getID() {
        return mID;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isSolved() {
        return mSolved;
    }

}
