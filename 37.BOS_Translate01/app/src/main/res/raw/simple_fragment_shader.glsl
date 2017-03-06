#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

#define PI 3.14159265359

mat2 rotate2d(float _angle){
    return mat2(cos(_angle),-sin(_angle),
                sin(_angle),cos(_angle));
}

mat2 scale(vec2 _scale){
    return mat2(_scale.x,0.0,
                0.0,_scale.y);
}

float box(in vec2 _st, in vec2 _size){
    _size = vec2(0.5) - _size*.5;
    vec2 uv = smoothstep(_size,
                        _size+vec2(0.001),
                        _st);
    //相并得此
    uv *= smoothstep(_size,
                    _size+vec2(0.001),
                    vec2(1.0)-_st);
    return uv.x*uv.y;
}

//十字架
float cross(in vec2 _st, float _size){
    return  box(_st, vec2(_size,_size/4.))+box(_st, vec2(_size/4.,_size));
}

void main(){
    vec2 st = gl_FragCoord.xy/u_resolution.xy;
    vec3 color = vec3(0.0);

    //###Rotate
    // 先移回原点
    st -= vec2(0.5);
    // 旋转
    st = rotate2d( sin(u_time)*PI ) * st;
    // 移动回来
    st += vec2(0.5);


    //###Scale
    st -= vec2(0.5);
        // 旋转
    st = scale(vec2(sin(u_time)*.1+0.5)) * st;
    // 移动回来
    st += vec2(0.5);

    //###Translate
    // To move the cross we move the space
    vec2 translate = vec2(cos(u_time),sin(u_time));
    // 半径缩减
    st += translate*0.15;


    //###Color
    // 背景增加颜色
     color = vec3(st.x,st.y,sin(u_time));

    // Add the shape on the foreground
    color += vec3(cross(st,0.25));

    gl_FragColor = vec4(color,1.0);
}