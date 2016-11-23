package com.example.martinrgb.censor_wallpaper;

import android.app.Application;
import android.view.Gravity;

import com.codemonkeylabs.fpslibrary.FrameDataCallback;
import com.codemonkeylabs.fpslibrary.TinyDancer;

/**
 * Created by MartinRGB on 2016/11/22.
 */

public class DebugApplication extends Application {

    @Override public void onCreate() {
        TinyDancer.create()
                .show(this);

        //alternatively
        TinyDancer.create()
                .redFlagPercentage(.1f) // set red indicator for 10%
                .startingGravity(Gravity.TOP)
                .startingXPosition(200)
                .startingYPosition(600)
                .show(this);

        //you can add a callback to get frame times and the calculated
        //number of dropped frames within that window
        TinyDancer.create()
                .addFrameDataCallback(new FrameDataCallback() {
                    @Override
                    public void doFrame(long previousFrameNS, long currentFrameNS, int droppedFrames) {
                        //collect your stats here
                    }
                })
                .show(this);
    }
}
