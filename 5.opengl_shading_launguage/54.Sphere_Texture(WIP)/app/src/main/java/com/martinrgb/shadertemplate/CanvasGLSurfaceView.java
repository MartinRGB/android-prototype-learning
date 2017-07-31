package com.martinrgb.shadertemplate;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

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
