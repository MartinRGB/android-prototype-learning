#ifdef GL_ES
precision highp float;
#endif

#define PI 3.14159265359
uniform vec2 u_resolution;
uniform float u_time;

//#Function for sip
vec3 glColor3f(float r,float g,float b){
    return vec3(r,g,b);
}


//#Draw Lines
float plot (vec2 st, float pct){
  return  smoothstep( pct-0.2, pct, st.y) -
          smoothstep( pct, pct+0.2, st.y);
}


void main() {
    //#Coordinate
    vec2 st = gl_FragCoord.xy/u_resolution.xy;
    //#Base Constructor
    vec3 color = vec3(0.);

    //#Color Setting
    vec3 colorwhite = glColor3f(1.00,1.00,1.00);
    vec3 colorbg = glColor3f(0.10,0.12,0.30);
    vec3 color0 = glColor3f(0.58,0.15,0.56);
    vec3 color1 = glColor3f(0.00,0.68,0.94);
    vec3 color2 = glColor3f(0.45,0.75,0.27);
    vec3 color3 = glColor3f(1.00,0.95,0.00);
    vec3 color4 = glColor3f(0.97,0.58,0.12);
    vec3 color5 = glColor3f(0.93,0.19,0.14);

    vec3 pct = vec3(st.x,st.y,0.0);

    //mix(bgColor,LineColor,Shapes);
    color = mix(colorbg*sin(u_time),colorwhite*cos(u_time),pct);

    float period = 10.;
    float phase = 2.*PI/period;

    color = mix(color,color0,plot(st,sin(phase*st.x + 2.*phase + u_time )-0.152));
    color = mix(color,color1,plot(st,sin(phase*st.x + 2.*phase + u_time)-0.308));
    color = mix(color,color3,plot(st,sin(phase*st.x + 2.*phase + u_time)-0.460));
    color = mix(color,color4,plot(st,sin(phase*st.x + 2.*phase + u_time)-0.612));
    color = mix(color,color5,plot(st,sin(phase*st.x + 2.*phase + u_time)-0.764));

    gl_FragColor = vec4(color,1.0);
}

//HSB <--> RGB

//vec3 rgb2hsb( in vec3 c ){
//    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
//    vec4 p = mix(vec4(c.bg, K.wz),
//                 vec4(c.gb, K.xy),
//                 step(c.b, c.g));
//    vec4 q = mix(vec4(p.xyw, c.r),
//                 vec4(c.r, p.yzx),
//                 step(p.x, c.r));
//    float d = q.x - min(q.w, q.y);
//    float e = 1.0e-10;
//    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)),
//                d / (q.x + e),
//                q.x);
//}
//
////  Function from Iñigo Quiles
////  https://www.shadertoy.com/view/MsS3Wc
//vec3 hsb2rgb( in vec3 c ){
//    vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0,4.0,2.0),
//                             6.0)-3.0)-1.0,
//                     0.0,
//                     1.0 );
//    rgb = rgb*rgb*(3.0-2.0*rgb);
//    return c.z * mix(vec3(1.0), rgb, c.y);
//}
//
//void main(){
//    vec2 st = gl_FragCoord.xy/u_resolution;
//    vec3 color = vec3(0.0);
//
//    // We map x (0.0 - 1.0) to the hue (0.0 - 1.0)
//    // And the y (0.0 - 1.0) to the brightness
//    //color = hsb2rgb(vec3(st.x,1.0,st.y));
//    color = rgb2hsb(vec3(st.x,st.y,st.x));
//    gl_FragColor = vec4(color,1.0);
//}