// simple fragment shader

// 'time' contains seconds since the program was linked.
uniform float time;

const float ONE = 1.0;
const float HALF = 0.5;

uniform vec3 BrickColor,MortarColor;
uniform vec2 BrickSize,BrickPct;

varying vec2 MCposition;
varying float LightIntensity;

void main()
{
    vec3 color;
    vec2 position, useBrick;

    position = MCposition / BrickSize;

    if(fract(position.y * HALF) > HALF)
        position.x += HALF;

    position = fract(position);

    useBrick = step(position, BrickPct);

    color = mix(MortarColor, BrickColor, useBrick.x * useBrick.y);
    color *= LightIntensity;
    gl_FragColor = vec4(color, ONE);
}