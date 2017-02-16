package com.xiaomi.martinrgb.airhockeyorthom;

import android.content.Context;
import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.xiaomi.martinrgb.airhockeyorthom.util.LoggerConfig;
import com.xiaomi.martinrgb.airhockeyorthom.util.ShaderHelper;
import com.xiaomi.martinrgb.airhockeyorthom.util.TextResourceReader;

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

    //每个浮点占用4个字节
    private static final int BYTES_PER_FLOAT = 4;

    //每一个顶点位置分量的数量
    private static final int POSITION_COMPONENT_COUNT = 2;
    //创建常量来容纳 OpenGL 程序中位置位置的变量
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    //每一个顶点颜色分量的数量
    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private int aColorLocation;

    //计算使用的字节量
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;

    //利用此改变内存分配方式，让OPENGL可以从 Dalvik环境中读取 tableVertices 数组
    private final FloatBuffer vertexData;
    private final Context context;
    private int program;

    //旋转矩阵
    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatixLocation;


    public AirHockeyRenderer(Context context){
        this.context = context;
        //4顶点定义
        float[] tableVerticesWithTriangles = {
                //Order of coordinates:X,Y,R,G,B

                //OPENGL只能绘制点 线 以及三角形
                //定义三角形要逆时针(卷曲顺序)，可以优化性能
                //数后面要加 f,否则判断为double
                //无论是x还是y OpenGL 都会将屏幕映射到 [-1,1] (百分比) 的范围内。所以我们要将之前定义的坐标做一些改变。
                //Table
                0f,0f,1f,1f,1f,
                -0.5f,-0.88f,0.7f,0.7f,0.7f,
                0.5f,-0.88f,0.7f,0.7f,0.7f,
                0.5f, 0.88f,0.7f,0.7f,0.7f,
                -0.5f, 0.88f,0.7f,0.7f,0.7f,
                -0.5f,-0.88f,0.7f,0.7f,0.7f,
                //Line 1
                -0.5f,0f,1f,0f,0f,
                0.5f,0f,1f,0f,0f,
                //Mallets
                0f,-0.44f,0f,0f,1f,
                0f,0.44f,1f,0f,0f

        };

        //每个浮点4个字节，根据数组数量，分配相应大小本地内存，不受垃圾回收控制
        //按照从左往右顺序读取
        //asFloatBuffer得到反应底层字节的FloatBuffer实例
        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);

    }

    //创建
    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config){
        //清空屏幕颜色
        GLES20.glClearColor(0,0,0,0);

        //读GLSL文件
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);

        //编译构建顶点和片段着色器
        int vertexShader = ShaderHelper.complieVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.complieFragmentShader(fragmentShaderSource);

        //链接顶点着色器和片段着色器，构建程序对象
        program = ShaderHelper.linkProgram(vertexShader,fragmentShader);

        //验证 OPENGL
        if(LoggerConfig.ON){
            ShaderHelper.validateProgram(program);
        }

        //开始使用程序
        GLES20.glUseProgram(program);

        //查询颜色位置
        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
        //查询位置位置
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        //查询矩阵旋转的位置
        uMatixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);

        //## 做一个指针，从vertextData中读取数据给APositionLocation
        //在vertexData的缓冲区中 对应 a_Position的位置
        //aPositionLoacation是被传入的属性位置，这里是位置属性位置
        //POSITION_COMPONENT_COUNT指的是，多少个数组分量与一个顶点相关联，因为x y 坐标，所以传入两个
        //GL FLOAT指的是数据类型
        //STRIDE 是跨距的意思，告诉两组参数之间差多少位置

        //定位在数组位置的开头处，从这个位置读取
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
        //OpenGL从aPositionLocation从获取属性
        GLES20.glEnableVertexAttribArray(aPositionLocation);

        //## 做一个指针，从vertextData中读取数据给AColorPosition
        vertexData.position(POSITION_COMPONENT_COUNT);
        GLES20.glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GLES20.GL_FLOAT,false,STRIDE,vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
;
    }
    //横竖屏切换和尺寸改变
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height){
        //窗口尺寸
        glViewport(0,0,width,height);

        final float aspectRatio = width > height?
                (float) width / (float) height :
                (float) height  / (float) width;

        //orthoM(float[]m,int mOffset,float left,float right,float bottom,float top,float near,float far)
        //float[]m目标数组，存贮正交投影
        //mOffset，矩阵开始便宜值
        if(width > height){
            android.opengl.Matrix.orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
        } else {
            android.opengl.Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
        }
    };
    //每绘制一帧的时候，这个方法被 GLSurfaceView 调用
    @Override
    public void onDrawFrame(GL10 glUnused){
        //擦除屏幕上所有颜色，并用之前的glClearColor()调用定义颜色
        glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //传递正交投影矩阵
        glUniformMatrix4fv(uMatixLocation,1,false,projectionMatrix,0);

        //## 绘制两个三角形
        //第一个参数告诉，我们要画三角形，0代表从第0个位置也就是开头读取顶点，6代表读取六个顶点
        glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
        //## 绘制分割线
        glDrawArrays(GLES20.GL_LINES,6,2);
        //## 绘制木锤
        glDrawArrays(GLES20.GL_POINTS,8,1);
        glDrawArrays(GLES20.GL_POINTS,9,1);

    }

}