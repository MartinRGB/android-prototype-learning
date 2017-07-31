package com.xiaomi.martinrgb.a25bos_uniforms.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.a25bos_uniforms.R;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleShaderProgram extends ShaderProgram {

    private final int aPositionLocation;
    private final int uTimeLocation;

    public SimpleShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        uTimeLocation = GLES20.glGetUniformLocation(program,U_TIME);
    }

    public void setUniforms(float time){
        GLES20.glUniform1f(uTimeLocation,time);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
