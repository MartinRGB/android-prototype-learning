package com.xiaomi.martinrgb.skybox.util;

/**
 * Created by MartinRGB on 2017/2/16.
 */

public class MatrixHelper {
    public static void perspectiveM(float[] m,float yFovInDegrees,float aspect,float n ,float f){
        //计算焦距
        //yFovInDegrees 视野 radians 弧度角
        //把视野从度转换成弧度
        final float angleInradians = (float)(yFovInDegrees * Math.PI / 180);
        //焦距 = 1/tan(视野/2);
        final float a = (float) (1.0/Math.tan(angleInradians/2.0));

        //aspect 高宽比 f到远处平米的距离(必须正值，且大于到进处平面的值)，n到近处平面的距离(其为绝对值后取得正值）
        //标准投影矩阵
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}
