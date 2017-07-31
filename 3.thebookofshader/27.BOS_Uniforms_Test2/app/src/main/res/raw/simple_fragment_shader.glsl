#ifdef GL_ES
precision mediump float;
#endif
#define PI 3.14159265359
#define TWO_PI 6.28318530718
#define AQUA vec3(0.3, 1.0, 1.0)
#define SUNFLOWER vec3(1.0, 1.0, 0.6)
#define IVORY vec3(1.0, 0.9, 0.8)
#define WHITE vec3(1.0, 1.0, 1.0)
#define NAVY vec3(0.0, 0.1, 0.2)
#define TURQUOISE vec3(0.0, 1.0, 0.7)
#define SUNSET vec3(0.9, 0.3, 0.3)

uniform vec2 u_resolution;
uniform vec2 u_mouse;
uniform float u_time;

vec2 rotate(vec2 coord, float angle) {
  return mat2(cos(angle),-sin(angle),
              sin(angle),cos(angle)) * coord;
}

float easeOutQuad(float t) {
    return -1.0 * t * (t - 2.0);
}

float easeOutCubic(float t) {
    return (t = t - 1.0) * t * t + 1.0;
}

float easeInCubic(float t) {
    return t * t * t;
}

float easeInOutCubic(float t) {
    if ((t *= 2.0) < 1.0) {
        return 0.5 * t * t * t;
    } else {
        return 0.5 * ((t -= 2.0) * t * t + 2.0);
    }
}

float easeInOutExpo(float t) {
    if (t == 0.0 || t == 1.0) {
        return t;
    }
    if ((t *= 2.0) < 1.0) {
        return 0.5 * pow(2.0, 10.0 * (t - 1.0));
    } else {
        return 0.5 * (-pow(2.0, -10.0 * (t - 1.0)) + 2.0);
    }
}

float linearstep(float begin, float end, float t) {
    return clamp((t - begin) / (end - begin), 0.0, 1.0);
}

float linearstepUpDown(float upBegin, float upEnd, float downBegin, float downEnd, float t) {
    return linearstep(upBegin, upEnd, t) - linearstep(downBegin, downEnd, t);
}

float stepUpDown(float begin, float end, float t) {
  return step(begin, t) - step(end, t);
}

float clockWipe(vec2 p, float t) {
    float a = atan(-p.x, -p.y);
    float v = (t * TWO_PI > a + PI) ? 1.0 : 0.0;
    return v;
}

float smoothedge(float v, float f) {
    return smoothstep(0.0, f / u_resolution.x, v);
}

float circle(vec2 p, float radius) {
  return length(p) - radius;
}

float circlePlot(vec2 p, float radius) {
  return 1.0 - smoothedge(circle(p, radius), 1.0);
}

float rect(vec2 p, vec2 size) {
  vec2 d = abs(p) - size;
  return min(max(d.x, d.y), 0.0) + length(max(d,0.0));
}

float rectPlot(vec2 p, vec2 size) {
  return 1.0 - smoothedge(rect(p, size), 1.0);
}

float triangle(vec2 p, float size) {
    vec2 q = abs(p);
    return max(-p.y * 0.5, q.x * 0.866025 + p.y * 0.5) - size * 0.5;
}

float trianglePlot(vec2 p, float size) {
  return 1.0 - smoothedge(triangle(p, size), 1.0);
}

float stripe(float p, float w0, float w1, float offset) {
    float w = (w0 + w1);
    return step(w0, mod(p + offset, w));
}

float ringSpinner(vec2 st, float r0, float r1, float t0, float t1) {
   	float v0 = circlePlot(st - vec2(0.5), r0);
    float v1 = circlePlot(st - vec2(0.5), r1);
   	float v2 = clockWipe(st - vec2(0.5), easeInOutCubic(linearstep(0.0, 1.0, t0)));
    float v3 = clockWipe(st - vec2(0.5), easeInOutCubic(linearstep(0.0, 1.0, t1)));
    return (v0 - v1) * (v2 - v3);
}

float ringSpinners(vec2 st, float t) {
    float v = 0.0;
   	for (float i = 0.0; i < 0.5; i += 0.25) {
        float t0 = linearstep(i + 0.0, i + 0.5, t);
        float t1 = linearstep(i + 0.25, i + 0.5, t);
        v = max(v, ringSpinner(st, i * 1.0 + 0.25, i * 1.0, t0, t1));
    }
    return v;
}

float stripeWipe(vec2 st, float t) {
    t = easeInOutCubic(t);
    return stripe(rotate(st - vec2(0.5), PI * 0.75).x, 0.05 - t * 0.05, t * 0.05, 0.0);
}

float collider(vec2 p, vec2 b, vec2 e, float t) {

    float t0 = linearstep(0.0, 0.5, t);
    float p0 = easeInCubic(t0);
    float t1 = linearstep(0.5, 1.0, t);
    float p1 = easeOutQuad(t1);

    return rectPlot(p - mix(b, e, p0 - p1), vec2(0.05));
}

float colliders(vec2 st, float t) {
    float t0 = fract(t);
    float t1 = fract(t + 0.5);
    float v = collider(st, vec2(0.05, 0.5), vec2(0.45, 0.5), t0);
    v = max(v, collider(st, vec2(0.95, 0.5), vec2(0.55, 0.5), t0));
    v = max(v, collider(st, vec2(0.5, 0.05), vec2(0.5, 0.45), t1));
    v = max(v, collider(st, vec2(0.5, 0.95), vec2(0.5, 0.55), t1));
    return v;
}

float chaser(vec2 p, float t) {
    float t0 = linearstep(0.0, 0.25, t);
    float x0 = easeInOutExpo(t0);
    float t1 = linearstep(0.5, 0.75, t);
    float x1 = easeInOutExpo(t1);

    float t2 = linearstep(0.25, 0.5, t);
    float y0 = easeInOutExpo(t2);
    float t3 = linearstep(0.75, 1.0, t);
    float y1 = easeInOutExpo(t3);

    return rectPlot(p - vec2(mix(0.2, 0.8, x0 - x1), mix(0.2, 0.8, y0 - y1)), vec2(0.05));
}

float chasers(vec2 st, float t) {
    t = fract(t);
    float v = chaser(st, fract(t));
    v = max(v, chaser(st, fract(t + 0.25)));
    v = max(v, chaser(st, fract(t + 0.5)));
    v = max(v, chaser(st, fract(t + 0.75)));
    return v;
}

void main(){
    vec2 st = gl_FragCoord.xy / u_resolution.xy;
    vec2 center = vec2(0.5);
    float t = mod(u_time, 6.0), v = 0.0;

    vec3 color = SUNSET;

    // SPINNER
    float v00 = ringSpinners(fract(st * 1.0), linearstep(0.0, 2.0, t));
    float v01 = circlePlot(st - vec2(0.5), easeInOutCubic(linearstep(1.0, 2.0, t)));
    color = mix(color, IVORY, v00);

    // WIPE - COLLIDERS
    color = mix(color, SUNFLOWER, v01);
    float t00 = easeInOutCubic(linearstep(2.0, 3.0, t));
    vec2 st00 = st;
    st00.x = fract(st.x + t00);
    float v02 = colliders(st00, fract(t));
    color = mix(color, TURQUOISE, v02 * v01);

    // WIPE - ZOOM
    color = mix(color, TURQUOISE, step(1.0 - st.x, t00));
    float t01 = easeInOutCubic(linearstep(2.2, 3.0, t));
    vec2 st01 = st;
    st01.x += t01 - 1.0;
    float v03 = rectPlot(st01 - center, vec2(mix(0.05, 0.5, easeInCubic(linearstep(2.75, 3.25, t)))));
    float v04 = rectPlot(st01 - center, vec2(mix(0.0, 0.5, easeInCubic(linearstep(3.0, 3.5, t)))));
    float t03 = easeOutCubic(linearstep(3.0, 3.75, t));
	color = mix(color, IVORY, v03);
    color = mix(color, NAVY, v04);

    // CHASERS
    float v05 = chasers((st - center) / t03 + center, linearstep(3.5, 7.5, t));
    float v06 = stripeWipe(st, linearstep(4.0, 4.5, t));
    color = mix(color, SUNFLOWER, v05 * v04 * (1.0 - v06));

    // TRIANGLE
    float t04 = 1.0 - easeInOutCubic(linearstep(4.0, 4.5, t))
        + easeInOutCubic(linearstep(4.5, 5.0, t)) * 0.25
        - easeInCubic(linearstep(4.75, 5.5, t) * 1.5);
    vec2 st02 = rotate(st - center, (linearstep(4.4, 4.7, t) - 1.0) * PI * 0.5) + vec2(0.0, t04);
    float t05 = easeInCubic(linearstep(4.75, 5.25, t)) * 0.2;
    float t06 = easeInCubic(linearstep(5.0, 5.25, t)) * 2.0;
    float v07 = trianglePlot(st02, 0.1);
    float v08 = trianglePlot(st02 + vec2(0.0, t05), 0.1 + t05);
    float v09 = trianglePlot(st02 + vec2(0.0, t06), 0.1 + t06);
    color = mix(color, AQUA, v09);
    color = mix(color, IVORY , v08);
    color = mix(color, TURQUOISE, v07);

    // SHUTTER
    float t07 = easeOutCubic(linearstep(5.1, 5.6, t));
    float v10 = rectPlot(st - vec2(-0.25 + t07 * 0.5, 0.5), vec2(0.25, 0.5));
    float v11 = rectPlot(st - vec2(1.25 - t07 * 0.5, 0.5), vec2(0.25, 0.5));
    color = mix(color, NAVY, v10);
    color = mix(color, NAVY, v11);

    float t08 = easeInOutCubic(linearstep(5.4, 5.9, t));
    float v12 = rectPlot(st - vec2(0.5, 1.26 - t08 * 0.51), vec2(0.5, 0.25));
    float v13 = rectPlot(st - vec2(0.5, -0.26 + t08 * 0.51), vec2(0.5, 0.25));
    color = mix(color, SUNSET, v12);
    color = mix(color, SUNSET, v13);

    gl_FragColor = vec4(color, 1.0);
}
