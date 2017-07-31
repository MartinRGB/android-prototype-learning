
// Should be built in, but this is GLES

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

uniform mat4 u_ModelViewMatrix;
uniform mat4 u_ProjectionMatrix;
uniform mat3 u_NormalMatrix;
attribute vec4 a_Vertex;
attribute vec3 a_Normal;

// Other variables
uniform vec3 u_LightPosition;

const float SpecularContribution = 0.8;
const float DiffuseContribution = 10.; //3.
const float ZERO = 0.0;
const float ONE = 1.0;


varying float LightIntensity;
varying vec2 MCPosition;

void main()
{
    //盯住的中心位置
    vec3 ecPosition = vec3(u_ModelViewMatrix * a_Vertex);
    //漫反射方向
    vec3 tnorm      = normalize(u_NormalMatrix * a_Normal);

    //vec3 tnorm = normalize(vec3(u_NormalMatrix * vec4(a_Normal,0.0)));
    //光照方向
    vec3 lightVec   = normalize(u_LightPosition+vec3(1.+sin(u_time),1.+cos(u_time),1.) - ecPosition);
    //反射方向
    vec3 reflectVec = reflect(-lightVec, tnorm);
     //眼睛的方向
    vec3 viewVec    = normalize(-ecPosition);
    //漫反射
    float diffuse   = max(dot(lightVec, tnorm), ZERO);
    //镜面反射
    float spec      = ZERO;

    if(diffuse > ZERO)
    {
        spec = max(dot(reflectVec, viewVec), ZERO);
        spec = pow(spec, 16.0);
    }

    LightIntensity  = DiffuseContribution * diffuse +
              SpecularContribution * spec;

    MCPosition  = a_Vertex.xy;
    gl_Position = u_ProjectionMatrix * a_Vertex;
    //gl_Position  = a_Vertex;

}