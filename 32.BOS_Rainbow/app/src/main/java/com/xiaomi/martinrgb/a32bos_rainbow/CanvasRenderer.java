package com.xiaomi.martinrgb.a32bos_rainbow;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xiaomi.martinrgb.a32bos_rainbow.objects.SimpleColor;
import com.xiaomi.martinrgb.a32bos_rainbow.programs.SimpleShaderProgram;

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

    private long globalStartTime;
    //Override
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig){
        GLES20.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);

        simpleColor = new SimpleColor();
        simpleShaderProgram = new SimpleShaderProgram(context);
        globalStartTime = System.nanoTime();
    }
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height){
        GLES20.glViewport(0,0,width,height);
    }



    @Override
    public void onDrawFrame(GL10 gl10){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        float currentTime = (float) (System.nanoTime() - globalStartTime)/1000000000f;

        //Log.e("TIME",String.valueOf(currentTime));

        simpleShaderProgram.useProgram();
        simpleShaderProgram.setUniforms(currentTime,1080,1080);
        simpleColor.bindData(simpleShaderProgram);
        simpleColor.draw();

        showFPS();

    }

    private long mLastTime = 0;
    private int fps = 0, ifps = 0;
    private void showFPS(){
        //打印FPS

        long now = System.currentTimeMillis();
        System.out.println("FPS:" + fps);
        ifps++;
        if(now > (mLastTime + 1000)) {
            mLastTime = now;
            fps = ifps;
            ifps = 0;
        }
    }

}
