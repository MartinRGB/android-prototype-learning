
#ifdef GL_ES
precision highp float;
#endif

uniform vec2 u_resolution;
uniform vec2 u_mouse;
uniform float u_time;

const bool GlowEnabled = true;
const float Circumradius = 0.08;
const float WallDistance = 0.01;
const float GlowDistance = 0.11974;

const bool Jitter = true;
const float MarchDumping = 0.658;
const float Far = 5.0;
const int MaxSteps = 56;

#define PI 3.141592
#define RADIANS(x) ((x) * (PI / 180.0))

#define TETRAHEDRON_DIHEDRAL_ANGLE RADIANS(70.53)
#define HEXAHEDRON_DIHEDRAL_ANGLE RADIANS(90.0)
#define OCTAHEDRON_DIHEDRAL_ANGLE RADIANS(109.47)
#define DODECAHEDRON_DIHEDRAL_ANGLE RADIANS(116.57)
#define ICOSAHEDRON_DIHEDRAL_ANGLE RADIANS(138.19)

#define TETRAHEDRON_SCHLAFLI_SYMBOL vec2(3.0, 3.0)
#define HEXAHEDRON_SCHLAFLI_SYMBOL vec2(4.0, 3.0)
#define OCTAHEDRON_SCHLAFLI_SYMBOL vec2(3.0, 4.0)
#define DODECAHEDRON_SCHLAFLI_SYMBOL vec2(5.0, 3.0)
#define ICOSAHEDRON_SCHLAFLI_SYMBOL vec2(3.0, 5.0)

// Hash by iq
float hash(float h) {
    return fract(sin(h) * 43758.5453123);
}

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

float cot(float x) {
    return 1.0 / tan(x);
}

float getInradius(vec2 pq, float diha) {
    float tn = tan(diha * 0.5);
    float a = 2.0 * Circumradius / (tan(PI / pq.y) * tn);
    float r = 0.5 * a * cot(PI / pq.x) * tn;

    return r;
}

float tetrahedron(vec3 p) {
    float diha = -RADIANS(180.0 - 70.53);
    float tria = -RADIANS(60.0);
    float inra = getInradius(TETRAHEDRON_SCHLAFLI_SYMBOL, TETRAHEDRON_DIHEDRAL_ANGLE);

    float d = p.x - inra;

    p = rotz(p, diha);
    d = max(d, p.x - inra);

    p = rotx(p, tria);
    p = rotz(p, diha);

    d = max(d, p.x - inra);

    p = rotx(p, -tria);
    p = rotz(p, diha);
    d = max(d, p.x - inra);

    return d;
}

// cube
float hexahedron(vec3 p) {
    float inra = getInradius(HEXAHEDRON_SCHLAFLI_SYMBOL, HEXAHEDRON_DIHEDRAL_ANGLE);

    float d = abs(p.x) - inra;

    p = rotz(p, 1.5708); // 90 degrees
    d = max(d, abs(p.x) - inra);

    p = roty(p, 1.5708); // 90 degrees
    d = max(d, abs(p.x) - inra);

    return d;
}

float octahedron(vec3 p) {
    float d = -1e5;

    float inra = getInradius(OCTAHEDRON_SCHLAFLI_SYMBOL, OCTAHEDRON_DIHEDRAL_ANGLE);

    for (float i = 0.0; i < 4.0; i++) {
        p = rotz(p, 1.231); // 70.53110 degrees
        p = rotx(p, 1.047); // 60 degrees

        d = max(d, max(p.x - inra, -p.x - inra));
    }
    return d;
}

float dodecahedron(vec3 p) {
    float d = -1e5;

    float inra = getInradius(DODECAHEDRON_SCHLAFLI_SYMBOL, DODECAHEDRON_DIHEDRAL_ANGLE);

    for (float i = 0.0; i <= 4.0; i++) {
        p = roty(p, 0.81); // 46.40958 degrees
        p = rotx(p, 0.759); // 43.48750 degrees
        p = rotz(p, 0.3915); // 22.43130 degrees

        d = max(d, max(p.x - inra, -p.x - inra));
    }

    p = roty(p, 0.577); // 33.05966 degrees
    p = rotx(p, -0.266); // -15.24068 degrees
    p = rotz(p, -0.848); // -48.58682 degrees

    d = max(d, max(p.x - inra, -p.x - inra));

    return d;
}

float icosahedron(vec3 p) {
    float d = -1e5;

    //center band
    const float n1 = 0.7297; // 41.80873 degrees
    const float n2 = 1.0472; // 60 degrees

    float inra = getInradius(ICOSAHEDRON_SCHLAFLI_SYMBOL, ICOSAHEDRON_DIHEDRAL_ANGLE);

    for (float i = 0.0; i < 5.0; i++) {

        if (mod(i, 2.0) == 0.0) {
            p = rotz(p, n1);
            p = rotx(p, n2);
        } else {
            p = rotz(p, n1);
            p = rotx(p, -n2);
        }
        d = max(d, max(p.x - inra, -p.x - inra));
    }

    p = roty(p, 1.048); // 60.04598 degrees
    p = rotz(p, 0.8416); // 48.22013 degrees
    p = rotx(p, 0.7772); // 44.53028 degrees

    //top caps
    for (float i = 0.0; i < 5.0; i++) {
        p = rotz(p, n1);
        p = rotx(p, n2);

        d = max(d, max(p.x - inra, -p.x - inra));
    }
    return d;
}

float mapShape(vec3 p, float x) {
//    if (x > 4.0) {
//        return mix(icosahedron(p), tetrahedron(p), smoothstep(4.0, 5.0, x));
//    } else if (x > 3.0) {
//        return mix(octahedron(p), icosahedron(p), smoothstep(3.0, 4.0, x));
//    } else if (x > 2.0) {
//        return mix(dodecahedron(p), octahedron(p), smoothstep(2.0, 3.0, x));
//    } else if (x > 1.0) {
//        return mix(hexahedron(p), dodecahedron(p), smoothstep(1.0, 2.0, x));
//    }
    return mix(tetrahedron(p), hexahedron(p), smoothstep(0.0, 1.0, x));
}

float shapeProgress(float t) {
    t = mod(t, 15.0);

    float prog = 0.0;
    prog += smoothstep(2.0, 3.0, t);
    prog += smoothstep(5.0, 6.0, t);
    prog += smoothstep(8.0, 9.0, t);
    prog += smoothstep(11.0, 12.0, t);
    prog += smoothstep(14.0, 15.0, t);

    return prog;
}

float map(vec3 p) {
    return mapShape(p, shapeProgress(u_time));
}

vec3 render(vec3 ro, vec3 rd) {
    float tmin = 0.0;
    float tmax = Far;

    float precis = 0.002;
    float t = tmin;

    vec3 accu = vec3(0.0);
    float steps = 0.0;
    float glowFactor = 0.0;

    for (int i = 0; i < MaxSteps; i++) {
        steps = float(i);
        float d = map(ro + rd * t);
        float absd = abs(d);

        if (Jitter) {
            absd *= 0.8 + hash(absd) * 0.2;
        }
        if (t > tmax) {
            break;
        }
        glowFactor += pow(1.0 - smoothstep(-0.001, GlowDistance, d), 14.0)
                * step(0.0, d);
        float f = absd * (1.0 - smoothstep(0.0, WallDistance, absd));
        accu += vec3(f);

        t += max(0.0002, absd * MarchDumping);
    }
    glowFactor /= steps;

    if (GlowEnabled) {
        accu += 0.7 * pow(glowFactor, 1.2) * vec3(sin(u_time), cos(u_time), 0.);
    }
    return accu;
}

mat2 mm2(in float a) {
    float c = cos(a), s = sin(a);
    return mat2(c, -s, s, c);
}

vec3 mainImage(vec2 fragCoord) {
    vec2 p = fragCoord.xy / u_resolution.xy;
    p.y -= 0.15;
    vec2 uv = 2.0 * p - 1.0;
    uv.x *= u_resolution.x / u_resolution.y;

    vec3 ro = vec3(0.0, 0.0, -1.0);
    vec3 rd = normalize(vec3(uv, 3.5));

    vec2 um = u_mouse.xy / u_resolution.xy - 0.5;
    um.x *= u_resolution.x / u_resolution.y;

    mat2 mx = mm2(u_time * 0.75 + um.x * 6.0);
    mat2 my = mm2(u_time * 0.77 + um.y * 6.0);
    ro.xz *= mx;
    rd.xz *= mx;
    ro.xy *= my;
    rd.xy *= my;

    //vec3 col = vec3(0.);
    //col = mix(col,render(ro,rd),vec3(uv,1.));
    vec3 col = render(ro,rd);
    col = pow(col, vec3(0.4545));
    col = col.xyz;
    return col;
}

void main(){
    gl_FragColor = vec4(mainImage(gl_FragCoord.xy),1.);
}

//
//// Author @patriciogv - 2015
//// Title: DeFrag
//
//#ifdef GL_ES
//precision highp float;
//#endif
//
//uniform vec2 u_resolution;
//uniform vec2 u_mouse;
//uniform float u_time;
///*
//"Diamond test" by Emmanuel Keller aka Tambako - January 2016
//License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
//Contact: tamby@tambako.ch
//*/
//
//#define pi 3.141593
//
//struct Lamp
//{
//  vec3 position;
//  vec3 color;
//  float intensity;
//  float attenuation;
//};
//
//Lamp lamps[3];
//
//struct RenderData
//{
//  vec3 col;
//  vec3 pos;
//  vec3 norm;
//  int objnr;
//};
//
//vec3 campos = vec3(0., 0.5, 5.);
//vec3 camdir = vec3(0., -0.1, -1.);
//float fov = 2.;
//
//const vec3 ambientColor = vec3(0.7);
//const float ambientint = 0.08;
//
//#define specular
////#define color_disp
////#define only_shape
////#define show_not_finished
//const int nb_refr = 7;
//
//const float specint = 0.2;
//const float specshin = 20.;
//
//const float normdelta = 0.0004;
//const float maxdist = 55.;
//
//const float ior = 2.418;
//const float ior_r = 2.408;
//const float ior_g = 2.424;
//const float ior_b = 2.432;
//const vec3 diamondColor = vec3(0.95, 0.95, 0.9);
//
//// Antialias. Change from 1 to 2 or more AT YOUR OWN RISK! It may CRASH your browser while compiling!
//const float aawidth = 0.9;
//const int aasamples = 1;
//
//vec2 rotateVec(vec2 vect, float angle)
//{
//    vec2 rv;
//    rv.x = vect.x*cos(angle) + vect.y*sin(angle);
//    rv.y = vect.x*sin(angle) - vect.y*cos(angle);
//    return rv;
//}
//
//float map_simple(vec3 pos)
//{
//    float angle = 2.*pi*u_mouse.x/u_resolution.x;
//    float angle2 = -2.*pi*u_mouse.y/u_resolution.y;
//
//    vec3 posr = pos;
//    posr = vec3(posr.x, posr.y*cos(angle2) + posr.z*sin(angle2), posr.y*sin(angle2) - posr.z*cos(angle2));
//    posr = vec3(posr.x*cos(angle) + posr.z*sin(angle), posr.y, posr.x*sin(angle) - posr.z*cos(angle));
//
//    float d = 1.05;
//    float s = atan(posr.y, posr.x);
//
//    vec3 flatvec = vec3(cos(s), sin(s), 1.444);
//    vec3 flatvec2 = vec3(cos(s), sin(s), -1.072);
//
//    float d1 = dot(flatvec, posr) - d;                        // Crown
//    d1 = max(dot(flatvec2, posr) - d, d1);                    // Pavillon
//    d1 = max(dot(vec3(0., 0., 1.), posr) - 0.35, d1);         // Table
//    return d1;
//}
//
//float map(vec3 pos)
//{
//    float angle = 2.*pi*u_mouse.x/u_resolution.x;
//    float angle2 = -2.*pi*u_mouse.y/u_resolution.y;
//
//    vec3 posr = pos;
//    posr = vec3(posr.x, posr.y*cos(angle2) + posr.z*sin(angle2), posr.y*sin(angle2) - posr.z*cos(angle2));
//    posr = vec3(posr.x*cos(angle) + posr.z*sin(angle), posr.y, posr.x*sin(angle) - posr.z*cos(angle));
//
//    float d = 0.94;
//    float b = 0.5;
//
//    float af2 = 4./pi;
//    float s = atan(posr.y, posr.x);
//    float sf = floor(s*af2 + b)/af2;
//    float sf2 = floor(s*af2)/af2;
//
//    vec3 flatvec = vec3(cos(sf), sin(sf), 1.444);
//    vec3 flatvec2 = vec3(cos(sf), sin(sf), -1.072);
//    vec3 flatvec3 = vec3(cos(s), sin(s), 0);
//    float csf1 = cos(sf + 0.21);
//    float csf2 = cos(sf - 0.21);
//    float ssf1 = sin(sf + 0.21);
//    float ssf2 = sin(sf - 0.21);
//    vec3 flatvec4 = vec3(csf1, ssf1, -1.02);
//    vec3 flatvec5 = vec3(csf2, ssf2, -1.02);
//    vec3 flatvec6 = vec3(csf2, ssf2, 1.03);
//    vec3 flatvec7 = vec3(csf1, ssf1, 1.03);
//    vec3 flatvec8 = vec3(cos(sf2 + 0.393), sin(sf2 + 0.393), 2.21);
//
//    float d1 = dot(flatvec, posr) - d;                           // Crown, bezel facets
//    d1 = max(dot(flatvec2, posr) - d, d1);                       // Pavillon, pavillon facets
//    d1 = max(dot(vec3(0., 0., 1.), posr) - 0.3, d1);             // Table
//    d1 = max(dot(vec3(0., 0., -1.), posr) - 0.865, d1);          // Cutlet
//    d1 = max(dot(flatvec3, posr) - 0.911, d1);                   // Girdle
//    d1 = max(dot(flatvec4, posr) - 0.9193, d1);                  // Pavillon, lower-girdle facets
//    d1 = max(dot(flatvec5, posr) - 0.9193, d1);                  // Pavillon, lower-girdle facets
//    d1 = max(dot(flatvec6, posr) - 0.912, d1);                   // Crown, upper-girdle facets
//    d1 = max(dot(flatvec7, posr) - 0.912, d1);                   // Crown, upper-girdle facets
//    d1 = max(dot(flatvec8, posr) - 1.131, d1);                   // Crown, star facets
//    return d1;
//}
//
//float trace(vec3 cam, vec3 ray, float maxdist, bool inside)
//{
//    float t = 4.2;
//    float dist;
//
//    // "Bounding" tracing
//    if (!inside)
//    {
//  		for (int i = 0; i < 12; ++i)
//    	{
//    		vec3 pos = ray*t + cam;
//    		dist = map_simple(pos);
//        	if (dist>maxdist || abs(dist)<0.001)
//            	break;
//        	t+= dist*0.95;
//  		}
//    }
//
//    // "Actual" tracing
//  	for (int i = 0; i < 30; ++i)
//    {
//    	vec3 pos = ray*t + cam;
//    	dist = inside?-map(pos):map(pos);
//        if (dist>maxdist)
//            break;
//        t+= dist*(inside?0.4:0.8);
//  	}
//  	return t;
//}
//
//// Old slower version (but a bit more precise)
//float trace_sl(vec3 cam, vec3 ray, float maxdist, bool inside)
//{
//    float t = 4.;
//  	for (int i = 0; i < 80; ++i)
//    {
//    	vec3 pos = ray*t + cam;
//    	float dist = inside?-map(pos):map(pos);
//        if (dist>(inside?3.:maxdist) || abs(dist)<0.001 || (inside && i>30))
//            break;
//        t+= dist*0.4;
//  	}
//  	return t;
//}
//
//// From https://www.shadertoy.com/view/MstGDM
//vec3 getNormal(vec3 pos, float e, bool inside)
//{
//    vec2 q = vec2(0, e);
//    return (inside?-1.:1.)*normalize(vec3(map(pos + q.yxx) - map(pos - q.yxx),
//                          map(pos + q.xyx) - map(pos - q.xyx),
//                          map(pos + q.xxy) - map(pos - q.xxy)));
//}
//
//vec3 obj_color(vec3 norm, vec3 pos)
//{
//  	#ifdef only_shape
//  	return vec3(0.35, 0.7, 1.0);
//  	#else
//    return vec3(0.);
//    #endif
//}
//
//vec3 sky_color(vec3 ray)
//{
//	return vec3(0.);
//}
//
//// Fresnel reflectance factor through Schlick's approximation: https://en.wikipedia.org/wiki/Schlick's_approximation
//float fresnel(vec3 ray, vec3 norm, float n2)
//{
//   float n1 = 1.; // air
//   float angle = clamp(acos(-dot(ray, norm)), -pi/2.15, pi/2.15);
//   float r0 = pow((n1-n2)/(n1+n2), 2.);
//   float r = r0 + (1. - r0)*pow(1. - cos(angle), 5.);
//   return clamp(0., 0.9, r);
//}
//
//vec3 lampShading(Lamp lamp, vec3 norm, vec3 pos, vec3 ocol, bool inside)
//{
//	vec3 pl = normalize(lamp.position - pos);
//    float dlp = distance(lamp.position, pos);
//    vec3 pli = pl/pow(1. + lamp.attenuation*dlp, 2.);
//
//    vec3 col;
//
//    // Diffuse shading
//    if (!inside)
//    {
//    	float diff = clamp(dot(norm, pli), 0., 1.);
//    	col = ocol*normalize(lamp.color)*lamp.intensity*smoothstep(0., 1.04, pow(diff, 0.78));
//    }
//
//    // Specular shading
//    #ifdef specular
//    if (dot(norm, lamp.position - pos) > 0.0)
//        col+= normalize(lamp.color)*lamp.intensity*specint*pow(max(0.0, dot(reflect(pl, norm), normalize(pos - campos))), specshin);
//    #endif
//
//    return col;
//}
//
//vec3 lampsShading(vec3 norm, vec3 pos, vec3 ocol, bool inside)
//{
//    vec3 col = vec3(0.);
//    for (int l=0; l<3; l++) // lamps.length()
//        col+= lampShading(lamps[l], norm, pos, ocol, inside);
//
//    return col;
//}
//
//// From https://www.shadertoy.com/view/lsSXzD, modified
//vec3 GetCameraRayDir(vec2 vWindow, vec3 vCameraDir, float fov)
//{
//	vec3 vForward = normalize(vCameraDir);
//	vec3 vRight = normalize(cross(vec3(0.0, 1.0, 0.0), vForward));
//	vec3 vUp = normalize(cross(vForward, vRight));
//
//	vec3 vDir = normalize(vWindow.x * vRight + vWindow.y * vUp + vForward * fov);
//
//	return vDir;
//}
//
//RenderData trace0(vec3 tpos, vec3 ray, bool inside)
//{
//    float tx = trace(tpos, ray, maxdist, inside);
//    vec3 col;
//    int objnr;
//
//    vec3 pos = tpos + tx*ray;
//    vec3 norm;
//    if (tx<10.)
//    {
//        norm = getNormal(pos, normdelta, inside);
//        if (!inside)
//        {
//            // Coloring
//        	col = obj_color(norm, pos) + ambientColor*ambientint;
//        	objnr = 1;
//        }
//
//        // Shading
//        col = lampsShading(norm, pos, col, inside);
//  }
//  else
//  {
//      // Sky
//      col = sky_color(ray);
//      objnr = 3;
//  }
//  return RenderData(col, pos, norm, objnr);
//}
//
//vec4 render(vec2 fragCoord, vec3 campos, float ior)
//{
//  lamps[0] = Lamp(vec3(0., 4.5, 10.), vec3(1., 1., 1.), 5., 0.1);
//  lamps[1] = Lamp(vec3(12., -0.5, 6.), vec3(.7, .8, 1.), 5., 0.1);
//  lamps[2] = Lamp(vec3(-1.3, 0.8, -1.5), vec3(1., .95, .8), 3.5, 0.1);
//
//  vec2 uv = fragCoord.xy / u_resolution.xy;
//  uv = uv*2.0 - 1.0;
//  uv.x*= u_resolution.x / u_resolution.y;
//
//  vec3 ray = GetCameraRayDir(uv, camdir, fov);
//
//  RenderData traceinf = trace0(campos, ray, false);
//  vec3 col = traceinf.col;
//
//  #ifdef only_shape
//  	return vec4(col, 1.0);
//  #else
//
//  if (traceinf.objnr==1)
//  {
//  		vec3 norm = traceinf.norm;
//        vec3 ray_r = refract(ray, traceinf.norm, 1./ior);
//        vec3 ray_r2;
//
//        int n2;
//        for (int n=0; n<nb_refr; n++)
//        {
//        	traceinf = trace0(traceinf.pos, ray_r, true);
//        	col+= traceinf.col;
//        	col*= diamondColor;
//      		ray_r2 = refract(ray_r, traceinf.norm, ior);
//        	if (length(ray_r2)!=0.)
//            {
//           		col+= sky_color(ray_r2)*diamondColor;
//                break;
//            }
//            ray_r2 = reflect(ray_r, traceinf.norm);
//            ray_r = ray_r2;
//            n2 = n;
//        }
//        if (n2==nb_refr-1)
//            #ifdef show_not_finished
//            col = vec3(1., 0., 1.);
//      		#else
//            col+= sky_color(ray_r2)*diamondColor;
//      		#endif
//
//        // Outer reflection
//        float r = fresnel(ray, norm, ior);
//        col = mix(col, sky_color(reflect(ray, norm)), r);
//  }
//  return vec4(col, 1.0);
//  #endif
//}
//
//vec4 render_rgb(vec2 fragCoord, vec3 campos)
//{
//    #ifdef color_disp
//    	vec4 col;
//    	col.r = render(fragCoord, campos, ior_r).r;
//    	col.g = render(fragCoord, campos, ior_g).g;
//    	col.b = render(fragCoord, campos, ior_b).b;
//    	col.a = 1.;
//	return col;
//    #else
//    	return render(fragCoord, campos, ior);
//    #endif
//
//}
//
//vec4 render_aa(vec2 fragCoord, vec3 campos)
//{
//    // Antialiasing
//    vec4 vs = vec4(0.);
//    for (int j=0;j<aasamples ;j++)
//    {
//       float oy = float(j)*aawidth/max(float(aasamples-1), 1.);
//       for (int i=0;i<aasamples ;i++)
//       {
//          float ox = float(i)*aawidth/max(float(aasamples-1), 1.);
//          vs+= render_rgb(fragCoord + vec2(ox, oy), campos);
//       }
//    }
//    return vs/vec4(aasamples*aasamples);
//}
//
//vec3 mainImage(vec2 fragCoord)
//{
//       vec4 fragColor = render_aa(fragCoord, campos);
//       return fragColor.xyz;
//}
//
//void main(){
//    gl_FragColor = vec4(mainImage(gl_FragCoord.xy),1.);
//}