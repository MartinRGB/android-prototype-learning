// simple fragment shader

// 'time' contains seconds since the program was linked.
uniform float time;

varying float LightIntensity;
uniform sampler2D EarthTexture;

void main()
{
	vec3 lightColor = vec3(texture2D(EarthTexture,gl_TexCoord[0].st));

	gl_FragColor = vec4(lightColor*LightIntensity,1.);
}
