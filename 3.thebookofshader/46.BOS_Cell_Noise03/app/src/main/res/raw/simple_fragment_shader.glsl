#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

//vec2 random2( vec2 p ) {
   //    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
   //}
   //
   //void main() {
   //    vec2 st = gl_FragCoord.xy/u_resolution.xy;
   //    st.x *= u_resolution.x/u_resolution.y;
   //    vec3 color = vec3(.0);
   //
   //    // Scale
   //    st *= 5.;
   //
   //    // Tile the space
   //    vec2 i_st = floor(st);
   //    vec2 f_st = fract(st);
   //
   //    float m_dist = 10.;  // minimun distance
   //    vec2 m_point;        // minimum point
   //
   //    for (int j=-1; j<=1; j++ ) {
   //        for (int i=-1; i<=1; i++ ) {
   //            vec2 neighbor = vec2(float(i),float(j));
   //            vec2 point = random2(i_st + neighbor);
   //            point = 0.5 + 0.5*sin(u_time + 6.2831*point);
   //            vec2 diff = neighbor + point - f_st;
   //            float dist = length(diff);
   //
   //            if( dist < m_dist ) {
   //                m_dist = dist;
   //                m_point = point;
   //            }
   //        }
   //    }
   //
   //    // Assign a color using the closest point position
   //    color += dot(m_point,vec2(0.3,0.360));
   //
   //    // Add distance field to closest point center
   //    // color.g = m_dist;
   //
   //    // Show isolines
   //    color -= abs(sin(40.0*m_dist))*0.07;
   //
   //    // Draw cell center
   //    color += 1.-step(.05, m_dist);
   //
   //    // Draw grid
   //    color.r += step(.98, f_st.x) + step(.98, f_st.y);
   //
   //    gl_FragColor = vec4(color,1.0);
   //}


//Blur
//   vec3 hash3( vec2 p ) {
//       vec3 q = vec3( dot(p,vec2(127.1,311.7)),
//                      dot(p,vec2(269.5,183.3)),
//                      dot(p,vec2(419.2,371.9)) );
//       return fract(sin(q)*43758.5453);
//   }
//
//   float iqnoise( in vec2 x, float u, float v ) {
//       vec2 p = floor(x);
//       vec2 f = fract(x);
//
//       float k = 1.0+63.0*pow(1.0-v,4.0);
//
//       float va = 0.0;
//       float wt = 0.0;
//       for (int j=-2; j<=2; j++) {
//           for (int i=-2; i<=2; i++) {
//               vec2 g = vec2(float(i),float(j));
//               vec3 o = hash3(p + g)*vec3(u,u,1.0);
//               vec2 r = g - f + o.xy;
//               float d = dot(r,r);
//               float ww = pow( 1.0-smoothstep(0.0,1.414,sqrt(d)), k );
//               va += o.z*ww;
//               wt += ww;
//           }
//       }
//
//       return va/wt;
//   }
//
//   void main() {
//       vec2 st = gl_FragCoord.xy/u_resolution.xy;
//       st.x *= u_resolution.x/u_resolution.y;
//       vec3 color = vec3(0.0);
//
//       st *= 10.;
//       float n = iqnoise(st, u_mouse.x/u_resolution.x, u_mouse.y/u_resolution.y);
//
//       gl_FragColor = vec4(vec3(n),1.0);
//   }


//MetaBall

vec2 random2( vec2 p ) {
    return fract(sin(vec2(dot(p,vec2(127.1,311.7)),dot(p,vec2(269.5,183.3))))*43758.5453);
}

void main() {
    vec2 st = gl_FragCoord.xy/u_resolution.xy;
    st.x *= u_resolution.x/u_resolution.y;
    vec3 color = vec3(.0);

    // Scale
    st *= 5.;

    // Tile the space
    vec2 i_st = floor(st);
    vec2 f_st = fract(st);

    float m_dist = 1.;  // minimun distance
    for (int j= -1; j <= 1; j++ ) {
        for (int i= -1; i <= 1; i++ ) {
            // Neighbor place in the grid
            vec2 neighbor = vec2(float(i),float(j));

            // Random position from current + neighbor place in the grid
            vec2 offset = random2(i_st + neighbor);

            // Animate the offset
            offset = 0.5 + 0.5*sin(u_time + 6.2831*offset);

            // Position of the cell
            vec2 pos = neighbor + offset - f_st;

            // Cell distance
            float dist = length(pos);

            // Metaball it!
            m_dist = min(m_dist, m_dist*dist);
        }
    }

    // Draw cells
    color += step(0.060, m_dist);

    gl_FragColor = vec4(color,1.0);
}