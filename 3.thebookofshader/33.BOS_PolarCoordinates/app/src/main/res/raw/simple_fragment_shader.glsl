#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

#define TWO_PI 6.28318530718

vec3 hsb2rgb( in vec3 c ){
    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0,4.0,2.0),
                             6.0)-3.0)-1.0,
                     0.0,
                     1.0 );
    rgb = rgb*rgb*(3.0-2.0*rgb);
    return c.z * mix( vec3(1.0), rgb, c.y);
}


void main() {
    //Base Constructer

    //Ratio 16-9
    //vec2 st = gl_FragCoord.xy/u_resolution;
    //vec2 mouse = u_mouse/u_resolution;

    //Ratio 1:1
    vec2 st = vec2(gl_FragCoord.x/u_resolution.x, gl_FragCoord.y/u_resolution.y *1920.0f/1080.0f );
    vec2 mouse = vec2(u_mouse.x/u_resolution.x, u_mouse.y/u_resolution.y *1920.0f/1080.0f );

    vec3 color = vec3(.0);

    //向量，注意指向关系
    vec2 toCenter = mouse - st;
    //弧度，返回一个-PI 到 PI的值  除以 TWO_PI,得 -0.5 到 0.5,映射到0.0 到 1.0
    float angleInPI = atan(toCenter.y,toCenter.x);
    float angle = (angleInPI/TWO_PI) + 0.5;
    float radius = length(toCenter)*2.0;

    color = hsb2rgb(vec3(angle,radius*2.*sin(u_time) + 1.,1.0));

    gl_FragColor = vec4(color,1.0);
}

