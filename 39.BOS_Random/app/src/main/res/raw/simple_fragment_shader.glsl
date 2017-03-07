#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

//dot() 函数在这个例子中尤其有用。它根据两个向量的方向返回一个 0.0 到 1.0 之间的值。
//float random (vec2 st) {
//    return fract(sin(dot(st.xy,
//                         vec2(12.9898,78.233)))*
//        43758.5453123);
//}
//void main() {
//    vec2 st = gl_FragCoord.xy/u_resolution.xy;
//
//    float rnd = random( st );
//
//    vec3 color = vec3(st.x,st.y,0);
//	color +=  vec3(rnd);
//    //color += smoothstep(sin(u_time)*1.25,1.5,st.x);
//    gl_FragColor = vec4(color,1.0);
//}

//float random (vec2 st) {
//    return fract(sin(dot(st.xy,
//                         vec2(12.9898,78.233)))*
//        43758.5453123);
//}
//void main() {
//    vec2 st = gl_FragCoord.xy/u_resolution.xy;
//
//    //扩大十倍
//    st *= 10.0; // Scale the coordinate system by 10
//    //取整数
//    vec2 ipos = floor(st);  // get the integer coords
//    //取小数
//    vec2 fpos = fract(st);  // get the fractional coords
//
//    // Assign a random value based on the integer coord
//    vec3 color = vec3(random( ipos ));
//
//    // Uncomment to see the subdivided grid
//    // color = vec3(fpos,0.0);
//
//    gl_FragColor = vec4(color,1.0);
//}



float random (in float x) {
    return fract(sin(x)*1e4);
}

float random (in vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898,78.233)))* 43758.5453123);
}

float pattern(vec2 st, vec2 v, float t) {
    vec2 p = floor(st+v);
    return step(t, random(100.+p*.000001)+random(p.x)*0.5 );
}

void main() {
    vec2 st = gl_FragCoord.xy/u_resolution.xy;
    st.x *= u_resolution.x/u_resolution.y;

    vec2 grid = vec2(100.0,50.);
    st *= grid;

    vec2 ipos = floor(st);  // integer
    vec2 fpos = fract(st);  // fraction

    vec2 vel = vec2(u_time*2.*max(grid.x,grid.y)); // time
    vel *= vec2(-1.,0.0) * random(1.0+ipos.y); // direction

    // Assign a random value base on the integer coord
    //GlitchEffect
    vec2 offset = vec2(sin(u_time*2.),0.);

    vec3 color = vec3(0.);
    color.r = pattern(st+offset,vel,0.5+u_mouse.x/u_resolution.x);
    color.g = pattern(st,vel,0.5+u_mouse.x/u_resolution.x);
    color.b = pattern(st-offset,vel,0.5+u_mouse.x/u_resolution.x);

    // Margins
    color *= step(0.5,fpos.y);

    gl_FragColor = vec4(1.0-color,1.0);
}