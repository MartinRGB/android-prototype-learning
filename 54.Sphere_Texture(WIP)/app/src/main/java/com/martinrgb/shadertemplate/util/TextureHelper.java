package com.martinrgb.shadertemplate.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_LINEAR_MIPMAP_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static android.opengl.GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGenerateMipmap;
import static android.opengl.GLES20.glTexParameteri;

/**
 * Created by MartinRGB on 2017/2/17.
 */

public class TextureHelper {
    private static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, int resourceId) {
        //材质ID
        final int[] textureObjectIds = new int[1];
        //创建了材质纹理对象
        glGenTextures(1, textureObjectIds, 0);
        //检查调用是否成功
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.");
            }
            return 0;
        };

        //OpenGL 不支持读取压缩图形格式如 JPEG PNG，所以要用Android内置解码器把图像压缩为OpenGL 所需格式
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        //图像解码
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resourceId,options);
        if(bitmap == null){
            if(LoggerConfig.ON){
                Log.w(TAG,"Resource ID" + resourceId + "could not be decoded.");
            }
            glDeleteTextures(1,textureObjectIds,0);
            return 0;
        }

        //二维纹理，对象ID;
        glBindTexture(GL_TEXTURE_2D,textureObjectIds[0]);

        //纹理扩大或缩小的时候，需要纹理过滤来告知，纹理元素挤压到更小的片段 - 缩小 ,纹理元素拓展到更大的 片段 - 放大
        //缩小 使用三线性过滤（更平滑）
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
        //放大 双线性过滤（解决了最临界过滤放大的锯齿效果）
        glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);

        //告诉OpenGL读取Bitmap，并且绑定到纹理对象上
        GLUtils.texImage2D(GL_TEXTURE_2D,0,bitmap,0);
        //生成MIP贴图
        glGenerateMipmap(GL_TEXTURE_2D);
        //纹理对象绑定了Bitmap之后，Bitmap 回收
        bitmap.recycle();
        //纹理加载成贴图后，解除纹理的绑定
        glBindTexture(GL_TEXTURE_2D,0);

        return textureObjectIds[0];
        

    }

    //加载盒子材质
    public static int loadCubeMap(Context context,int[] cubeResources){
        final int[] textureObjectIds = new int[1];
        GLES20.glGenTextures(1,textureObjectIds,0);

        if(textureObjectIds[0] == 0){
            if(LoggerConfig.ON){
                Log.w(TAG,"Could not generate a new OpenGl texture object.");
            }
            return 0;
        }
        //传进6个图像资源，每个资源对应立方体的一面
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap[] cubeBitmaps = new Bitmap[6];

        //把图像解码到内存里
        for (int i =0;i<6;i++){
            cubeBitmaps[i] = BitmapFactory.decodeResource(context.getResources(),cubeResources[i],options);

            if(cubeBitmaps[i] == null){
                if(LoggerConfig.ON){
                    Log.w(TAG,"Resource ID" + cubeResources[i] + "could not be decoded.");
                }
                glDeleteTextures(1,textureObjectIds,0);
                return 0;
            }
        }

        //配置纹理过滤器
        glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP,textureObjectIds[0]);
        glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
        glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);

        //把每张图像与对应的立方体贴图对上 左右上下前后
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X,0,cubeBitmaps[0],0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X,0,cubeBitmaps[1],0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,0,cubeBitmaps[2],0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y,0,cubeBitmaps[3],0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z,0,cubeBitmaps[4],0);
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z,0,cubeBitmaps[5],0);
        //一次支架在一个位图，加载到对应立方体面上，以便于重用
        glBindTexture(GL_TEXTURE_2D,0);
        //用完图像酒回收
        for(Bitmap bitmap:cubeBitmaps){
            bitmap.recycle();
        }

        return  textureObjectIds[0];

    }
}
