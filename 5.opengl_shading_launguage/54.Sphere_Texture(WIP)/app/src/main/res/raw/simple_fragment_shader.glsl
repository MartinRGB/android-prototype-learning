#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

const float ONE = 1.0;
const float HALF = 0.5;

//uniform vec3 BrickColor, MortarColor;
//uniform vec2 BrickSize, BrickPct;

varying float LightIntensity;
varying vec2 MCPosition;

void main()
{
    vec3 color = vec3(1.,0.5,0.5);
//    vec2 position, useBrick;
//
//    position = MCPosition / BrickSize;
//
//    if(fract(position.y * HALF) > HALF)
//        position.x += HALF ;
//
//    position = fract(position);
//
//    useBrick = step(position, BrickPct);
//
//    color = mix(MortarColor, BrickColor, useBrick.x * useBrick.y);
//    color *= LightIntensity;


    gl_FragColor = vec4(color, ONE);
    //gl_FragColor =vec4(0.,1.,0.,1.);

    //gl_FragColor = vec4(gl_FragCoord.x/u_resolution.x,gl_FragCoord.y/u_resolution.y,0.5,1.);

}