#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;
//uniform sampler2D u_backbuffer;

#define M_PI 3.1415926535897932384626433832795


vec3 mod289(vec3 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec2 mod289(vec2 x) { return x - floor(x * (1.0 / 289.0)) * 289.0; }
vec3 permute(vec3 x) { return mod289(((x*34.0)+1.0)*x); }

float random (in vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233)))
                * 43758.5453123);
}

float snoise(vec2 v) {
    const vec4 C = vec4(0.211324865405187,  // (3.0-sqrt(3.0))/6.0
                        0.366025403784439,  // 0.5*(sqrt(3.0)-1.0)
                        -0.577350269189626,  // -1.0 + 2.0 * C.x
                        0.024390243902439); // 1.0 / 41.0
    vec2 i  = floor(v + dot(v, C.yy) );
    vec2 x0 = v -   i + dot(i, C.xx);
    vec2 i1;
    i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
    vec4 x12 = x0.xyxy + C.xxzz;
    x12.xy -= i1;
    i = mod289(i); // Avoid truncation effects in permutation
    vec3 p = permute( permute( i.y + vec3(0.0, i1.y, 1.0 ))
        + i.x + vec3(0.0, i1.x, 1.0 ));

    vec3 m = max(0.5 - vec3(dot(x0,x0), dot(x12.xy,x12.xy), dot(x12.zw,x12.zw)), 0.0);
    m = m*m ;
    m = m*m ;
    vec3 x = 2.0 * fract(p * C.www) - 1.0;
    vec3 h = abs(x) - 0.5;
    vec3 ox = floor(x + 0.5);
    vec3 a0 = x - ox;
    m *= 1.79284291400159 - 0.85373472095314 * ( a0*a0 + h*h );
    vec3 g;
    g.x  = a0.x  * x0.x  + h.x  * x0.y;
    g.yz = a0.yz * x12.xz + h.yz * x12.yw;
    return 130.0 * dot(m, g);
}

float level(vec2 st) {
    float n = 0.0;
    for (float i = 1.0; i < 8.0; i ++) {
        float m = pow(2.0, i);
        n += snoise(st * m) * (1.0 / m);
    }
    return n * 0.5 + 0.5;
}

vec3 normal(vec2 st) {
    float d = 0.001;
    float l0 = level(st);
    float l1 = level(st + vec2(d, 0.0)); // slightly offset the x-coord
    float l2 = level(st + vec2(0.0, d)); // slightly offset the y-coord
    // return normalized vector perpendicular to the surface using the noise values as the elevation of these points
    return normalize(vec3(-(l1 - l0), -(l2 - l0), d));
}




float hash( float n )
{
    return fract(sin(n)*43758.5453);
}

void main()
{
    
    vec2 st = gl_FragCoord.xy / u_resolution.xy;
    st.x *= u_resolution.x / u_resolution.y;
    
    
	float mx = max( u_resolution.x, u_resolution.y );
	vec2 uv = (gl_FragCoord.xy - u_resolution.xy*0.5)/mx;
    float r = 0.5;

    //Rotate
	uv *= mat2(
	r, -r,
	r, r );

    //Single
	float y = mx*(uv.x)*0.05 + u_time*2. + hash(uv.x/100000000.) ;//+ u_time
    //Lightness
	float f = 0.7;

    //Section
	vec3 color =
		vec3(
			mod( y + uv.x , 3. )*f,
			mod( y , 0.2)*f,
			mod( y*10. + uv.x*2., 0.9 )*f )*
			abs(fract(-uv.x*22.)  ) ;


    //Graident Mapping
    vec3 color2 = vec3(0.07+0.62*uv.x,0.04+0.15*uv.x,0.18+0.09*uv.x);
    color = mix(color,color2,0.7);

	gl_FragColor = vec4( color, 1.0 );
}
