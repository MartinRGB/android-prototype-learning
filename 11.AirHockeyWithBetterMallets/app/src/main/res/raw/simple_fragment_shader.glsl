precision mediump float;
//变量颜色
varying vec4 v_Color;
//统一颜色
//uniform vec4 u_Color;

uniform vec4 u_Color;

void main(){
    gl_FragColor = u_Color;
}