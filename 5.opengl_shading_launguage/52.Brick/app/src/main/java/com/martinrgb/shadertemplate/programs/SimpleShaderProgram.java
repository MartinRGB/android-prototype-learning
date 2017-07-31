package com.martinrgb.shadertemplate.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.martinrgb.shadertemplate.R;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleShaderProgram extends ShaderProgram {

    private final int aVertexLocation;


    private final int aNormalLocation;

    private final int uLightPositionLocation;
    private final int uModelViewMatirx;
    private final int uNormalMatirx;
    private final int uProjectionMatrix;

    private final int uBrickColor;
    private final int uMotarColor;
    private final int uBrickSize;
    private final int uBrickPct;


    private final int uTimeLocation;
    private final int uMouseLocation;
    private final int uResolutionLocation;

    public SimpleShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader);
        aVertexLocation = GLES20.glGetAttribLocation(program,A_VERTEX);
        aNormalLocation = GLES20.glGetAttribLocation(program,A_NORMAL);

        uTimeLocation = GLES20.glGetUniformLocation(program,U_TIME);
        uMouseLocation = GLES20.glGetUniformLocation(program,U_MOUSE);
        uResolutionLocation = GLES20.glGetUniformLocation(program,U_RESOLUTION);

        uLightPositionLocation = GLES20.glGetUniformLocation(program,U_LIGHT_POSITION);
        uModelViewMatirx =  GLES20.glGetUniformLocation(program,U_MODEL_VIEW_MATRIX);
        uNormalMatirx = GLES20.glGetUniformLocation(program,U_NORMAL_MATRIX);
        uProjectionMatrix = GLES20.glGetUniformLocation(program,U_MODEL_VIEW_PROJECTION_MATRIX);

        uBrickColor = GLES20.glGetUniformLocation(program,U_BRICK_COLOR);
        uMotarColor = GLES20.glGetUniformLocation(program,U_MORTAR_COLOR);
        uBrickSize = GLES20.glGetUniformLocation(program,U_BRICK_SIZE);
        uBrickPct = GLES20.glGetUniformLocation(program,U_BRICK_PCT);
    }

    public void setUniforms(float time,float width,float height,float mouseX,float mouseY,float[] modelMatrix,float[] normalMatrix,float[] projectionMatrix){
        GLES20.glUniform1f(uTimeLocation,time);
        GLES20.glUniform2f(uResolutionLocation,width,height);
        GLES20.glUniform2f(uMouseLocation,mouseX,mouseY);

        GLES20.glUniformMatrix4fv(uModelViewMatirx,1,false,modelMatrix,0);
        GLES20.glUniformMatrix3fv(uNormalMatirx,1,false,normalMatrix,0);

        GLES20.glUniformMatrix4fv(uProjectionMatrix,1,false,projectionMatrix,0);


        GLES20.glUniform3f(uLightPositionLocation,0.f,0.f,4.f);
        GLES20.glUniform3f(uBrickColor,1.f,0.3f,.2f);
        GLES20.glUniform3f(uMotarColor,0.85f,0.86f,0.84f);
        GLES20.glUniform2f(uBrickSize,0.3f,0.15f);
        GLES20.glUniform2f(uBrickPct,0.90f,0.85f);

    }

    public int getVertexAttributeLocation() {
        return aVertexLocation;
    }

    public int getNormalAttributeLocation() {return aNormalLocation;}

}
