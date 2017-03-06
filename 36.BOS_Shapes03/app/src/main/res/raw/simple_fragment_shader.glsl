#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

//dot()是计算圆最有效率的一种方式

//float circle(in vec2 _st, in float _radius){
//    vec2 dist = _st-vec2(u_mouse/u_resolution);
//	return 1.-smoothstep(_radius-(_radius*0.01),
//                         _radius+(_radius*0.01),
//                         dot(dist,dist)*4.0);
//}
//
//
//void main(){
//	vec2 st = gl_FragCoord.xy/u_resolution;
//    vec2 mouse = u_mouse.xy/u_resolution;
//    float pct = 0.0;
//
//    // a. The DISTANCE from the pixel to the center
//    //pct = distance(st,vec2(mouse));
////
////    pct = distance(st,vec2(0.4)) + distance(st,vec2(0.6));
////    pct = distance(st,vec2(0.4)) * distance(st,vec2(0.6));
////    pct = min(distance(st,vec2(0.4)),distance(st,vec2(0.6)));
////    pct = max(distance(st,vec2(0.4)),distance(st,vec2(0.6)));
////    pct = pow(distance(st,vec2(0.4)),distance(st,vec2(0.6)));
//
//    // b. The LENGTH of the vector
//    //    from the pixel to the center
//    // vec2 toCenter = vec2(0.5)-st;
//    // pct = length(toCenter);
//
//    // c. The SQUARE ROOT of the vector
//    //    from the pixel to the center
//    // vec2 tC = vec2(0.5)-st;
//    // pct = sqrt(tC.x*tC.x+tC.y*tC.y);
//
//    vec3 color = vec3(circle(st,0.9));
//
//	gl_FragColor = vec4( color, 1.0 );
//}

//void main(){
//  vec2 st = gl_FragCoord.xy/u_resolution.xy;
//  st.x *= u_resolution.x/u_resolution.y;
//  vec3 color = vec3(0.0);
//  float d = 0.0;
//
//  // Remap the space to -1. to 1.
//  st = st *2.-1.;
//
//  // Make the distance field
//  d = length( abs(st)-.3 );
//  // d = length( min(abs(st)-.3,0.) );
//  // d = length( max(abs(st)-.3,0.) );
//
//  // Visualize the distance field
//  gl_FragColor = vec4(vec3(fract(d*10.0)),1.0);
//
//  // Drawing with the distance field
//  // gl_FragColor = vec4(vec3( step(.3,d) ),1.0);
//  // gl_FragColor = vec4(vec3( step(.3,d) * step(d,.4)),1.0);
//  // gl_FragColor = vec4(vec3( smoothstep(.3,.4,d)* smoothstep(.6,.5,d)) ,1.0);
//}

#define PI 3.14159265359
#define TWO_PI 6.28318530718

void main(){
  vec2 st = gl_FragCoord.xy/u_resolution.xy;
  st.x *= u_resolution.x/u_resolution.y;
  vec3 color = vec3(0.0);
  float d = 0.0;

  // Remap the space to -1. to 1.
  st = st *2.-1.;

  // Number of sides of your shape
  int N = 3;

  // 角度和半径 Angle and radius from the current pixel
  float a = atan(st.x,st.y)+PI;
  float r = TWO_PI/float(N);

  // 构型函数 Shaping function that modulate the distance
  d = cos(floor(0.500+a/r)*r-a)*length(st);

  color = vec3(1.0-smoothstep(0.4,0.410,d));
  // color = vec3(d);

  gl_FragColor = vec4(color,1.0);
}