#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;
#define WAVES 3.0

float random (vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233)))*
        43758.5453123);
}

void main() {

    vec2 uvNorm = gl_FragCoord.xy/u_resolution.xy;
    vec2 uv = 2.0 * uvNorm - 1.;
    uv.y -= 0.7625;


    
    float sinTime = sin(u_time)/3.;
    float sintime2 = sin(u_time*3.)/10.;
    float sintime3 = 0.5 - sin(u_time)*4.5 ;
    float sintime4 = 1. - sin(u_time) * 0.6;
    float time = u_time * 10.3;

    vec4 color = vec4(0.0);
    vec3 colorLine = vec3(1.0, 1.0, 1.0);
    float epaisLine = 0.003;

    for(float i=0.0; i<WAVES; i++){
        float sizeDif = (i * 4.0);
        //colorLine = vec3(1.0 - (i*sintime2));

        colorLine = vec3(1.0 - (i*sintime2) + sinTime,1.0-(i*sintime2) + .5,1.0-(i*sintime2) - .2);


        //SiriWave
        float K = 4.0;
        float B = 5.0;
        float x = uv.x * 2.5 +sintime3 ;
        float att = (1.0 - (i*sintime2*sintime2)) * sinTime + sin((i+1.)*random(uv*u_time));
        float posOnde = uv.y + (att*pow((K/(K+pow(x, K))), K) * cos((B*x)-(time+(i*2.5))));

        //Line
        float difEpais = epaisLine + ((epaisLine/WAVES)*i);
        vec3 line = smoothstep( 0.0, sintime4, abs(epaisLine / posOnde)) * colorLine;
        color += vec4(line, smoothstep( 0.0, 1., abs(epaisLine / posOnde)) * colorLine );
    }

    gl_FragColor = color;

}