#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;


//void main() {
//	vec2 st = gl_FragCoord.xy/u_resolution.xy;
//
//	//init
//	vec3 color = vec3(.0);
//
//	//cell positions
//    vec2 point[5];
//    point[0] = vec2(0.530,0.610);
//    point[1] = vec2(0.630,0.300);
//    point[2] = vec2(0.28,0.64);
//    point[3] =  vec2(0.31,0.26);
//    //point[4] = u_mouse/u_resolution;
//    point[4] = vec2(0.5*sin(u_time) + 0.25,0.5*cos(u_time)+0.25);
//
//    //minimum distance
//    float m_dist = 1.;
//
//    for(int i=0;i<5;i++){
//        float dist = distance(st, point[i]);
//
//        // Keep the closer distance
//        m_dist = min(m_dist, dist);
//    }
//
//    //add shape
//    color += m_dist;
//
//
//    // Show isolines
//        // color -= step(.7,abs(sin(50.0*m_dist)))*.3;
//
//    gl_FragColor = vec4(color,1.0);
//
//}

//Higher Effeictive

vec2 random2( vec2 p ) {
     return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
 }


 void main() {
     vec2 st = gl_FragCoord.xy/u_resolution.xy;
     //st.x *= u_resolution.x/u_resolution.y;
     vec3 color = vec3(.0);

     // Scale
     st *= 5.;

     // Tile the space
     vec2 i_st = floor(st);
     vec2 f_st = fract(st);

     vec2 point = random2(i_st);
     vec2 diff = point - f_st;

     float dist = length(diff);

     // Draw the min distance (distance field)
     color += dist;

     // Draw cell center
     color += 1.-step(0.028, dist);

     // Draw grid
     color.r += step(.98, f_st.x) + step(.98, f_st.y);

     // Show isolines
     // color -= step(.7,abs(sin(27.0*dist)))*.5;

     gl_FragColor = vec4(color,1.0);
 }