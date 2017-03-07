#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

// 2D Random

float random(in vec2 st){
    return fract(sin(dot(st.xy,vec2(12.9898,78.233)))*43758.5453123);
}

//2D Noise

float noise(in vec2 st){
    vec2 i = floor(st);
    vec2 f = fract(st);

    //4 corners
    float a = random(i);
    float b = random(i + vec2(1.,0.));
    float c = random(i + vec2(0.,1.));
    float d = random(i + vec2(1.,1.));

    //Cubic Curve Function,same as smoothstep()

    vec2 u = f*f*(3.-2.*f);
    //u = smoothstep(0.,1.,f);

    // 2D 中，除了在一条线的两点（fract(x) 和 fract(x)+1.0）中插值，我们将在一个平面上的方形的四角（fract(st)，
    // fract(st)+vec2(1.,0.)， fract(st)+vec2(0.,1.) 和 fract(st)+vec2(1.,1.)）中插值。
     return mix(a, b, u.x) +
                (c - a)* u.y * (1.0 - u.x) +
                (d - b) * u.x * u.y;

}

//Gradient Random
vec2 random2(vec2 st){
    st = vec2( dot(st,vec2(127.1,311.7)),
              dot(st,vec2(269.5,183.3)) );
    return -1.0 + 2.0*fract(sin(st)*43758.5453123);
}

//Gradient Noise
float noise2(vec2 st) {
    vec2 i = floor(st);
    vec2 f = fract(st);

    vec2 u = f*f*(3.0-2.0*f);

    return mix( mix( dot( random2(i + vec2(0.0,0.0) ), f - vec2(0.0,0.0) ),
                     dot( random2(i + vec2(1.0,0.0) ), f - vec2(1.0,0.0) ), u.x),
                mix( dot( random2(i + vec2(0.0,1.0) ), f - vec2(0.0,1.0) ),
                     dot( random2(i + vec2(1.0,1.0) ), f - vec2(1.0,1.0) ), u.x), u.y);
}

void main() {
    vec2 st = gl_FragCoord.xy/u_resolution.xy;

    // Scale the coordinate system to see
    // some noise in action
    vec2 pos = vec2(st*15.0);

    // Use the noise function
    float n = noise(pos)*.5 + .5;

    gl_FragColor = vec4(vec3(n), 1.0);
}