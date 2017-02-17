package com.xiaomi.martinrgb.airhockeytextured;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Gravity;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.xiaomi.martinrgb.airhockeytextured.objects.BackGround;
import com.xiaomi.martinrgb.airhockeytextured.objects.Mallet;
import com.xiaomi.martinrgb.airhockeytextured.objects.Table;
import com.xiaomi.martinrgb.airhockeytextured.programs.AnimationShaderProgram;
import com.xiaomi.martinrgb.airhockeytextured.programs.ColorShaderProgram;
import com.xiaomi.martinrgb.airhockeytextured.programs.TextureShaderProgram;
import com.xiaomi.martinrgb.airhockeytextured.util.LoggerConfig;
import com.xiaomi.martinrgb.airhockeytextured.util.MatrixHelper;
import com.xiaomi.martinrgb.airhockeytextured.util.ShaderHelper;
import com.xiaomi.martinrgb.airhockeytextured.util.TextResourceReader;
import com.xiaomi.martinrgb.airhockeytextured.util.TextureHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
//
//    private final float[] modelMatrix = new float[16];
//
//    //每个浮点占用4个字节
//    private static final int BYTES_PER_FLOAT = 4;
//
//    //每一个顶点位置分量的数量
//    private static final int POSITION_COMPONENT_COUNT = 4;
//    //创建常量来容纳 OpenGL 程序中位置位置的变量
//    private static final String A_POSITION = "a_Position";
//    private int aPositionLocation;
//
//    //每一个顶点颜色分量的数量
//    private static final String A_COLOR = "a_Color";
//    private static final int COLOR_COMPONENT_COUNT = 3;
//    private int aColorLocation;
//
//    //计算使用的字节量
//    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
//
//    //利用此改变内存分配方式，让OPENGL可以从 Dalvik环境中读取 tableVertices 数组
//    private final FloatBuffer vertexData;
//    private final Context context;
//    private int program;
//
//    //旋转矩阵
//    private static final String U_MATRIX = "u_Matrix";
//    private final float[] projectionMatrix = new float[16];
//    private int uMatixLocation;
//
//    public boolean touchHasDetected = false;
//    private int mViewPortWidth;
//    private int mViewPortHeight;
//
//    public AirHockeyRenderer(Context context){
//        this.context = context;
//        //4顶点定义
//        float[] tableVerticesWithTriangles = {
//                //Order of coordinates:X,Y,Z,W,R,G,B
//
//                //OPENGL只能绘制点 线 以及三角形
//                //定义三角形要逆时针(卷曲顺序)，可以优化性能
//                //数后面要加 f,否则判断为double
//                //无论是x还是y OpenGL 都会将屏幕映射到 [-1,1] (百分比) 的范围内。所以我们要将之前定义的坐标做一些改变。
//                //Table
//                0f,0f,0f,1.5f,1f,1f,1f,
//                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//                0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//                0.5f, 0.8f,0f,2f,0.7f,0.7f,0.7f,
//                -0.5f, 0.8f,0f,2f,0.7f,0.7f,0.7f,
//                -0.5f,-0.8f,0f,1f,0.7f,0.7f,0.7f,
//                //Line 1
//                -0.5f,0f,0f,1.5f,1f,0f,0f,
//                0.5f,0f,0f,1.5f,1f,0f,0f,
//                //Mallets
//                0f,-0.4f,0f,1.25f,0f,0f,1f,
//                0f,0.4f,0f,1.75f,1f,0f,0f
//
//
//        };
//
//        //每个浮点4个字节，根据数组数量，分配相应大小本地内存，不受垃圾回收控制
//        //按照从左往右顺序读取
//        //asFloatBuffer得到反应底层字节的FloatBuffer实例
//        vertexData = ByteBuffer
//                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
//                .order(ByteOrder.nativeOrder())
//                .asFloatBuffer();
//
//        vertexData.put(tableVerticesWithTriangles);
//
//        showFPS();
//
//    }
//
//    //创建
//    @Override
//    public void onSurfaceCreated(GL10 glUnused, EGLConfig config){
//        //清空屏幕颜色
//        GLES20.glClearColor(0,0,0,0);
//
//        //读GLSL文件
//        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_vertex_shader);
//        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);
//
//        //编译构建顶点和片段着色器
//        int vertexShader = ShaderHelper.complieVertexShader(vertexShaderSource);
//        int fragmentShader = ShaderHelper.complieFragmentShader(fragmentShaderSource);
//
//        //链接顶点着色器和片段着色器，构建程序对象
//        program = ShaderHelper.linkProgram(vertexShader,fragmentShader);
//
//        //验证 OPENGL
//        if(LoggerConfig.ON){
//            ShaderHelper.validateProgram(program);
//        }
//
//        //开始使用程序
//        GLES20.glUseProgram(program);
//
//        //查询颜色位置
//        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
//        //查询位置位置
//        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
//        //查询矩阵旋转的位置
//        uMatixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
//
//        //## 做一个指针，从vertextData中读取数据给APositionLocation
//        //在vertexData的缓冲区中 对应 a_Position的位置
//        //aPositionLoacation是被传入的属性位置，这里是位置属性位置
//        //POSITION_COMPONENT_COUNT指的是，多少个数组分量与一个顶点相关联，因为x y 坐标，所以传入两个
//        //GL FLOAT指的是数据类型
//        //STRIDE 是跨距的意思，告诉两组参数之间差多少位置
//
//        //定位在数组位置的开头处，从这个位置读取
//        vertexData.position(0);
//        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
//        //OpenGL从aPositionLocation从获取属性
//        GLES20.glEnableVertexAttribArray(aPositionLocation);
//
//        //## 做一个指针，从vertextData中读取数据给AColorPosition
//        vertexData.position(POSITION_COMPONENT_COUNT);
//        GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
//        GLES20.glEnableVertexAttribArray(aColorLocation);
//
//;
//    }
//    //横竖屏切换和尺寸改变
//    private float aspectRatio;
//    @Override
//    public void onSurfaceChanged(GL10 glUnused, int width, int height){
//        //窗口尺寸
//        glViewport(0,0,width,height);
//
//        //视锥体从 z = -1开始， z = -10结束;45度视野创建投影.
//        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
//
//        //矩阵返回值都保存在modelMatrix中
//        Matrix.setIdentityM(modelMatrix,0);
//        //Z轴方向移动负两个单位
//        Matrix.translateM(modelMatrix,0,0f,0f,-2.5f);
//        //旋转60度
//        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);
//
//        //multiplyMM() 这个方法将模型矩阵（移动矩阵），和投影矩阵相乘得到一个最终使用的矩阵 temp ，
//        //最后使用 System.arraycopy() 把结果存回 projectionMatrix 中。
//        final float[] temp = new float[16];
//        //矩阵与投影矩阵相乘，实现归一化坐标
//        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
//
//
//    };
//    //每绘制一帧的时候，这个方法被 GLSurfaceView 调用
//    @Override
//    public void onDrawFrame(GL10 glUnused){
//        //擦除屏幕上所有颜色，并用之前的glClearColor()调用定义颜色
//        glClear(GLES20.GL_COLOR_BUFFER_BIT);
//
//        //传递正交投影矩阵
//        glUniformMatrix4fv(uMatixLocation,1,false,projectionMatrix,0);
//
//        //## 绘制两个三角形
//        //第一个参数告诉，我们要画三角形，0代表从第0个位置也就是开头读取顶点，6代表读取六个顶点
//        glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
//        //## 绘制分割线
//        glDrawArrays(GLES20.GL_LINES,6,2);
//        //## 绘制木锤
//        glDrawArrays(GLES20.GL_POINTS,8,1);
//        glDrawArrays(GLES20.GL_POINTS,9,1);
//
//        //动画测试
////        if(mViewPortWidth > mViewPortHeight){
////
////            if(touchHasDetected){
////
////                Log.e("TAG","不中");
////                mSpring.setEndValue(1);
////                android.opengl.Matrix.orthoM(projectionMatrix,0,-aspectRatio*mScaleRatio,aspectRatio*mScaleRatio,-1f*mScaleRatio,1f*mScaleRatio,-1f,1f);
////            }
////            else {
////                mSpring.setEndValue(0);
////                android.opengl.Matrix.orthoM(projectionMatrix,0,-aspectRatio*mScaleRatio,aspectRatio*mScaleRatio,-1f*mScaleRatio,1f*mScaleRatio,-1f,1f);
////            }
////        } else {
////            Log.e("TAG","中");
////            if(touchHasDetected){
////                mSpring.setEndValue(1);
////                android.opengl.Matrix.orthoM(projectionMatrix,0,-1f*mScaleRatio,1f*mScaleRatio,-aspectRatio*mScaleRatio,aspectRatio*mScaleRatio,-1f,1f);
////            }
////            else{
////                mSpring.setEndValue(0);
////                android.opengl.Matrix.orthoM(projectionMatrix,0,-1f*mScaleRatio,1f*mScaleRatio,-aspectRatio*mScaleRatio,aspectRatio*mScaleRatio,-1f,1f);
////            }
////        }
//
//    }

    private final Context context;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final int mTime = 20;
    private final int mResolution = 1000;

    private Table table;
    private Mallet mallet;
    private BackGround backGround;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private AnimationShaderProgram animationProgram;
    private int texture;

    public AirHockeyRenderer(Context context) {
        this.context = context;
        showFPS();
    }

    @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        table = new Table();
        mallet = new Mallet();
        backGround = new BackGround();

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        animationProgram = new AnimationShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width,height);

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);

        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix,0,0f,0f,-2.5f);
        Matrix.rotateM(modelMatrix,0,-60f,1f,0f,0f);
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp,0,projectionMatrix,0,modelMatrix,0);
        System.arraycopy(temp,0,projectionMatrix,0,temp.length);

    }

    @Override public void onDrawFrame(GL10 gl10) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        animationProgram.useProgram();
        animationProgram.setUniforms(projectionMatrix,0.5f,1,1);
        backGround.bindData(animationProgram);
        backGround.draw();

        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix,texture);
        table.bindData(textureProgram);
        table.draw();

        colorProgram.useProgram();
        colorProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorProgram);
        mallet.draw();





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