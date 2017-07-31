package com.xiaomi.martinrgb.skybox;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Gravity;

import com.xiaomi.martinrgb.skybox.objects.ParticleEmitter;
import com.xiaomi.martinrgb.skybox.objects.ParticleSystem;
import com.xiaomi.martinrgb.skybox.objects.Skybox;
import com.xiaomi.martinrgb.skybox.programs.ParticleShaderProgram;
import com.xiaomi.martinrgb.skybox.programs.SkyboxShaderProgram;
import com.xiaomi.martinrgb.skybox.util.Geometry;
import com.xiaomi.martinrgb.skybox.util.MatrixHelper;
import com.xiaomi.martinrgb.skybox.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ParticlesRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    //Particle Part
    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;
    private ParticleEmitter redParticleEmitter;
    private ParticleEmitter greenParticleEmitter;
    private ParticleEmitter blueParticleEmitter;
    private int particleTexture;
    private long globalStartTime;

    //Particle Parameters
    private int particleNumCount = 100000;
    private Geometry.Vector particleDirection = new Geometry.Vector(0f, 0.5f, 0f);
    private float angleVarianceInDegrees = 5f;
    private float speedVariance = 1f;

    //Skybox Part
    private SkyboxShaderProgram skyboxShaderProgram;
    private Skybox skybox;
    private int skyboxTexture;

    public ParticlesRenderer(Context context) {
        this.context = context;
    }

    @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //### 粒子系统
        initParticles(context);
        //### 天空盒子
        initSkybox(context);
    }

    @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width,height);
        MatrixHelper.perspectiveM(projectionMatrix,45,(float) width/(float) height,1f,10f);
    }

    @Override public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawSkybox();
        drawParitcles();
        //打印FPS
        //controllerFPS();
    }

    private void initParticles(Context context){
        //### 粒子系统
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

        particleTexture = TextureHelper.loadTexture(context,R.drawable.particle_texture);
    }

    private void initSkybox(Context context){
        skyboxShaderProgram = new SkyboxShaderProgram(context);
        skybox = new Skybox();
//        skyboxTexture = TextureHelper.loadCubeMap(context,new int[]{R.drawable.left,R.drawable.right,
//                R.drawable.bottom,R.drawable.top, R.drawable.front,R.drawable.back});

        skyboxTexture = TextureHelper.loadCubeMap(context,new int[]{R.drawable.emptyroom_c00,R.drawable.emptyroom_c01,
                R.drawable.emptyroom_c02,R.drawable.emptyroom_c03, R.drawable.emptyroom_c04,R.drawable.emptyroom_c05});
    }

    private void drawSkybox(){
        Matrix.setIdentityM(viewMatrix,0);
        //首先旋转y轴，再旋转x轴 - FPS 样式
        Matrix.rotateM(viewMatrix,0,-yRotation,1f,0f,0f);
        Matrix.rotateM(viewMatrix,0,-xRotation,0f,1f,mSkyboxZPosition);
        Matrix.translateM(viewMatrix,0,0f,0f,0);
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
        skyboxShaderProgram.useProgram();
        skyboxShaderProgram.setUniforms(viewProjectionMatrix,skyboxTexture);
        skybox.bindData(skyboxShaderProgram);
        skybox.draw();
    }


    private void drawParitcles(){
        //计算当前时间,因为nanoTime纳米时间
        float currentTime = (float) (System.nanoTime() - globalStartTime)/1000000000f;

        redParticleEmitter.addParticles(particleSystem, currentTime, 20);
        greenParticleEmitter.addParticles(particleSystem, currentTime, 20);
        blueParticleEmitter.addParticles(particleSystem, currentTime, 20);

        Matrix.setIdentityM(viewMatrix,0);
        //首先旋转y轴，再旋转x轴 - FPS 样式
        Matrix.rotateM(viewMatrix,0,-yRotation,1f,0f,0f);
        Matrix.rotateM(viewMatrix,0,-xRotation,0f,1f,0f);
        Matrix.translateM(viewMatrix,0,0f,-1.5f,mParticleZPosition);
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        //累加融合技术
        GLES20.glEnable(GLES20.GL_BLEND);
        //输出 = (GL_ONE * 愿片段)+(目标因子 *目标片段)
        GLES20.glBlendFunc(GLES20.GL_ONE,GLES20.GL_ONE);


        particleProgram.useProgram();;
        //告诉着色器粒子移动了多远
        particleProgram.setUniforms(viewProjectionMatrix,currentTime,particleTexture);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();

        GLES20.glDisable(GLES20.GL_BLEND);
    }

    private float xRotation,yRotation;

    public void handleTouchDrag(float deltaX,float deltaY){
        xRotation += deltaX /16f;
        yRotation += deltaY /16f;

        if(yRotation < -90){
            yRotation = -90;
        }else if(yRotation > 90){
            yRotation = 90;
        }
    }

    private float mParticleZPosition = -5f;
    private float mSkyboxZPosition = 0f;

    public void handleVoiceUp(){
        mSkyboxZPosition += 0.1f;
        mParticleZPosition += 0.1f;

    }

    public void handleVoiceDown(){
        mSkyboxZPosition -= 0.1f;
        mParticleZPosition -= 0.1f;
    }

    //FPS
    private long mLastTime = 0;
    private int fps = 0, ifps = 0;
    private void controllerFPS() {

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