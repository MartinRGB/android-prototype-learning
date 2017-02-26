package com.xiaomi.martinrgb.a25bos_hello_world.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.a25bos_hello_world.R;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleShaderProgram extends ShaderProgram {

    private final int aPositionLocation;

    public SimpleShaderProgram(Context context){
        super(context, R.raw.simple_vertex_shader,R.raw.simple_fragment_shader);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
    }

    public void setUniforms(){

    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
