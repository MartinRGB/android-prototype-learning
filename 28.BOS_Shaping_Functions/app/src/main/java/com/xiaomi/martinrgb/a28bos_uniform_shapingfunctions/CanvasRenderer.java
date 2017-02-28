package com.xiaomi.martinrgb.a28bos_uniform_shapingfunctions;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xiaomi.martinrgb.a28bos_uniform_shapingfunctions.objects.SimpleColor;
import com.xiaomi.martinrgb.a28bos_uniform_shapingfunctions.programs.SimpleShaderProgram;

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
        GLES20.glClearColor(0.0f, 1.0f, 1.0f, 1.0f);

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

        simpleShaderProgram.useProgram();
        simpleShaderProgram.setUniforms(1080,1080);
        simpleColor.bindData(simpleShaderProgram);
        simpleColor.draw();
    }

}
