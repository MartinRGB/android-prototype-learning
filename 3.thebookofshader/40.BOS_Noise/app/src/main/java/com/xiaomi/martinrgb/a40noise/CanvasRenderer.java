package com.xiaomi.martinrgb.a40noise;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.xiaomi.martinrgb.a40noise.objects.SimpleColor;
import com.xiaomi.martinrgb.a40noise.programs.SimpleShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class CanvasRenderer  implements GLSurfaceView.Renderer{
    //Constructor
    private final Context context;
    private SimpleColor simpleColor;
    private SimpleShaderProgram simpleShaderProgram;
    private static final String TAG = "CanvasRenderer";
    public CanvasRenderer(Context context) {
        this.context = context;
        setSpringSystem();
    }

    //Override
    private long globalStartTime;
    private float screenWidth;
    private float screenHeight;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig){
        GLES20.glClearColor(1.0f, 0.0f, 1.0f, 1.0f);
        simpleColor = new SimpleColor();
        simpleShaderProgram = new SimpleShaderProgram(context);
        globalStartTime = System.nanoTime();
    }
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height){
        GLES20.glViewport(0,0,width,height);
        screenWidth = (float) width;
        screenHeight = (float) height;
    }
    @Override
    public void onDrawFrame(GL10 gl10){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float currentTime = (float) (System.nanoTime() - globalStartTime)/1000000000f;
        simpleShaderProgram.useProgram();
        simpleShaderProgram.setUniforms(currentTime,screenWidth,screenWidth,(float) mSpringX.getCurrentValue(),(float) mSpringY.getCurrentValue());
        simpleColor.bindData(simpleShaderProgram);
        simpleColor.draw();
    }

    //Delegate - Touch Gesture
    private boolean hasPressed = false;
    public void handleTouchDown(float normalizedX,float normalizedY){
        Log.e(TAG,"Down");

        //Spring
        mSpringX.setEndValue(normalizedX);
        mSpringY.setEndValue(normalizedY);

        hasPressed = true;
    }

    public void handleTouchUp(float normalizedX,float normalizedY){
        Log.e(TAG,"Up");
        hasPressed = false;
    }

    public void handleTouchDrag(float normalizedX,float normalizedY){
        if(hasPressed){
            Log.e(TAG,"Drag");
            mSpringX.setEndValue(normalizedX);
            mSpringY.setEndValue(normalizedY);
            Log.e("ST - X",String.valueOf(normalizedX/1080));
            Log.e("ST - Y",String.valueOf(normalizedY/1920));
        }
    }

    //Utils - Springsytem
    private static final SpringConfig mconfig = SpringConfig.fromOrigamiTensionAndFriction(180, 20);
    private SpringSystem mSpringSystem;
    private Spring mSpringX;
    private Spring mSpringY;
    private void setSpringSystem() {
        mSpringSystem = SpringSystem.create();
        mSpringX = mSpringSystem.createSpring();
        mSpringX.setSpringConfig(mconfig);
        mSpringY = mSpringSystem.createSpring();
        mSpringY.setSpringConfig(mconfig);

    }

    //Utils - Clamp Value
    private float clamp(float value,float min,float max){
        return Math.min(max,Math.max(value,min));
    }


}
