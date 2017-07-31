#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

const float ONE = 1.0;
const float HALF = 0.5;

uniform vec3 BrickColor, MortarColor;
uniform vec2 BrickSize, BrickPct;

varying float LightIntensity;
varying vec2 MCPosition;

//2
#define iterations 17
#define formuparam 0.53

#define volsteps 10
#define stepsize 0.1

#define zoom   0.800
#define tile   0.850
#define speed  0.010

#define brightness 0.0015
#define darkmatter 0.700
#define distfading 0.730
#define saturation 0.850

vec3 mainImage(in vec2 fragCoord )
{

    //get coords and direction
    vec2 uv=fragCoord.xy/u_resolution.xy-.5;
    uv.y*=u_resolution.y/u_resolution.x;
    vec3 dir=vec3(fragCoord.xy/1080.*zoom,1.);
    float time=u_time*speed+.25;

    //mouse rotation
    float a1=.5+u_mouse.x/u_resolution.x/100.;
    float a2=.8+u_mouse.y/u_resolution.y/100.;
    mat2 rot1=mat2(cos(a1),sin(a1),-sin(a1),cos(a1));
    mat2 rot2=mat2(cos(a2),sin(a2),-sin(a2),cos(a2));
    dir.xz*=rot1;
    dir.xy*=rot2;
    vec3 from=vec3(1.,0.5,0.5);
    from+=vec3(time*2.,time,-2.);
    from.xz*=rot1;
    from.xy*=rot2;

    //volumetric rendering
    float s=0.1,fade=1.5;
    vec3 v=vec3(0.);
    for (int r=0; r<volsteps; r++) {
        vec3 p=from+s*dir*.5;
        p = abs(vec3(tile)-mod(p,vec3(tile*1.5))); // tiling fold
        float pa,a=pa=0.;
        for (int i=0; i<iterations; i++) {
            p=abs(p)/dot(p,p)-formuparam; // the magic formula
            a+=abs(length(p)-pa); // absolute sum of average change
            pa=length(p);
        }
        float dm=max(0.,darkmatter); //dark matter
        a*=a*a; // add contrast
        if (r>10) fade*=1.- dm; // dark matter, don't render near
        //v+=vec3(dm,dm*.5,0.);
        v+=fade;
        v+=vec3(s/2.,s*s,s*s*s*s*4.9)*a*brightness*fade; // coloring based on distance
        fade*=distfading; // distance fading
        s+=stepsize;
    }
    v=mix(vec3(length(v)),v,saturation); //color adjust
    //fragColor = vec4(v*.01,1.);

    return v*0.01;
}

void main()
{
    vec3 color;
    vec2 position, useBrick;

    position = MCPosition / BrickSize;

    if(fract(position.y * HALF) > HALF)
        position.x += HALF ;

    position = fract(position);

    useBrick = step(position, BrickPct);

    color = mix(MortarColor, BrickColor, useBrick.x * useBrick.y);
    color *= LightIntensity;
//    gl_FragColor = vec4(mainImage(gl_FragCoord.xy)*LightIntensity, ONE);

    gl_FragColor = vec4(color, ONE);
    //gl_FragColor =vec4(0.,1.,0.,1.);


    //gl_FragColor = vec4(gl_FragCoord.x/u_resolution.x,gl_FragCoord.y/u_resolution.y,0.5,1.);

}