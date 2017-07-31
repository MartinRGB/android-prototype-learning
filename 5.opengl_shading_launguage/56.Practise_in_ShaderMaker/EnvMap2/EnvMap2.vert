varying vec3 Normal;
varying vec3 EyeDir;
varying float LightIntensity;

const vec3 LightPos = vec3(0.,0.,4.);

void main(void)
{
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor  = gl_Color;
	gl_TexCoord[0] = gl_MultiTexCoord0;

	
	Normal = normalize(gl_NormalMatrix * gl_Normal);
	vec4 pos = gl_ModelViewMatrix * gl_Vertex;
	EyeDir = pos.xyz;
	LightIntensity = max(dot(normalize(LightPos - EyeDir),Normal),0.0);
	
	//gl_TexCoord[0] = gl_MultiTexCoord0;
	//gl_Position = ftransform();
}
