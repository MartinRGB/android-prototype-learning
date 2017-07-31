// simple fragment shader

// 'time' contains seconds since the program was linked.
uniform sampler2D EarthDay;
uniform sampler2D EarthNight;
uniform sampler2D EarthCloudGloss;

varying float Diffuse;
varying vec3 Specular;
varying vec2 TexCoord;

vec3 rotx(vec3 p, float a) {
	float s = sin(a), c = cos(a);
	return vec3(p.x, c * p.y - s * p.z, s * p.y + c * p.z);
}
vec3 roty(vec3 p, float a) {
	float s = sin(a), c = cos(a);
	return vec3(c * p.x + s * p.z, p.y, -s * p.x + c * p.z);
}
vec3 rotz(vec3 p, float a) {
	float s = sin(a), c = cos(a);
	return vec3(c * p.x - s * p.y, s * p.x + c * p.y, p.z);
}

void main()
{
	vec3 clouds = texture2D(EarthCloudGloss,TexCoord).stp ;
	vec3 daytime = (texture2D(EarthDay,TexCoord).stp * Diffuse + Specular * clouds.g) * (1.0 - clouds.r)+ clouds.r * Diffuse;
	vec3 nighttime = texture2D(EarthNight,TexCoord).stp * (1.0 - clouds.r);
	
	vec3 color = daytime;
	if(Diffuse < -0.1)
		color =nighttime;
	if(abs(Diffuse) < 0.1)
		color = mix(nighttime,daytime,(Diffuse + 0.1) * 5.0);


	gl_FragColor = vec4(color,1.);
}
