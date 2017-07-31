#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;


float impulse( float k, float x ){
    float h = k*x;
    return h*exp(1.0-h);
}

//根据XY的值，绘制一条线,移动最后的值，可以控制Y的移动
float plot(vec2 st,float pct){
    return smoothstep(pct- 0.01,pct,st.y) - smoothstep(pct,pct+0.01,st.y);
}

void main() {
    //把坐标系确定到 0 -> 1
    vec2 st = gl_FragCoord.xy/u_resolution;

    //float y = st.x ;
    //pow() （求x的y次幂）是 GLSL 的一个原生函数，GLSL 有很多原生函数。大多数原生函数都是硬件加速的，会非常快
    //float y = sin((st.x+u_time)*8.)/8. + .9;

    //float y =sin((sin(st.x)+sin(u_time))*8.)/8. + .9;
    //float y = sin((st.x+u_time)*8.)/8. + .9;
    //float y = abs(sin((st.x+u_time)*8.)/8.) + .9;
    //float y = fract(sin((st.x+u_time)*8.)/8.) + .9;

    //float y =sin((st.x+u_time)*8.)/8. + .9;
    //float y = mod(st.x,0.5) + .6;


    float y = impulse(12.,st.x)/2. + .6;

    vec3 color = vec3(y);

    //画线
    float pct = plot(st,y);

    color = (1.0 - pct) * color + pct*vec3(0.0,1.0,0.0);

    gl_FragColor = vec4(color,1.0);

}