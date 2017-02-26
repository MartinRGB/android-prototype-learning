package com.xiaomi.martinrgb.a25bos_uniforms_gradient.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.a25bos_uniforms_gradient.R;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleShaderProgram extends ShaderProgram {

    private final int aPositionLocation;
    private final int uTimeLocation;
    private final int uResolutionLocation;
    private final int uMouseLocation;

    public SimpleShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);

        uTimeLocation = GLES20.glGetUniformLocation(program,U_TIME);
        uResolutionLocation = GLES20.glGetUniformLocation(program,U_RESOLUTION);
        uMouseLocation = GLES20.glGetUniformLocation(program,U_MOUSE);
    }

    public void setUniforms(float time,float width,float height,float touchX,float touchY){
        GLES20.glUniform1f(uTimeLocation,time);
        GLES20.glUniform2f(uResolutionLocation,width,height);
        GLES20.glUniform2f(uMouseLocation,touchX,touchY);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
