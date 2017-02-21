package com.xiaomi.martinrgb.Heightmap.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.Heightmap.R;

/**
 * Created by MartinRGB on 2017/2/21.
 */

public class HeightmapShaderProgram extends ShaderProgram {

    private final int uMatrixLocation;
    private final int aPositionLocation;


    public int getaPositionAttributeLocation() {
        return aPositionLocation;
    }

    public HeightmapShaderProgram(Context context){
        super(context, R.raw.heightmap_vertex_shader,R.raw.heightmap_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
    }

    public void setUniforms(float[] matrix){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

//        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
//        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textureId);
//        GLES20.glUniform1i(uTextureLocation,0);
    }


}
