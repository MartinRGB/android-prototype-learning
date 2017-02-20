uniform mat4 u_Matrix;
uniform float u_Time;

attribute vec3 a_Position;
attribute vec3 a_Color;
attribute vec3 a_DirectionVector;
attribute float a_ParticleStartTime;

varying vec3 v_Color;
varying float v_ElapsedTime;



void main() {

   //颜色发给片段着色器
   v_Color = a_Color;
   //计算粒子从创建后运行了多少时间，发送给着色器
   v_ElapsedTime = u_Time - a_ParticleStartTime;
   //重力因素
   float gravityFactor = v_ElapsedTime * v_ElapsedTime / 8.0;
   //开始位置 + 速度*时间 = 粒子当前位置
   vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);
   currentPosition.y -= gravityFactor;
   //粒子的矩阵投影
   gl_Position = u_Matrix * vec4(currentPosition,1.0);
   //点大小
   //gl_PointSize = 10.0;
   gl_PointSize = 20.0;


}
