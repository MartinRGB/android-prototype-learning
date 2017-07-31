precision highp float;
//变量颜色
varying vec4 v_Color;
//统一颜色
//uniform vec4 u_Color;

uniform float u_time;
uniform vec2 u_resolution;
uniform mat4 u_Matrix;

varying vec3 from;
varying vec3 dir;
varying vec3 v;

#define iterations 17
#define formuparam 0.53

#define volsteps 10
#define stepsize 0.1

#define tile   0.850

#define brightness 0.0015
#define darkmatter 0.700
#define distfading 0.730
#define saturation 0.850

void main(){





    //gl_FragColor = vec4(v*0.01,1.);
    gl_FragColor = v_Color;
}