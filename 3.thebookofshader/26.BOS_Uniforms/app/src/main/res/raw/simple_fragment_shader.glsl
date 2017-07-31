#ifdef GL_ES
precision mediump float;
uniform float u_Time;
#endif


void main() {
	gl_FragColor = vec4(abs(sin(u_Time)),0.0,0.0,1.0);
}

