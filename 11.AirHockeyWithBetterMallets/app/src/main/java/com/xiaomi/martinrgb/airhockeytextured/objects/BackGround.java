package com.xiaomi.martinrgb.airhockeytextured.objects;

import android.opengl.GLES20;

import com.xiaomi.martinrgb.airhockeytextured.data.Constants;
import com.xiaomi.martinrgb.airhockeytextured.data.VertexArray;
import com.xiaomi.martinrgb.airhockeytextured.programs.AnimationShaderProgram;
import com.xiaomi.martinrgb.airhockeytextured.programs.TextureShaderProgram;

/**
 * Created by MartinRGB on 2017/2/17.
 */

public class BackGround {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private final VertexArray vertexArray;

    private static final float[] VERTEX_DATA = {
            //Order of coordinates: X, Y, S, T
            // 桌子的的 x y 位置，以及 S T 纹理坐标
            // Triangle Fan

            0f, 0f, 0.5f, 0.5f,
            -0.5f, -0.8f, 0f, 0.9f,
            0.5f, -0.8f, 1f, 0.9f,
            0.5f, 0.8f, 1f, 0.1f,
            -0.5f, 0.8f, 0f, 0.1f,
            -0.5f, -0.8f, 0f, 0.9f

    };

    public BackGround(){
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(AnimationShaderProgram animationProgram){

        //从着色器构建的程序中获取每一个属性的位置，
        //通过getPositionAttributeLocation把程序位置属性绑定到着色器位置属性上
        //通过getTextureCoordinatesAttributeLocation把程序纹理位置属性绑定到着色器属纹理置属性上
        vertexArray.setVertexAttribPointer(
                0,
                animationProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);

        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                animationProgram.getaTextureCoordnatesLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    //根据位置和纹理绘制形状
    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN,0,6);
    }

}
