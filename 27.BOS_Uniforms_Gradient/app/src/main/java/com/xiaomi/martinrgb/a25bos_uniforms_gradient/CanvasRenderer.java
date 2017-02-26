package com.xiaomi.martinrgb.a25bos_uniforms_gradient;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.xiaomi.martinrgb.a25bos_uniforms_gradient.objects.SimpleColor;
import com.xiaomi.martinrgb.a25bos_uniforms_gradient.programs.SimpleShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class CanvasRenderer  implements GLSurfaceView.Renderer{
    //Constructor
    private final Context context;
    public CanvasRenderer(Context context) {
        this.context = context;
    }
    private SimpleColor simpleColor;
    private SimpleShaderProgram simpleShaderProgram;

    //Override
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig){
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        simpleColor = new SimpleColor();
        simpleShaderProgram = new SimpleShaderProgram(context);
    }
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height){
        GLES20.glViewport(0,0,width,height);
    }
    @Override
    public void onDrawFrame(GL10 gl10){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        initTimeValue += incrementPerFrame;
        simpleShaderProgram.useProgram();
        simpleShaderProgram.setUniforms(initTimeValue,1080.0f,1920.0f,touchX*1080.0f,1920.0f - touchY*1920);
        simpleColor.bindData(simpleShaderProgram);
        simpleColor.draw();
        controllerFPS();

    }

    //Unifrom - u_Time;
    private float initTimeValue = clamp(0.0f,0.0f,1.0f);
    private float incrementPerFrame = 0.01f;
    //限定范围
    private float clamp(float value,float min,float max){
        return Math.min(max,Math.max(value,min));
    }

    //Gesture
    private boolean hasPressed = false;
    private boolean hasDragged = false;
    private float touchX;
    private float touchY;
    private float prevTouchX;
    private float prevTouchY;
    public void handleTouchPress(float normalizedX, float normalizedY){
        hasPressed = !hasPressed;
        prevTouchX = touchX;
        prevTouchY = touchY;
        touchX = normalizedX;
        touchY = normalizedY;
        Log.e("TAG",String.valueOf(prevTouchX));
        Log.e("TAG",String.valueOf(touchX));
    }
    public void handleTouchDrag(float normalizedX, float normalizedY){
        hasDragged = !hasDragged;
        prevTouchX = touchX;
        prevTouchY = touchY;
        touchX = normalizedX;
        touchY = normalizedY;

    }

    //FPS Listener
    public interface onDrawFrameListener {
        public void getDrawFramePerSecond(int fps);
    }
    private onDrawFrameListener mOnDrawFrameListener = null;
    public void setOnDrawFrameListener(onDrawFrameListener onDrawFrameListener) {
        mOnDrawFrameListener = onDrawFrameListener;
    }
    private long mLastTime = 0;
    private int fps = 0, ifps = 0;
    private void controllerFPS() {
        long now = System.currentTimeMillis();
        //System.out.println("FPS:" + fps);
        mOnDrawFrameListener.getDrawFramePerSecond(fps);
        ifps++;
        if(now > (mLastTime + 1000)) {
            mLastTime = now;
            fps = ifps;
            ifps = 0;
        }
    }

}
