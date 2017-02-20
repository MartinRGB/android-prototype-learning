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
   v_ElapsedTime = u_Time - a_ParticleStartTime;
   vec3 currentPosition = a_Position + (a_DirectionVector * v_ElapsedTime);
   gl_Position = u_Matrix * vec4(currentPosition,1.0);
   gl_PointSize = 10.0;
}
