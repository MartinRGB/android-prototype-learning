package com.xiaomi.martinrgb.particles.util;

import android.app.Application;

import com.codemonkeylabs.fpslibrary.FrameDataCallback;
import com.codemonkeylabs.fpslibrary.TinyDancer;

/**
 * Created by MartinRGB on 2017/2/16.
 */

public class DebugApplication extends Application {

    @Override public void onCreate() {
        TinyDancer.create()
                .show(this);

        //alternatively
        TinyDancer.create()
                .redFlagPercentage(.1f) // set red indicator for 10%
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