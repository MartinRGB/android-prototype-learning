// simple vertex shader
uniform vec3 LightPosition;
const float SpecularContribution = 0.5;
const float DiffuseContribution = 0.7;

const float ZERO = 0.;
varying float LightIntensity;
varying vec2 MCposition;

void main()
{
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor  = gl_Color;
	gl_BackColor = vec4(vec3(0.),1.);
	gl_TexCoord[0] = gl_MultiTexCoord0;

	vec3 ecPosition = vec3(gl_ModelViewMatrix * gl_Vertex);

	//漫反射方向
    vec3 tnorm      = normalize(gl_NormalMatrix * gl_Normal);
    //光照方向
    vec3 lightVec   = normalize(LightPosition - ecPosition);
    //反射方向
    vec3 reflectVec = reflect(-lightVec, tnorm);
     //眼睛的方向
    vec3 viewVec    = normalize(-ecPosition);
    //漫反射
    float diffuse   = max(dot(lightVec, tnorm), 0.);
    //镜面反射
    float spec      = 0.;

	if(diffuse > ZERO)
    {
        spec = max(dot(reflectVec, viewVec), 0.);
        spec = pow(spec, 16.0);
    }

	LightIntensity = DiffuseContribution * diffuse + SpecularContribution * spec;
	MCposition = gl_Vertex.xy;
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	//gl_Position = ftransform();



}
