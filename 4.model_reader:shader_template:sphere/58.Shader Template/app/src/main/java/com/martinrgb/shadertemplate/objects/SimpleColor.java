package com.martinrgb.shadertemplate.objects;

import android.opengl.GLES20;

import com.martinrgb.shadertemplate.programs.SimpleShaderProgram;
import com.martinrgb.shadertemplate.util.Constants;
import com.martinrgb.shadertemplate.util.VertexArray;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleColor {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private final VertexArray vertexArray;

    private static final float[] VERTEX_DATA = {
            //Order of coordinates: X, Y
            // Triangle Fan
            0f, 0f,
            -1f, -1f,
            1f, -1f,
            1f, 1f,
            -1f, 1f,
            -1f, -1f
    };

    public SimpleColor() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(SimpleShaderProgram simpleShaderProgram){

        //从着色器构建的程序中获取每一个属性的位置，
        //通过getPositionAttributeLocation把程序位置属性绑定到着色器位置属性上
        //通过getTextureCoordinatesAttributeLocation把程序纹理位置属性绑定到着色器属纹理置属性上
        vertexArray.setVertexAttribPointer(
                0,
                simpleShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

    }

    //根据位置和纹理绘制形状
    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
    }
}
