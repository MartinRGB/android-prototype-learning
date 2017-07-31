const vec3 Xunitvec = vec3 (1.,0.,0.);
const vec3 Yunitvec = vec3 (0.,1.,0.);

const vec3 BaseColor = vec3 (0.4,0.4,1.);
float MixRatio = 0.8;

uniform sampler2D EnvMap;

varying vec3 Normal;
varying vec3 EyeDir;
varying float LightIntensity;

void main(void) 
{
	vec3 reflectDir = reflect(EyeDir,Normal);

	vec2 index;

	index.y = dot(normalize(reflectDir),Yunitvec);
	reflectDir.y = 0.0;
	index.x = dot(normalize(reflectDir),Xunitvec) * 0.5;

	if (reflectDir.z >= 0.0)
		index = (index + 1.0)*0.5;
	else
		index.t = (index.t + 1.0) * 0.5;
		index.s = (-index.s) * 0.5 + 1.0;

	vec3 envColor = vec3(texture2D(EnvMap,index));
	vec3 base = LightIntensity * BaseColor;
	envColor = mix(envColor,base,MixRatio);

	gl_FragColor = vec4 (envColor,1.0);


}