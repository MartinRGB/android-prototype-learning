#ifdef GL_ES
precision mediump float;
#endif

uniform vec2 u_resolution;

//根据XY的值，绘制一条线,移动最后的值，可以控制Y的移动
float plot(vec2 st,float pct){
    return smoothstep(pct- 0.02,pct,st.y-0.4) - smoothstep(pct,pct+0.02,st.y-0.4);
}

void main() {
    //把坐标系确定到 0 -> 1
    vec2 lst = gl_FragCoord.xy/vec2(u_resolution.x,u_resolution.y);
    vec2 st = vec2(lst.x,lst.y );

    //float y = st.x ;
    //pow() （求x的y次幂）是 GLSL 的一个原生函数，GLSL 有很多原生函数。大多数原生函数都是硬件加速的，会非常快
    float y = pow(st.x,5.0);

    vec3 color = vec3(y);

    //画线
    float pct = plot(st,y);

    color = (1.0 - pct) * color + pct*vec3(0.0,1.0,0.0);

    gl_FragColor = vec4(color,1.0);

}