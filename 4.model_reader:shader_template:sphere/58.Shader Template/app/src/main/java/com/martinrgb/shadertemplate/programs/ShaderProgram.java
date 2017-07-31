package com.martinrgb.shadertemplate.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.martinrgb.shadertemplate.util.ShaderHelper;
import com.martinrgb.shadertemplate.util.TextResourceReader;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class ShaderProgram {

    //Uniform常量
    //protected static final String U_MATRIX = "u_Matrix";
    //protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    //protected static final String U_COLOR = "u_Color";

    //Attribute 常量
    protected static final String A_POSITION = "a_Position";
    protected static final String U_TIME = "u_time";
    protected static final String U_RESOLUTION = "u_resolution";
    protected static final String U_MOUSE = "u_mouse";
    //protected static final String A_COLOR = "a_Color";
    //protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId){

        //拿到着色器GLSL文件，用buildProgram构建Program
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResourceId));
    }

    public void useProgram(){
        GLES20.glUseProgram(program);
    }


}