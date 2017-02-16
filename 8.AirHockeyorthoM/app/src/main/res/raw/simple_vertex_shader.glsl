attribute vec4 a_Position;
attribute vec4 a_Color;

//varying 特殊变量 融合两个顶点之间片段的颜色，产生渐变
varying vec4 v_Color;

//mat4 的意思是 4x4矩阵
uniform mat4 u_Matrix;

void main(){

    v_Color = a_Color;

    gl_Position = a_Position * u_Matrix;
    //指定点的大小
    gl_PointSize = 10.0;
}