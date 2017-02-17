package com.xiaomi.martinrgb.airhockeytextured.objects;

import android.opengl.GLES20;

import com.xiaomi.martinrgb.airhockeytextured.data.Constants;
import com.xiaomi.martinrgb.airhockeytextured.data.VertexArray;
import com.xiaomi.martinrgb.airhockeytextured.programs.ColorShaderProgram;

/**
 * Created by MartinRGB on 2017/2/17.
 */

public class Mallet {

    private static final int POSTION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSTION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {

            //X,Y,R,G,B
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    };

    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorShaderProgram){
        vertexArray.setVertexAttribPointer(
                0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSTION_COMPONENT_COUNT,
                STRIDE
        );

        vertexArray.setVertexAttribPointer(
                POSTION_COMPONENT_COUNT,
                colorShaderProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE
        );
    }

    public void draw(){
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,2);
    }



}
