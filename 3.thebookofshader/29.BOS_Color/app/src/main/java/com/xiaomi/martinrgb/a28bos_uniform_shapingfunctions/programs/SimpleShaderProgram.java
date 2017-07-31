package com.xiaomi.martinrgb.a28bos_uniform_shapingfunctions.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.a28bos_uniform_shapingfunctions.R;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleShaderProgram extends ShaderProgram {

    private final int aPositionLocation;
    private final int uResolutionLocation;
    private final int uTimeLocation;

    public SimpleShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader2);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        uResolutionLocation = GLES20.glGetUniformLocation(program,U_RESOLUTION);
        uTimeLocation = GLES20.glGetUniformLocation(program,U_TIME);
    }

    public void setUniforms(float time,float width,float height){
        GLES20.glUniform2f(uResolutionLocation,width,height);
        GLES20.glUniform1f(uTimeLocation,time);

    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
