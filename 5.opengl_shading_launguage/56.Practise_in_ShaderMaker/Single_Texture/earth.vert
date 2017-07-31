// simple vertex shader
uniform float time;
varying float LightIntensity;
uniform vec3 LightPosition;


void main()
{
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	//gl_FrontColor  = gl_Color;
	gl_TexCoord[0] = gl_MultiTexCoord0;

	vec3 ecPosition = vec3(gl_ModelViewMatrix * gl_Vertex);
	vec3 tnorm = normalize(gl_NormalMatrix * gl_Normal);
	vec3 lightVec = normalize(LightPosition - ecPosition);
	vec3 reflectVec = reflect(-lightVec,tnorm);
	vec3 viewVec = normalize(-ecPosition);

	float spec = clamp(dot(reflectVec,viewVec),0.,1.);
	spec = pow(spec,16.);
	
	LightIntensity = 0.9 * max(dot(lightVec,tnorm),0.) + 0.1 * spec;
	
	//gl_TexCoord[0] = gl_MultiTexCoord0;
	//gl_Position = ftransform();
}
