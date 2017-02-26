#ifdef GL_ES
precision mediump float;
uniform vec2 u_Resolution;
uniform vec2 u_Mouse;
uniform float u_Time;
#endif


void main() {
    vec2 pointerst = gl_FragCoord.xy/u_Mouse;
    float blue = pointerst.x * pointerst.y;
	vec2 st = gl_FragCoord.xy/u_Resolution;
    gl_FragColor = vec4(st.x,abs(sin(u_Time)),blue,1.0);
}

