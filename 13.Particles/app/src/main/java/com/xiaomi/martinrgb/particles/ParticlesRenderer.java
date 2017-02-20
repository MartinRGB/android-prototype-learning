package com.xiaomi.martinrgb.particles;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Gravity;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.xiaomi.martinrgb.particles.objects.ParticleEmitter;
import com.xiaomi.martinrgb.particles.objects.ParticleSystem;
import com.xiaomi.martinrgb.particles.programs.ParticleShaderProgram;
import com.xiaomi.martinrgb.particles.util.Geometry;
import com.xiaomi.martinrgb.particles.util.MatrixHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ParticlesRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;
    private ParticleEmitter redParticleEmitter;
    private ParticleEmitter greenParticleEmitter;
    private ParticleEmitter blueParticleEmitter;
    private long globalStartTime;

    private int particleNumCount = 100000;
    private Geometry.Vector particleDirection = new Geometry.Vector(0f, 0.5f, 0f);
    private float angleVarianceInDegrees = 5f;
    private float speedVariance = 1f;

    public ParticlesRenderer(Context context) {
        this.context = context;
        //showFPS();
    }

    @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //累加融合技术
        GLES20.glEnable(GLES20.GL_BLEND);
        //输出 = (GL_ONE * 愿片段)+(目标因子 *目标片段)
        GLES20.glBlendFunc(GLES20.GL_ONE,GLES20.GL_ONE);

        //实例化着色器编译程序,获取GLSL里面的属性
        particleProgram = new ParticleShaderProgram(context);
        //实例化粒子系统世界，设置粒子数目10000
        particleSystem = new ParticleSystem(particleNumCount);
        //用System.nanoTime获取当前系统时间，作为全局启动时间
        globalStartTime = System.nanoTime();
        //三喷泉

        redParticleEmitter = new ParticleEmitter(
                new Geometry.Point(-1f, 0f, 0f),
                particleDirection,
                Color.rgb(255, 50, 5),angleVarianceInDegrees,speedVariance);

        greenParticleEmitter = new ParticleEmitter(
                new Geometry.Point(0f, 0f, 0f),
                particleDirection,
                Color.rgb(25, 255, 25),angleVarianceInDegrees,speedVariance);

        blueParticleEmitter = new ParticleEmitter(
                new Geometry.Point(1f, 0f, 0f),
                particleDirection,
                Color.rgb(5, 50, 255),angleVarianceInDegrees,speedVariance);
    }

    @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width,height);

        MatrixHelper.perspectiveM(projectionMatrix,45,(float) width/(float) height,1f,10f);
        Matrix.setIdentityM(viewMatrix,0);
        Matrix.translateM(viewMatrix,0,0f,-1.5f,-5f);
        //Matrix.setLookAtM(viewMatrix,0,0f,1.2f,4f,0f,0f,0f,0f,1f,0f);
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

    }

    float mXRotate = 0f;
    float mYRotate = 0f;
    float mZRotate = 5f;

    private long mLastTime = 0;
    private int fps = 0, ifps = 0;

    @Override public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //计算当前时间,因为nanoTime纳米时间
        float currentTime = (float) (System.nanoTime() - globalStartTime)/1000000000f;

        redParticleEmitter.addParticles(particleSystem, currentTime, 20);
        //Log.e("红色",String.valueOf(redParticleEmitter.EmitterParticlesCount));
        greenParticleEmitter.addParticles(particleSystem, currentTime, 20);
        //Log.e("绿色",String.valueOf(greenParticleEmitter.EmitterParticlesCount));
        blueParticleEmitter.addParticles(particleSystem, currentTime, 20);
        //Log.e("蓝色",String.valueOf(blueParticleEmitter.EmitterParticlesCount));

//        if(mXRotate < 6){
//            mXRotate += 0.025;
//        }
//        else {
//            mXRotate = 0;
//        }
//
//        if(mYRotate < 2){
//            mYRotate += 0.025;
//        }
//        else {
//            mYRotate = 0;
//        }

//        if(mZRotate < 8){
//            mZRotate += 0.025;
//        }
//        else {
//            mZRotate = 5f;
//        }


        particleProgram.useProgram();;
        //告诉着色器粒子移动了多远
        particleProgram.setUniforms(viewProjectionMatrix,currentTime);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();


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


    private void showFPS() {
        TinyDancer.create().show(context);
        TinyDancer.create()
                .redFlagPercentage(.1f) // set red indicator for 10%
                .startingGravity(Gravity.TOP)
                .startingXPosition(200)
                .startingYPosition(600)
                .show(context);
    }

}