package com.xiaomi.martinrgb.a25bos_uniforms;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xiaomi.martinrgb.a25bos_uniforms.objects.SimpleColor;
import com.xiaomi.martinrgb.a25bos_uniforms.programs.SimpleShaderProgram;

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

        initTimeValue += incrementPerFrame;

        simpleShaderProgram.useProgram();
        simpleShaderProgram.setUniforms(initTimeValue);
        simpleColor.bindData(simpleShaderProgram);
        simpleColor.draw();
    }

    //Unifrom - u_Time;
    private float initTimeValue = clamp(0.0f,0.0f,1.0f);
    private float incrementPerFrame = 0.05f;
    //限定范围
    private float clamp(float value,float min,float max){
        return Math.min(max,Math.max(value,min));
    }

}
