package com.martinrgb.shadertemplate;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.provider.Settings;
import android.util.Log;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.martinrgb.shadertemplate.objects.SimpleColor;
import com.martinrgb.shadertemplate.programs.SimpleShaderProgram;
import com.martinrgb.shadertemplate.util.MatrixHelper;

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


    private final float[] modelMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] normalMatrix = new float[16];
    private final float[] tempMatrix = new float[16];

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
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        simpleShaderProgram = new SimpleShaderProgram(context);
        simpleColor = new SimpleColor();
        globalStartTime = System.nanoTime();
    }
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height){
        GLES20.glViewport(0,0,width,height);
        screenWidth = (float) width;
        screenHeight = (float) height;
        //MatrixHelper.perspectiveM(modelViewProjectionMatrix,45,(float) width/(float) height,1f,10f);

        //Matrix.orthoM(modelViewProjectionMatrix,0,-1f,1f,-screenHeight/screenWidth,screenHeight/screenWidth,-1f,1f);

        //观察角
        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);

        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix,0,0f,0f,-3f);
        Matrix.rotateM(modelMatrix,0,45f,1f,0f,0f);

        final float[] temp = new float[16];
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);




    }
    @Override
    public void onDrawFrame(GL10 gl10){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT  | GLES20.GL_DEPTH_BUFFER_BIT); //
        float currentTime = (float) (System.nanoTime() - globalStartTime)/1000000000f;


        //反转
        Matrix.invertM(tempMatrix, 0, modelMatrix, 0);
        Matrix.transposeM(normalMatrix, 0, tempMatrix, 0);
        //设置归一化变量
        simpleShaderProgram.useProgram();
        simpleShaderProgram.setUniforms(currentTime,screenWidth,screenHeight,(float) mSpringX.getCurrentValue(),(float) mSpringY.getCurrentValue(),modelMatrix,normalMatrix,projectionMatrix);
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
