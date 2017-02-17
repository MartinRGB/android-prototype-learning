package com.xiaomi.martinrgb.airhockeytextured.programs;

/**
 * Created by MartinRGB on 2017/2/17.
 */

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.airhockeytextured.R;

public class AnimationShaderProgram extends ShaderProgram{

    private final int uMatrixLocation;
    private final int aPositionLocation;
    private final int aTextureCoordnatesLocation;

    private final int uTimeLocation;
    private final int uResolutionLocation;

    public AnimationShaderProgram(Context context){
        super(context, R.raw.animation_vertex_shader,R.raw.animation_fragment_shader);

        //从GLSL着色器文件中读取各种属性，利用父函数所定义的String值(U_MATRIX = "u_Matrix")
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aTextureCoordnatesLocation = GLES20.glGetAttribLocation(program,A_TEXTURE_COORDINATES);
        uTimeLocation = GLES20.glGetUniformLocation(program,TIME);
        uResolutionLocation = GLES20.glGetUniformLocation(program,RESOLUTION);
    }


    public void setUniforms(float[] matrix,float time,int width,int height){
        //传递正交投影矩阵
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES20.glUniform1f(uTimeLocation,time);
        GLES20.glUniform2i(uResolutionLocation,width,height);

    }

    public int getPositionAttributeLocation(){
        return aPositionLocation;
    }

    public int getaTextureCoordnatesLocation(){
        return aTextureCoordnatesLocation;
    }

}