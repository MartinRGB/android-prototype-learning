#ifdef GL_ES
precision mediump float;
#endif

#extension GL_OES_standard_derivatives : enable

uniform float time;
uniform vec2 resolution;

void main( void ) {
	vec2 pos = ((gl_FragCoord.xy / resolution.xy) * 2. - 1.) * vec2(resolution.x / resolution.y, 1.0);
	
	float d = abs(0.1 + length(pos) - 0.75 * abs(sin(time - cos(time * 0.01) * 9.0 * length(pos)))) * 6.0;
	float e = abs(0.1 + length(pos) - 0.5 * abs(sin(time * 0.5 - 4.))) * 10.0;
	
	gl_FragColor += vec4(0.1/d, 0.1 / d, 0.27 / d, 1);
	gl_FragColor += vec4(0.27/d/e, 0.1 / d/e, 0.1 / d/e, 1);
}