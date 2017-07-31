uniform mat4 u_Matrix;
attribute vec3 a_Position;
varying vec3 v_Position;

void main() {

    //顶点的位置传递给片段着色器
    v_Position = a_Position;
    //右手坐标空间变为左手坐标空间
    v_Position.z = -v_Position.z;
    //把位置乘以矩阵投影到裁剪空间
    gl_Position = u_Matrix * vec4(a_Position,10);
    gl_Position = gl_Position.xyww;

}
