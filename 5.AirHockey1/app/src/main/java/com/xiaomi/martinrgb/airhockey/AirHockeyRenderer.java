package com.xiaomi.martinrgb.airhockey;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.xiaomi.martinrgb.airhockey.util.ShaderHelper;
import com.xiaomi.martinrgb.airhockey.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    //每一个顶点分量的数量
    private static final int POSITION_COMPONENT_COUNT = 2;

    //利用此改变内存分配方式，让OPENGL可以从 Dalvik环境中读取 tableVertices 数组
    //每个浮点占用4个字节
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private final Context context;

    public AirHockeyRenderer(Context context){
        this.context = context;
        //4顶点定义
        float[] tableVerticesWithTriangles = {
                //OPENGL只能绘制点 线 以及三角形
                //定义三角形要逆时针(卷曲顺序)，可以优化性能
                //数后面要加 f,否则判断为double
                //Tri1
                0f,0f,
                9f,14f,
                0f,14f,
                //Tri2
                0f,0f,
                9f,0f,
                9f,14f,
                //Line 1
                0f,7f,
                9f,7f,
                //Mallets
                4.5f,2f,
                4.5f,12f
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
        glClearColor(1,0,0,0);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.complieVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.complieFragmentShader(fragmentShaderSource);
    }
    //横竖屏切换和尺寸改变
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height){
        //窗口尺寸
        glViewport(0,0,width,height);
    };
    //每绘制一帧的时候，这个方法被 GLSurfaceView 调用
    @Override
    public void onDrawFrame(GL10 glUnused){
        //擦除屏幕上所有颜色，并用之前的glClearColor()调用定义颜色
        glClear(GL_COLOR_BUFFER_BIT);
    }

}