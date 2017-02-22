package com.xiaomi.martinrgb.wallpaper.programs;

import android.content.Context;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.wallpaper.R;

/**
 * Created by MartinRGB on 2017/2/21.
 */

public class SkyboxShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uTextureLocation;

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    private final int aPositionLocation;

    public SkyboxShaderProgram(Context context){
        super(context, R.raw.skybox_vertex_shader,R.raw.skybox_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        uTextureLocation = GLES20.glGetUniformLocation(program,U_TEXTURE_UNIT);
        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
    }

    public void setUniforms(float[] matrix,int textureId){
        GLES20.glUniformMatrix4fv(uMatrixLocation,1,false,matrix,0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textureId);
        GLES20.glUniform1i(uTextureLocation,0);
    }


}
