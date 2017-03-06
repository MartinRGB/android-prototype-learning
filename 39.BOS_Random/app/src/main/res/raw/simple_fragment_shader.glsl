#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

//dot() 函数在这个例子中尤其有用。它根据两个向量的方向返回一个 0.0 到 1.0 之间的值。
float random (vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233)))*
        43758.5453123);
}

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

void main() {
    vec2 st = gl_FragCoord.xy/u_resolution.xy;

    //扩大十倍
    st *= 10.0; // Scale the coordinate system by 10
    //取整数
    vec2 ipos = floor(st);  // get the integer coords
    //取小数
    vec2 fpos = fract(st);  // get the fractional coords

    // Assign a random value based on the integer coord
    vec3 color = vec3(random( ipos ));

    // Uncomment to see the subdivided grid
    // color = vec3(fpos,0.0);

    gl_FragColor = vec4(color,1.0);
}