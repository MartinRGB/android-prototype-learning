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
    protected static final String A_VERTEX = "a_Vertex";
    protected static final String A_NORMAL = "a_Normal";

    protected static final String U_TIME = "u_time";
    protected static final String U_RESOLUTION = "u_resolution";
    protected static final String U_MOUSE = "u_mouse";

    protected static final String U_LIGHT_POSITION = "LightPosition";
    protected static final String U_MODEL_VIEW_MATRIX = "u_ModelViewMatrix";
    protected static final String U_NORMAL_MATRIX  = "u_NormalMatrix";
    protected static final String U_MODEL_VIEW_PROJECTION_MATRIX = "u_ProjectionMatrix";

    protected static final String U_BRICK_COLOR = "BrickColor";
    protected static final String U_MORTAR_COLOR = "MotarColor";
    protected static final String U_BRICK_SIZE = "BrickSize";
    protected static final String U_BRICK_PCT = "BrickPct";



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