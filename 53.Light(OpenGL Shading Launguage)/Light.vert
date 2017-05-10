varying vec2 vTexCoord;
varying vec3 vNormal;
varying vec3 vEye;
varying float fogFactor;

void main() {

	vTexCoord = gl_MultiTexCoord0.xy;
	vNormal = normalize(gl_NormalMatrix * gl_Normal);

	vec4 eyeSpaceVertexPos = gl_ModelViewMatrix * gl_Vertex;
	vEye = (vec3(eyeSpaceVertexPos)) / eyeSpaceVertexPos.w;

	gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * vec4( gl_Vertex.xyz, 1.0 );
	gl_FrontColor = gl_Color;

	vec3 vVertex = (gl_ModelViewMatrix * gl_Vertex).xyz;

	gl_FogFragCoord = length(vVertex);
	float fogFactor = exp2(-gl_Fog.density *
					 gl_Fog.density  *
					 gl_FogFragCoord *
					 gl_FogFragCoord *
					 1.442695);
	fogFactor = clamp(fogFactor, 0.0, 1.0);
}
