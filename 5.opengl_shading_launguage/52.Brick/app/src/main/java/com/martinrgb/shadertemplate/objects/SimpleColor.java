package com.martinrgb.shadertemplate.objects;

import android.opengl.GLES20;

import com.martinrgb.shadertemplate.programs.SimpleShaderProgram;
import com.martinrgb.shadertemplate.util.Constants;
import com.martinrgb.shadertemplate.util.VertexArray;

import java.nio.ByteBuffer;

/**
 * Created by MartinRGB on 2017/2/26.
 */

public class SimpleColor {
    private static final int VERTEX_COMPONENT_COUNT = 4; //3
    private static final int NORMAL_COMPONENT_COUNT = 3; //3
    private static final int STRIDE = (VERTEX_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT  ) * Constants.BYTES_PER_FLOAT;
    private final VertexArray positionVertexArray;

    public SimpleColor() {

        positionVertexArray = new VertexArray(new float[] {

                //Position X Y Z W | Normal X Y Z

                0f,0f,0f,1.5f,  0f,0f,1.5f,
                -0.5f,-0.8f,0f,1f,  -0.5f,-0.8f,1f,
                0.5f,-0.8f,0f,1f,  0.5f,-0.8f,1f,
                0.5f,0.8f,0f,2f,  0.5f,0.8f,2f,
                -0.5f,0.8f,0f,2f,  -0.5f,0.8f,2f,
                -0.5f,-0.8f,0f,1f, -0.5f,-0.8f,1f
        });

    }

    public void bindData(SimpleShaderProgram simpleShaderProgram){

        //从着色器构建的程序中获取每一个属性的位置，
        //通过getPositionAttributeLocation把程序位置属性绑定到着色器位置属性上
        //通过getTextureCoordinatesAttributeLocation把程序纹理位置属性绑定到着色器属纹理置属性上
        positionVertexArray.setVertexAttribPointer(
                0,
                simpleShaderProgram.getVertexAttributeLocation(),
                VERTEX_COMPONENT_COUNT,
                STRIDE);


        positionVertexArray.setVertexAttribPointer(
                VERTEX_COMPONENT_COUNT,
                simpleShaderProgram.getNormalAttributeLocation(),
                NORMAL_COMPONENT_COUNT,
                STRIDE);

    }

    //根据位置和纹理绘制形状
    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
        //GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36 , GLES20.GL_UNSIGNED_BYTE, indexArray);
    }
}
