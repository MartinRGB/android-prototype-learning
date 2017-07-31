#ifdef GL_FRAGMENT_PRECISION_HIGH
precision highp float;
#else
precision mediump float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;
//uniform sampler2D u_backbuffer;

#define M_PI 3.1415926535897932384626433832795




float hash( float n )
{
    return fract(sin(n)*43758.5453);
}

void main()
{
    
    
	float mx = max( u_resolution.x, u_resolution.y );
	vec2 uv = (gl_FragCoord.xy - u_resolution.xy*0.5)/mx;
    float r = 0.5 ;
    
    
    //Rotate
	uv *= mat2(
	r, r ,
	r, r );

    
    //Single
    float y;
    if(mx*uv.x>0.5){
        
		y = mx*(uv.x)*1./22.  - u_time*1. - hash(uv.x/100000.)  ;//+ u_time
    }else{
        y =  mx*(uv.x)*1./22. - mod(u_time,3.8)*1.;
    }
    
    //Lightness
	float f = 0.8 ;
    

    //Section
	vec3 color =
		vec3(
			mod( y + uv.x     , 3.8 )*f,   //3. 每一栏目宽度
			mod( y +uv.x , 0.1)*f,
			mod( y*8. + uv.x*2., 0.9 )*f )*  //y*3. 条纹
			abs(fract(-uv.x*22.)  )  ;
    


    //Graident Mapping
    vec3 color2 = vec3(0.07+0.62*uv.x,0.04+0.15*uv.x,0.18+0.09*uv.x);
    color = mix(color,color2,0.7);
    
	gl_FragColor = vec4( color, 1.0 );
}
