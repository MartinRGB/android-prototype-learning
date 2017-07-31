attribute vec4 a_Position;
attribute vec4 a_Color;

//varying 特殊变量 融合两个顶点之间片段的颜色，产生渐变
varying vec4 v_Color;
varying vec4 v_position;

//mat4 的意思是 4x4矩阵
uniform float u_time;
uniform vec2 u_resolution;
uniform mat4 u_Matrix;

varying vec3 from;
varying vec3 dir;
varying vec3 v;

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


void main(){

//    vec2 st = (a_Position*u_Matrix).xy + 1. ;
//    st.y *=u_resolution.y/u_resolution.x;
//
//
//    vec3 color = vec3(0.);
//    color = vec3(st.x,st.y,abs(sin(u_time)));
//
//    //get coords and direction
//    vec2 uv= st;
//    dir=vec3(uv*zoom,1.);
//    float time=u_time*speed+.25;
//
//    //mouse rotation
//    float a1=.5;
//    float a2=.8;
//    mat2 rot1=mat2(cos(a1),sin(a1),-sin(a1),cos(a1));
//    mat2 rot2=mat2(cos(a2),sin(a2),-sin(a2),cos(a2));
//    dir.xz*=rot1;
//    dir.xy*=rot2;
//    from=vec3(1.,0.5,0.5);
//    from+=vec3(time*2.,time,-2.);
//    from.xz*=rot1;
//    from.xy*=rot2;
//
//    float s=0.1,fade=1.5;
//    v=vec3(0.);
//    for (int r=0; r<volsteps; r++) {
//        vec3 p=from+s*dir*.5;
//        p = abs(vec3(tile/1.)-mod(p,vec3(tile/1.*1.5))); // tiling fold
//        float pa,a=pa=0.;
//        for (int i=0; i<iterations; i++) {
//            p=abs(p)/dot(p,p)-formuparam; // the magic formula
//            a+=abs(length(p)-pa); // absolute sum of average change
//            pa=length(p);
//        }
//        float dm=max(0.,darkmatter); //dark matter
//        a*=a*a; // add contrast
//        if (r>10) fade*=1.- dm; // dark matter, don't render near
//        //v+=vec3(dm,dm*.5,0.);
//        v+=fade;
//        v+=vec3(s/2.,s*s,s*s*s*s*4.9)*a*brightness*fade; // coloring based on distance
//        fade*=distfading; // distance fading
//        s+=stepsize;
//    }
//
//
//    v=mix(vec3(length(v)),v,saturation); //color adjust






    v_Color = a_Color;
    //v_Color = a Color;

    v_position = a_Position * u_Matrix;
    gl_Position = a_Position * u_Matrix;

    //指定点的大小
    gl_PointSize = 1.0;
}