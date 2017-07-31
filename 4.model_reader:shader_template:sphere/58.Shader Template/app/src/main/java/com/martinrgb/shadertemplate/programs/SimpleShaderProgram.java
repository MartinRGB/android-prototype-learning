package com.martinrgb.shadertemplate.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.martinrgb.shadertemplate.R;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleShaderProgram extends ShaderProgram {

    private final int aPositionLocation;
    private final int uTimeLocation;
    private final int uMouseLocation;
    private final int uResolutionLocation;

    public SimpleShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        uTimeLocation = GLES20.glGetUniformLocation(program,U_TIME);
        uMouseLocation = GLES20.glGetUniformLocation(program,U_MOUSE);
        uResolutionLocation = GLES20.glGetUniformLocation(program,U_RESOLUTION);
    }

    public void setUniforms(float time,float width,float height,float mouseX,float mouseY){
        GLES20.glUniform1f(uTimeLocation,time);
        GLES20.glUniform2f(uResolutionLocation,width,height);
        GLES20.glUniform2f(uMouseLocation,mouseX,mouseY);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
