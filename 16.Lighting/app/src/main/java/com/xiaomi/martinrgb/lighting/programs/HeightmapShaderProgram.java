package com.xiaomi.martinrgb.lighting.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.lighting.R;
import com.xiaomi.martinrgb.lighting.util.Geometry;

/**
 * Created by MartinRGB on 2017/2/21.
 */

public class HeightmapShaderProgram extends ShaderProgram {

    private final int uMatrixLocation;
    private final int aPositionLocation;


    private final int uVectorToLightLocation;
    private final int aNormalLocation;

    public int getaPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getaNormalAttributeLocation() {
        return aNormalLocation;
    }

    public HeightmapShaderProgram(Context context){
        super(context, R.raw.heightmap_vertex_shader,R.raw.heightmap_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        uVectorToLightLocation = GLES20.glGetUniformLocation(program,U_VECTOR_TO_LIGHT);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aNormalLocation = GLES20.glGetAttribLocation(program,A_NORMAL);
    }

    public void setUniforms(float[] matrix,Geometry.Vector vectorToLight){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);
        GLES20.glUniform3f(uVectorToLightLocation,vectorToLight.x,vectorToLight.y,vectorToLight.z);

//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textureId);
//        GLES20.glUniform1i(uTextureLocation,0);
    }


}
