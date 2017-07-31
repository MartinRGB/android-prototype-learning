attribute vec4 a_Position;
attribute vec4 a_Color;

//varying 特殊变量 融合两个顶点之间片段的颜色，产生渐变
varying vec4 v_Color;

void main(){

    v_Color = a_Color;

    gl_Position = a_Position;
    //指定点的大小
    gl_PointSize = 10.0;
}