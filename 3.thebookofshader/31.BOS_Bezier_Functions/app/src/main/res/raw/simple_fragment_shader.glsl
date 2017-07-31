#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform float u_time;


float quadraticBezier (float x, vec2 a){
  // adapted from BEZMATH.PS (1993)
  // by Don Lancaster, SYNERGETICS Inc.
  // http://www.tinaja.com/text/bezmath.html

  float epsilon = 0.00001;
  a.x = clamp(a.x,0.0,1.0);
  a.y = clamp(a.y,0.0,1.0);
  if (a.x == 0.5){
    a += epsilon;
  }

  // solve t from x (an inverse operation)
  float om2a = 1.0 - 2.0 * a.x;
  float t = (sqrt(a.x*a.x + om2a*x) - a.x)/om2a;
  float y = (1.0-2.0*a.y)*(t*t) + (2.0*a.y)*t;
  return y;
}

float lineSegment(vec2 p, vec2 a, vec2 b) {
    vec2 pa = p - a, ba = b - a;
    float h = clamp( dot(pa,ba)/dot(ba,ba), 0.0, 1.0 );
    return smoothstep(0.0, 1.0 / u_resolution.x, length(pa - ba*h));
}

void main() {

    float ntime = clamp(sin(u_time),0.,0.4);
    vec2 st = gl_FragCoord.xy/u_resolution.xy;
	float px = 1.0 / u_resolution.y + ntime;

    // control point
    vec2 cp = vec2(cos(u_time),sin(u_time)) * 0.45 + 0.5;
    float l = quadraticBezier(st.x, cp);
    vec3 color = vec3(smoothstep(l, l+px, st.y));

    // draw control point
    color = mix(vec3(0.5), color, lineSegment(st, vec2(0.0), cp));
    color = mix(vec3(0.5), color, lineSegment(st, vec2(1.0), cp));
    float d = distance(cp, st);
    color = mix(vec3(1.0,0.0,0.0), color, smoothstep(0.01,0.01+px,d));

    gl_FragColor = vec4(color, 1.0);
}