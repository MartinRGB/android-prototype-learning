package com.xiaomi.martinrgb.airhockeytouch;

import android.content.Context;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Gravity;

import com.codemonkeylabs.fpslibrary.TinyDancer;
import com.xiaomi.martinrgb.airhockeytouch.objects.BackGround;
import com.xiaomi.martinrgb.airhockeytouch.objects.Mallet;
import com.xiaomi.martinrgb.airhockeytouch.objects.Puck;
import com.xiaomi.martinrgb.airhockeytouch.objects.Table;
import com.xiaomi.martinrgb.airhockeytouch.programs.AnimationShaderProgram;
import com.xiaomi.martinrgb.airhockeytouch.programs.ColorShaderProgram;
import com.xiaomi.martinrgb.airhockeytouch.programs.TextureShaderProgram;
import com.xiaomi.martinrgb.airhockeytouch.util.Geometry;
import com.xiaomi.martinrgb.airhockeytouch.util.MatrixHelper;
import com.xiaomi.martinrgb.airhockeytouch.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;

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


    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    private final int mTime = 20;
    private final int mResolution = 1000;

    private Table table;
    private Mallet mallet;
    private BackGround backGround;
    private Puck puck;

    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private AnimationShaderProgram animationProgram;
    private int texture;

    public AirHockeyRenderer(Context context) {
        this.context = context;
        //showFPS();
    }

    @Override public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        table = new Table();
        mallet = new Mallet(0.08f,0.15f,32);
        puck = new Puck(0.06f,0.02f,32);
        //backGround = new BackGround();

        puckPosition = new Geometry.Point(0f,puck.height/2f,0f);
        puckVector = new Geometry.Vector(0f,0f,0f);

        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        //animationProgram = new AnimationShaderProgram(context);

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);

        blueMalletPosition = new Geometry.Point(0f,mallet.height/2f,0.4f);


        //Matrix.setLookAtM(viewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);
    }

    @Override public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width,height);

        MatrixHelper.perspectiveM(projectionMatrix,45,(float)width/(float)height,1f,10f);

        Matrix.setLookAtM(viewMatrix,0,0f,1.2f,2.2f,0f,0f,0f,0f,1f,0f);

    }


    float mXRotate = 0f;
    float mYRotate = 0f;

    @Override public void onDrawFrame(GL10 gl10) {

        //冰球的速率
        puckPosition = puckPosition.translate(puckVector);

        //碰撞检测
        //如果超过了左边 或右边，反向回弹
        if (puckPosition.x < leftBound + puck.radius
                || puckPosition.x > rightBound - puck.radius) {
            puckVector = new Geometry.Vector(-puckVector.x, puckVector.y, puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }

        //如果超过了近点或者远点，反向回弹
        if (puckPosition.z < farBound + puck.radius
                || puckPosition.z > nearBound - puck.radius) {
            puckVector = new Geometry.Vector(puckVector.x, puckVector.y, -puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }
        // 限定范围
        puckPosition = new Geometry.Point(
                clamp(puckPosition.x, leftBound + puck.radius, rightBound - puck.radius),
                puckPosition.y,
                clamp(puckPosition.z, farBound + puck.radius, nearBound - puck.radius)
        );

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

//        animationProgram.useProgram();
//        animationProgram.setUniforms(projectionMatrix,0.5f,1,1);
//        backGround.bindData(animationProgram);
//        backGround.draw();

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        //反转矩阵，取消试图矩阵和投影矩阵
        Matrix.invertM(invertedViewProjectionmatrix,0,viewProjectionMatrix,0);

        positionTableInScene();
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix,texture);
        table.bindData(textureProgram);
        table.draw();

        //画两个Mallet
        positionObjectInScene(0f,mallet.height/2f,-0.4f);
        colorProgram.useProgram();
        colorProgram.setUniforms(modelViewProjectionMatrix,1f,0f,0f);
        mallet.bindData(colorProgram);
        mallet.draw();

        positionObjectInScene(blueMalletPosition.x,blueMalletPosition.y,blueMalletPosition.z);
        colorProgram.setUniforms(modelViewProjectionMatrix,0f,0f,1f);
        mallet.draw();


        //让冰球在每一帧上移动
        positionObjectInScene(puckPosition.x,puckPosition.y,puckPosition.z);
        colorProgram.setUniforms(modelViewProjectionMatrix,0.8f,0.8f,1f);
        puck.bindData(colorProgram);
        puck.draw();


        if(mXRotate < 6){
            mXRotate += 0.005;
        }
        else {
            mXRotate = 0;
        }

//        if(mYRotate <3){
//            mYRotate += 0.0005;
//        }
//        else {
//            mYRotate = 0;
//        }


        Matrix.setLookAtM(viewMatrix,0,mXRotate,1.2f,2.2f,0f,0f,0f,0f,1f,0f);

        //每一帧都缩放 0.99f速度分量
        puckVector = puckVector.scale(0.99f);


    }

    private void positionTableInScene(){
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.rotateM(modelMatrix,0,-90f,1f,0f,0f);
        //把viewProjectionMatrix和 modelMatrix合并到一起扔进modelViewProjectionmarix里面
        Matrix.multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
    }

    private void positionObjectInScene(float x,float y,float z){
        Matrix.setIdentityM(modelMatrix,0);
        Matrix.translateM(modelMatrix,0,x,y,z);
        //把viewProjectionMatrix和 modelMatrix合并到一起扔进modelViewProjectionmarix里面
        Matrix.multiplyMM(modelViewProjectionMatrix,0,viewProjectionMatrix,0,modelMatrix,0);
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

    private boolean malletPressed = false;
    private Geometry.Point blueMalletPosition;
    private Geometry.Point previousBlueMalletPosition;
    private final float[] invertedViewProjectionmatrix = new float[16];

    //用向量存储冰球的速度和方向
    private Geometry.Point puckPosition;
    private Geometry.Vector puckVector;

    public void handleTouchPress(float normalizedX, float normalizedY){

        Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX,normalizedY);

        //创造了一个球，包在木槌周围
        Geometry.Sphere malletBoundingSphere =new Geometry.Sphere(new Geometry.Point(
                blueMalletPosition.x,
                blueMalletPosition.y,
                blueMalletPosition.z),
                mallet.height/2);

        malletPressed = Geometry.intersects(malletBoundingSphere,ray);

        if(malletPressed == true){
            Log.e("TAG","碰到了");
        }
        else {
            Log.e("TAG","没有");


        }

    }
    //碰撞检测

    private final float leftBound = -0.5f;
    private final float rightBound = 0.5f;
    private final float farBound = -0.8f;
    private final float nearBound = 0.8f;


    //碰撞检测限定范围
    private float clamp(float value,float min,float max){
        return Math.min(max,Math.max(value,min));
    }


    public void handleTouchDrag(float normalizedX, float normalizedY){
        if(malletPressed){

            previousBlueMalletPosition = blueMalletPosition;

            //控制 Mallet 本身的移动
            Geometry.Ray ray = convertNormalized2DPointToRay(normalizedX,normalizedY);

            Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0,0,0),new Geometry.Vector(0,1,0));

            Geometry.Point touchedPoint = Geometry.intersectionPoint(ray,plane);
            //blueMalletPosition = new Geometry.Point(touchedPoint.x,mallet.height/2f,touchedPoint.z);
            //碰撞检测
            blueMalletPosition = new Geometry.Point(
                    clamp(touchedPoint.x, leftBound + mallet.radius, rightBound - mallet.radius),
                    mallet.height / 2f, clamp(touchedPoint.z, 0f + mallet.radius, nearBound - mallet.radius));

            //控制冰球的碰撞

            float distance = Geometry.vectorBetween(blueMalletPosition,puckPosition).length();
            //如果距离小于两个 radius，那么就是集中了
            if(distance < (puck.radius + mallet.radius)){
                puckVector = Geometry.vectorBetween(previousBlueMalletPosition,blueMalletPosition);
            }
        }

    }

    private Geometry.Ray convertNormalized2DPointToRay(float normalizedX, float normalizedY){
        //近远点矩阵
        final float[] nearPointNdc = {normalizedX,normalizedY,-1,1};
        final float[] farPointNdc = {normalizedX,normalizedY,1,1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];
        //获取转换为2维空间的矩阵
        Matrix.multiplyMV(nearPointWorld,0,invertedViewProjectionmatrix,0,nearPointNdc,0);
        Matrix.multiplyMV(farPointWorld,0,invertedViewProjectionmatrix,0,farPointNdc,0);

        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        //获取远近点
        Geometry.Point nearPointRay =
                new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPointRay =
                new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        return new Geometry.Ray(nearPointRay, Geometry.vectorBetween(nearPointRay, farPointRay));

    }

    //很多时候剔除了投影矩阵后，透视矩阵的 w 会被反转，所以要抵消一下
    private void divideByW(float[] vetctor){
        vetctor[0] /= vetctor[3];
        vetctor[1] /= vetctor[3];
        vetctor[2] /= vetctor[3];
        vetctor[3] /= vetctor[3];
    }



}