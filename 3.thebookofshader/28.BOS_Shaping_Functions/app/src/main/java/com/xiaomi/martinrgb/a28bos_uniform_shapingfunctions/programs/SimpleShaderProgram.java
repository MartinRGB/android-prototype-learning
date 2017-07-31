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

    public SimpleShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        uResolutionLocation = GLES20.glGetUniformLocation(program,U_RESOLUTION);
    }

    public void setUniforms(float width,float height){
        GLES20.glUniform2f(uResolutionLocation,width,height);

    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
