#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;



//void main() {
//    //Base Constructer
//
//    //Ratio 16-9
//    vec2 st = gl_FragCoord.xy/u_resolution;
//    vec2 mouse = u_mouse/u_resolution;
//
//    //Ratio 1:1
////    vec2 st = vec2(gl_FragCoord.x/u_resolution.x, gl_FragCoord.y/u_resolution.y *1920.0f/1080.0f );
////    vec2 mouse = vec2(u_mouse.x/u_resolution.x, u_mouse.y/u_resolution.y *1920.0f/1080.0f );
//
//    vec3 color = vec3(.0);
//
//    //bottom-left
//    vec2 bl = step(vec2(0.1),st);
//    //* 相当于 && ，表示 AND
//    float pct = bl.x * bl.y;
//
//    //top-right
//    vec2 tr = step(vec2(0.1),1.0-st);
//    //* 相当于 && ，表示 AND
//    pct *= tr.x * tr.y;
//
//    color = vec3(pct);
//
//    gl_FragColor = vec4(color,1.0);
//}


void main() {
    //Base Constructer

    //Ratio 16-9
    vec2 st = gl_FragCoord.xy/u_resolution;
    vec2 mouse = u_mouse/u_resolution;

    vec3 color = vec3(.0);

    //bottom-left
    //smoothstep塑形，这里第一个值越逼近第二个值越不模糊，第二个值代表宽度，第三个值位移
    float bottom = smoothstep(st.y-0.199*sin(u_time),st.y-0.2,.02*sin(u_time));
    float left = smoothstep(st.x-0.199*sin(u_time),st.x-0.2,.02*sin(u_time));
    //* 相当于 && ，表示 AND
    float pct = left * bottom;

    //top-right
	float top = smoothstep(st.y-1.0 - 0.199*sin(u_time),st.y-0.800,.02*sin(u_time));
    float right = smoothstep(st.x- 1.0 - .199*sin(u_time),st.x-0.800,.02*sin(u_time));

    pct *= right * top;

    color = vec3(pct);

    gl_FragColor = vec4(color,1.0);
}