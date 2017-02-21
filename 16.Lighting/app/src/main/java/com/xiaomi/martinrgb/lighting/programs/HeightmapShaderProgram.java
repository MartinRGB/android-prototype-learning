package com.xiaomi.martinrgb.lighting.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.lighting.R;
import com.xiaomi.martinrgb.lighting.util.Geometry;

/**
 * Created by MartinRGB on 2017/2/21.
 */

public class HeightmapShaderProgram extends ShaderProgram {

    private final int aPositionLocation;


    private final int uVectorToLightLocation;

    public int getaNormalAttributeLocation() {
        return aNormalLocation;
    }

    private final int aNormalLocation;

    private final int uMVMatrixLocation;
    private final int uIT_MVMatrixLocation;
    private final int uMVPMatrixLocation;
    private final int uPointLightPositionsLocation;
    private final int uPointLightColorsLocation;

    public int getaPositionAttributeLocation() {
        return aPositionLocation;
    }

    public HeightmapShaderProgram(Context context){
        super(context, R.raw.heightmap_vertex_shader,R.raw.heightmap_fragment_shader);


        uVectorToLightLocation = GLES20.glGetUniformLocation(program, U_VECTOR_TO_LIGHT);
        uMVMatrixLocation = GLES20.glGetUniformLocation(program, U_MV_MATRIX);
        uIT_MVMatrixLocation = GLES20.glGetUniformLocation(program, U_IT_MV_MATRIX);
        uMVPMatrixLocation = GLES20.glGetUniformLocation(program, U_MVP_MATRIX);

        uPointLightPositionsLocation =
                GLES20.glGetUniformLocation(program, U_POINT_LIGHT_POSITIONS);
        uPointLightColorsLocation =
                GLES20.glGetUniformLocation(program, U_POINT_LIGHT_COLORS);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aNormalLocation = GLES20.glGetAttribLocation(program, A_NORMAL);
    }

    public void setUniforms(float[] mvMatrix,
                            float[] it_mvMatrix,
                            float[] mvpMatrix,
                            float[] vectorToDirectionalLight,
                            float[] pointLightPositions,
                            float[] pointLightColors){

        GLES20.glUniformMatrix4fv(uMVMatrixLocation, 1, false, mvMatrix, 0);
        GLES20.glUniformMatrix4fv(uIT_MVMatrixLocation, 1, false, it_mvMatrix, 0);
        GLES20.glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);
        GLES20.glUniform3fv(uVectorToLightLocation, 1, vectorToDirectionalLight, 0);
        GLES20.glUniform4fv(uPointLightPositionsLocation, 3, pointLightPositions, 0);
        GLES20.glUniform3fv(uPointLightColorsLocation, 3, pointLightColors, 0);

//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textureId);
//        GLES20.glUniform1i(uTextureLocation,0);
    }


}
