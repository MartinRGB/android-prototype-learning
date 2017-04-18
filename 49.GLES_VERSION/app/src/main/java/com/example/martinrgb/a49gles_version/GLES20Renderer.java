package com.example.martinrgb.a49gles_version;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.ContentValues.TAG;
import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.glGetShaderiv;

/**
 * Created by MartinRGB on 2017/4/17.
 */

public class GLES20Renderer implements GLSurfaceView.Renderer {

    private Context mContext;

    public GLES20Renderer(Context context){
        mContext = context;
    }

    private int _arrowProgram;
    private FloatBuffer _VFBarrow;
    private ShortBuffer _ISBarrow;
    private int[] _arrowBuffers = new int[2];

    private float[] _RMatrix = new float[16];

    private int _arrowAPositionLocation;
    private int _arrowUMVPLocation;
    private static volatile float _zAngle;

    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        GLES20.glClearColor(0.f,0.f,0.f,1);
    }
    public void onSurfaceChanged(GL10 gl,int width, int height){
        GLES20.glViewport(0,0,width,height);
        initArrow();

        //创建程序
        _arrowProgram = GLES20.glCreateProgram();
        //编译 Shader
        GLES20.glAttachShader(_arrowProgram, compileShader(GLES20.GL_VERTEX_SHADER,readTextFileFromResource(mContext,R.raw.arrow_vertex)));
        GLES20.glAttachShader(_arrowProgram, compileShader(GLES20.GL_FRAGMENT_SHADER,readTextFileFromResource(mContext,R.raw.arrow_fragment)));
        //链接程序
        GLES20.glLinkProgram(_arrowProgram);

        //设置 attribute 和 uniform 的地址
        _arrowAPositionLocation = GLES20.glGetAttribLocation(_arrowProgram, "aPosition");
        _arrowUMVPLocation = GLES20.glGetUniformLocation(_arrowProgram, "uMVP");
        Matrix.setIdentityM(_RMatrix, 0);
    }
    public void onDrawFrame(GL10 gl){
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(_RMatrix, 0);
        Matrix.rotateM(_RMatrix, 0, _zAngle, 0, 0, 1);

        //使用程序
        GLES20.glUseProgram(_arrowProgram);
        //注入矩阵
        GLES20.glUniformMatrix4fv(_arrowUMVPLocation, 1, false, _RMatrix, 0);

        //绑定缓冲区对象
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _arrowBuffers[0]);
        GLES20.glVertexAttribPointer(_arrowAPositionLocation, 3, GLES20.GL_FLOAT, false, 12, 0);
        GLES20.glEnableVertexAttribArray(_arrowAPositionLocation);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, _arrowBuffers[1]);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_SHORT, 0);
        System.gc();
        logFrameRate();
    }


    public static void setZAngle(float angle) {
        GLES20Renderer._zAngle = angle;
    }
    public static float getZAngle() {
        return GLES20Renderer._zAngle;
    }

    private void initArrow(){
            float[] arrowVFA = {
                    0.000467f,0.205818f,-0.000364f,
                    0.026045f,-0.113806f,-0.020301f,
                    -0.026045f,-0.113806f,-0.020301f,
                    -0.000467f,0.205818f,-0.000364f,
                    0.000467f,0.205818f,0.000364f,
                    0.026045f,-0.113806f,0.020301f,
                    -0.026045f,-0.113806f,0.020301f,
                    -0.000467f,0.205818f,0.000364f,
            };

            short[] arrowISA = {
                    0,1,3,
                    4,7,6,
                    0,4,1,
                    1,5,2,
                    2,6,3,
                    4,0,7,
                    1,2,3,
                    5,4,6,
                    4,5,1,
                    5,6,2,
                    6,7,3,
                    0,3,7,
            };

            //分配内存
            ByteBuffer arrowVBB = ByteBuffer.allocateDirect(arrowVFA.length * 4);
            arrowVBB.order(ByteOrder.nativeOrder());
            _VFBarrow = arrowVBB.asFloatBuffer();
            _VFBarrow.put(arrowVFA);
            _VFBarrow.position(0);

            ByteBuffer arrowIBB = ByteBuffer.allocateDirect(arrowISA.length * 2);
            arrowIBB.order(ByteOrder.nativeOrder());
            _ISBarrow = arrowIBB.asShortBuffer();
            _ISBarrow.put(arrowISA);
            _ISBarrow.position(0);

            GLES20.glGenBuffers(2, _arrowBuffers, 0);
            GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, _arrowBuffers[0]);
            GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, arrowVFA.length * 4, _VFBarrow, GLES20.GL_STATIC_DRAW);
            GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, _arrowBuffers[1]);
            GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, arrowISA.length * 2, _ISBarrow, GLES20.GL_STATIC_DRAW);
    }


    //构建单个着色器对象
    private static int compileShader(int type,String shaderCode){

        //glCreateShader 构建了着色器对象，并把 ID 存入shaderObjectID
        final int shaderObjectId = GLES20.glCreateShader(type);

        //把着色器源代码传入着色器对象里
        GLES20.glShaderSource(shaderObjectId, shaderCode);
        //编译着色器
        GLES20.glCompileShader(shaderObjectId);

        //检测编译是否成功
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatus,0);

        //检测连接成功失败的日志

        return shaderObjectId;
    };

    public static String readTextFileFromResource(Context context, int resourceID){
        StringBuilder body = new StringBuilder();

        try{
            InputStream inputStream = context.getResources().openRawResource(resourceID);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;

            while((nextLine = bufferedReader.readLine()) != null){
                body.append(nextLine);
                body.append('\n');
            }
        } catch (IOException e){
            throw new RuntimeException("Could not open resource:" + resourceID,e);
        } catch (Resources.NotFoundException nfe){
            throw new RuntimeException("Resource not found:" + resourceID,nfe);
        }

        return body.toString();
    };

    private long startTimeMs;
    private int frameCount;
    private void logFrameRate(){
        long elapsedRealtimeMs = SystemClock.elapsedRealtime();
        double elapsedSeconds = (elapsedRealtimeMs - startTimeMs)/1000.0;

        if(elapsedSeconds >= 1.0){
            Log.v(TAG,frameCount/elapsedSeconds + "fps");
            startTimeMs = SystemClock.elapsedRealtime();
            frameCount = 0;
        }
        frameCount ++;
    }
}
