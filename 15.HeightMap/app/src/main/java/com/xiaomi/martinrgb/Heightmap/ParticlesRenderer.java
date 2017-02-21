package com.xiaomi.martinrgb.Heightmap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.xiaomi.martinrgb.Heightmap.objects.Heightmap;
import com.xiaomi.martinrgb.Heightmap.objects.ParticleEmitter;
import com.xiaomi.martinrgb.Heightmap.objects.ParticleSystem;
import com.xiaomi.martinrgb.Heightmap.objects.Skybox;
import com.xiaomi.martinrgb.Heightmap.programs.HeightmapShaderProgram;
import com.xiaomi.martinrgb.Heightmap.programs.ParticleShaderProgram;
import com.xiaomi.martinrgb.Heightmap.programs.SkyboxShaderProgram;
import com.xiaomi.martinrgb.Heightmap.util.Geometry;
import com.xiaomi.martinrgb.Heightmap.util.MatrixHelper;
import com.xiaomi.martinrgb.Heightmap.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ParticlesRenderer implements GLSurfaceView.Renderer {

    private final Context context;

    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];

    private final float[] modelMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private final float[] tempMatrix = new float[16];
    private final float[] viewMatrixForSkybox = new float[16];

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

    //Heightmap Part
    private HeightmapShaderProgram heightmapShaderProgram;
    private Heightmap heightmap;

    public ParticlesRenderer(Context context) {
        this.context = context;
    }

    @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {



        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        //剔除
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //### 高度图
        initHeightMap(context);

        //### 粒子系统
        initParticles(context);
        //### 天空盒子
        initSkybox(context);
    }

    @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width,height);
        MatrixHelper.perspectiveM(projectionMatrix,45,(float) width/(float) height,1f,100f);
        updateViewMatrices();
    }

    @Override public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        drawHeightmap();
        drawSkybox();
        drawParitcles();
        //打印FPS
        controllerFPS();
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
        skyboxTexture = TextureHelper.loadCubeMap(context,new int[]{R.drawable.left,R.drawable.right,
                R.drawable.bottom,R.drawable.top,
                R.drawable.front,R.drawable.back});
    }

    private void initHeightMap(Context context){
        heightmapShaderProgram = new HeightmapShaderProgram(context);
        heightmap = new Heightmap(
                ((BitmapDrawable) context.getResources().getDrawable(R.drawable.heightmap)).getBitmap());
    }

    private void updateViewMatrices() {
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        System.arraycopy(viewMatrix, 0, viewMatrixForSkybox, 0, viewMatrix.length);

        Matrix.translateM(viewMatrix,0,0,-1.5f,-5f);
    }

    private void updpateMvpMatrix(){
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
    }

    private void updateMvpMatrixForSkybox(){
        Matrix.multiplyMM(tempMatrix, 0, viewMatrixForSkybox, 0, modelMatrix, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
    }

    private void drawSkybox(){
        Matrix.setIdentityM(viewMatrix,0);
        updateMvpMatrixForSkybox();
        //首先旋转y轴，再旋转x轴 - FPS 样式
        Matrix.rotateM(viewMatrix,0,-yRotation,1f,0f,0f);
        Matrix.rotateM(viewMatrix,0,-xRotation,0f,1f,5f);
        Matrix.translateM(viewMatrix,0,0f,0f,0f);
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        //深度测试
        GLES20.glDepthFunc(GLES20.GL_LEQUAL);
        skyboxShaderProgram.useProgram();
        skyboxShaderProgram.setUniforms(viewProjectionMatrix,skyboxTexture);
        skybox.bindData(skyboxShaderProgram);
        skybox.draw();
        GLES20.glDepthFunc(GLES20.GL_LESS);
    }


    private void drawParitcles(){
        //计算当前时间,因为nanoTime纳米时间
        float currentTime = (float) (System.nanoTime() - globalStartTime)/1000000000f;

        redParticleEmitter.addParticles(particleSystem, currentTime, 20);
        greenParticleEmitter.addParticles(particleSystem, currentTime, 20);
        blueParticleEmitter.addParticles(particleSystem, currentTime, 20);

        Matrix.setIdentityM(modelMatrix,0);
        updpateMvpMatrix();
        //首先旋转y轴，再旋转x轴 - FPS 样式
        Matrix.rotateM(viewMatrix,0,-yRotation,1f,0f,0f);
        Matrix.rotateM(viewMatrix,0,-xRotation,0f,1f,0f);
        Matrix.translateM(viewMatrix,0,0f,-1.5f,mParticleZPosition);
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);

        //累加融合技术
        GLES20.glEnable(GLES20.GL_BLEND);
        //输出 = (GL_ONE * 愿片段)+(目标因子 *目标片段)
        GLES20.glBlendFunc(GLES20.GL_ONE,GLES20.GL_ONE);


        //深度缓冲
        GLES20.glDepthMask(false);
        particleProgram.useProgram();;
        //告诉着色器粒子移动了多远
        particleProgram.setUniforms(viewProjectionMatrix,currentTime,particleTexture);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();
        GLES20.glDepthMask(true);

        GLES20.glDisable(GLES20.GL_BLEND);
    }

    private void drawHeightmap() {
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.scaleM(modelMatrix,0,100f,10f,100f);
        updpateMvpMatrix();
        Matrix.translateM(viewMatrix,0,0f,0f,mSkyboxZPosition);
        Matrix.multiplyMM(viewProjectionMatrix,0,projectionMatrix,0,viewMatrix,0);
        heightmapShaderProgram.useProgram();
        heightmapShaderProgram.setUniforms(modelViewProjectionMatrix);
        heightmap.bindData(heightmapShaderProgram);
        heightmap.draw();
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