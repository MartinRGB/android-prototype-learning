package com.xiaomi.martinrgb.airhockey.util;

import android.util.Log;

import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glCreateShader;

/**
 * Created by MartinRGB on 2017/2/16.
 */

//glsl 着色器从文件中取出后，需要编译
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int complieVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }

    public static int complieFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }

    private static int compileShader(int type,String shaderCode){

        //glCreateShader 构建了着色器对象，并把 ID 存入shaderObjectID
        final int shaderObjectId = glCreateShader(type);

        if(shaderObjectId == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not create new shader");
            }
            return  0;
        }
    };

    public static int linkProgram(int vertexShaderId,int FragmentShaderId){

    }
}
