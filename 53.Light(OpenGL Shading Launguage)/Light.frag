// simple fragment shader

// 'time' contains seconds since the program was linked.
uniform float time;

// #define COLOR_TEXTURE 1
// #define ALPHA_TEXTURE 2

#ifdef COLOR_TEXTURE
uniform sampler2D tColor;
#endif

#ifdef ALPHA_TEXTURE
uniform sampler2D tAlpha;
#endif

varying vec2 vTexCoord;
varying vec3 vNormal;
varying vec3 vEye;
varying float fogFactor;

int lightsNumber = 8;

void DirectionalLight(in int i, in vec3 normal,inout vec4 ambient, inout vec4 diffuse, inout vec4 specular){
     float nDotVP;         // normal . light direction
     float nDotHV;         // normal . light half vector
     float pf;             // power factor

     nDotVP = max(0.0, dot(normal,
                   normalize(vec3(gl_LightSource[i].position))));
     nDotHV = max(0.0, dot(normal, vec3(gl_LightSource[i].halfVector)));

     if (nDotVP == 0.0)
         pf = 0.0;
     else
         pf = pow(nDotHV, gl_FrontMaterial.shininess);

     ambient  += gl_LightSource[i].ambient;
     diffuse  += gl_LightSource[i].diffuse * nDotVP;
     specular += gl_LightSource[i].specular * pf;
}

void PointLight(in int i, in vec3 eye, in vec3 ecPosition3, in vec3 normal, inout vec4 ambient, inout vec4 diffuse, inout vec4 specular){
    float nDotVP;         // normal . light direction
    float nDotHV;         // normal . light half vector
    float pf;             // power factor
    float attenuation;    // computed attenuation factor
    float d;              // distance from surface to light source
    vec3  VP;             // direction from surface to light position
    vec3  halfVector;     // direction of maximum highlights

    // Compute vector from surface to light position
    VP = vec3(gl_LightSource[i].position) - ecPosition3;

    // Compute distance between surface and light position
    d = length(VP);

    // Normalize the vector from surface to light position
    VP = normalize(VP);

    // Compute attenuation
    attenuation = 1.0 / (gl_LightSource[i].constantAttenuation +
                         gl_LightSource[i].linearAttenuation * d +
                         gl_LightSource[i].quadraticAttenuation * d * d);

    halfVector = normalize(VP + eye);

    nDotVP = max(0.0, dot(normal, VP));
    nDotHV = max(0.0, dot(normal, halfVector));

    if (nDotVP == 0.0)
        pf = 0.0;
    else
        pf = pow(nDotHV, gl_FrontMaterial.shininess);

    ambient += gl_LightSource[i].ambient * attenuation;
    diffuse += gl_LightSource[i].diffuse * nDotVP * attenuation;
    specular += gl_LightSource[i].specular * pf * attenuation;
}           

void SpotLight(in int i, in vec3 eye, vec3 ecPosition3, in vec3 normal, inout vec4 ambient, inout vec4 diffuse, inout vec4 specular){
    float nDotVP;           // normal . light direction
    float nDotHV;           // normal . light half vector
    float pf;               // power factor
    float spotDot;          // cosine of angle between spotlight
    float spotAttenuation;  // spotlight attenuation factor
    float attenuation;      // computed attenuation factor
    float d;                // distance from surface to light source
    vec3 VP;                // direction from surface to light position
    vec3 halfVector;        // direction of maximum highlights

    // Compute vector from surface to light position
    VP = vec3(gl_LightSource[i].position) - ecPosition3;

    // Compute distance between surface and light position
    d = length(VP);

    // Normalize the vector from surface to light position
    VP = normalize(VP);

    // Compute attenuation
    attenuation = 1.0 / (gl_LightSource[i].constantAttenuation +
                         gl_LightSource[i].linearAttenuation * d +
                         gl_LightSource[i].quadraticAttenuation * d * d);

    // See if point on surface is inside cone of illumination
    spotDot = dot(-VP, normalize(gl_LightSource[i].spotDirection));

    if (spotDot < gl_LightSource[i].spotCosCutoff)
        spotAttenuation = 0.0; // light adds no contribution
    else
        spotAttenuation = pow(spotDot, gl_LightSource[i].spotExponent);

    // Combine the spotlight and distance attenuation.
    attenuation *= spotAttenuation;

    halfVector = normalize(VP + eye);

    nDotVP = max(0.0, dot(normal, VP));
    nDotHV = max(0.0, dot(normal, halfVector));

    if (nDotVP == 0.0)
        pf = 0.0;
    else
        pf = pow(nDotHV, gl_FrontMaterial.shininess);

    ambient  += gl_LightSource[i].ambient * attenuation;
    diffuse  += gl_LightSource[i].diffuse * nDotVP * attenuation;
    specular += gl_LightSource[i].specular * pf * attenuation;
} 

vec4 calc_lighting_color(in vec3 _ecPosition, in vec3 _normal) {
  vec3 eye = vec3(0.0, 0.0, 1.0);
  //eye = -normalize(ecPosition3);

  // Clear the light intensity accumulators
  vec4 amb  = vec4(0.0);
  vec4 diff = vec4(0.0);
  vec4 spec = vec4(0.0);

  // Loop through enabled lights, compute contribution from each
  for (int i = 0; i < lightsNumber; i++){
      if (gl_LightSource[i].position.w == 0.0)
          DirectionalLight(i, normalize(_normal), amb, diff, spec);
      else if (gl_LightSource[i].spotCutoff == 180.0)
          PointLight(i, eye, _ecPosition, normalize(_normal), amb, diff, spec);
      else
          SpotLight(i, eye, _ecPosition, normalize(_normal), amb, diff, spec);
  }

  // NOTE: gl_FrontLightModelProduct.sceneColor = gl_FrontMaterial.emission + gl_FrontMaterial.ambient * gl_LightModel.ambient
  vec4 color =  gl_FrontLightModelProduct.sceneColor + 
                amb * gl_FrontMaterial.ambient + 
#ifdef COLOR_TEXTURE
                diff * texture2D(tColor, vTexCoord) +
#else
                diff * gl_FrontMaterial.diffuse +
#endif
                spec * gl_FrontMaterial.specular;

#ifdef ALPHA_TEXTURE
  color.a = texture2D(tAlpha, vTexCoord).a;
#endif

  return color;
}

void main()

{



	gl_FragColor = calc_lighting_color(vEye,vNormal);
}
