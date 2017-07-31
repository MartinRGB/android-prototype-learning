package com.xiaomi.martinrgb.a25bos_hello_world;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.widget.Toast;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class CanvasGLSurfaceView extends GLSurfaceView {
    private final Context context;

    //Constructor
    public CanvasGLSurfaceView(Context context) {
        super(context);
        this.context = context;
        //initGLSurfaceView(context);
    }

    public CanvasGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

}
