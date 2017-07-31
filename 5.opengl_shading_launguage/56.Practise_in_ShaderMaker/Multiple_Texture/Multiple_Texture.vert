// simple vertex shader
varying float Diffuse;
varying vec3 Specular;
varying vec2 TexCoord;

uniform vec3 LightPosition;

void main()
{
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor  = gl_Color;
	gl_TexCoord[0] = gl_MultiTexCoord0;

	vec3 ecPosition = vec3(gl_ModelViewMatrix * gl_Vertex);
	vec3 tnorm = normalize(gl_NormalMatrix * gl_Normal);
	vec3 lightVec = normalize(LightPosition - ecPosition);
	vec3 reflectVec = reflect(-lightVec,tnorm);
	vec3 viewVec = normalize(-ecPosition);

	float spec = clamp(dot(reflectVec,viewVec),0.,1.);
	spec = pow(spec,8.);
	Specular = vec3(spec)*vec3(1.0,0.941,0.898) * 0.3;
	Diffuse = max(dot(lightVec,tnorm),0.0);

	TexCoord = gl_MultiTexCoord0.st;

	
	//gl_TexCoord[0] = gl_MultiTexCoord0;
	//gl_Position = ftransform();
}
