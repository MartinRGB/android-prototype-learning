const vec3 Xunitvec = vec3 (1.,0.,0.);
const vec3 Yunitvec = vec3 (0.,1.,0.);

const vec3 BaseColor = vec3 (0.4,0.4,1.);
float MixRatio = 0.2;

uniform sampler2D EnvMap;

varying vec3 Normal;
varying vec3 EyeDir;
varying float LightIntensity;

void main(void) 
{
	vec3 reflectDir = reflect(EyeDir,Normal);

	vec3 envColor = vec3(texture2D(EnvMap,reflectDir.xy/4.));
	vec3 base = LightIntensity * BaseColor;
	envColor = mix(envColor,base,MixRatio);	


	gl_FragColor = vec4 (envColor,1.0);

	


}