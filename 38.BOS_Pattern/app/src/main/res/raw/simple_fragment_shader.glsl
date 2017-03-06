#ifdef GL_ES
precision highp float;
#endif

uniform float u_time;
uniform vec2 u_resolution;
uniform vec2 u_mouse;

//##1
//vec2 brickTile(vec2 _st, float _zoom){
//    _st *= _zoom;
//
//    // Here is where the offset is happening
//    _st.x += step(0.0, mod(_st.y,3.)) * (6.- u_time);
//    _st.x += step(1.0, mod(_st.y,2.)) * (6.+ 2.*u_time);
//
//    //_st.y += step(0.0, mod(_st.x,3.)) * (6.- u_time);
//
//    return fract(_st);
//}
//
//float box(vec2 _st, vec2 _size){
//    _size = vec2(0.5)-_size*0.5;
//    vec2 uv = smoothstep(_size,_size+vec2(1e-4),_st);
//    uv *= smoothstep(_size,_size+vec2(1e-4),vec2(1.0)-_st);
//    return uv.x*uv.y;
//}
//
//void main(void){
//    vec2 st = gl_FragCoord.xy/u_resolution.xy;
//    vec3 color = vec3(0.836,0.551,0.865);
//
//    // Modern metric brick of 215mm x 102.5mm x 65mm
//    // http://www.jaharrison.me.uk/Brickwork/Sizes.html
//     //st /= vec2(2.15,0.65)/1.5;
//
//    // Apply the brick tiling
//    st = brickTile(st,5.);
//
//    vec2 translate = vec2(cos(u_time),sin(u_time));
//
//
//    color = vec3(box(st,vec2(0.900)));
//
//    // Uncomment to see the space coordinates
//     color = vec3(st.x,st.y,sin(u_time));
//
//    gl_FragColor = vec4(color,1.0);
//}

// Author @patriciogv ( patriciogonzalezvivo.com ) - 2015

//##2
//#define PI 3.14159265358979323846
//
//vec2 rotate2D(vec2 _st, float _angle){
//    _st -= 0.5;
//    _st =  mat2(cos(_angle),-sin(_angle),
//                sin(_angle),cos(_angle)) * _st;
//    _st += 0.5;
//    return _st;
//}
//
//vec2 tile(vec2 _st, float _zoom){
//    _st *= _zoom;
//    return fract(_st);
//}
//
//float box(vec2 _st, vec2 _size, float _smoothEdges){
//    _size = vec2(0.5)-_size*0.5;
//    vec2 aa = vec2(_smoothEdges*.5);
//    vec2 uv = smoothstep(_size,_size+aa,_st);
//    uv *= smoothstep(_size,_size+aa,vec2(1.0)-_st);
//    return uv.x*uv.y;
//}
//
//void main(void){
//    vec2 st = gl_FragCoord.xy/u_resolution.xy;
//    vec3 color = vec3(0.0);
//
//    // Divide the space in 4
//    st = tile(st,4.);
//
//    // Use a matrix to rotate the space 45 degrees
//    st = rotate2D(st,PI*sin(u_time)*cos(u_time));
//
//    // Draw a square
//    color = vec3(box(st,vec2(0.7),0.082));
//    // color = vec3(st,0.0);
//
//    gl_FragColor = vec4(color,1.0);
//}



//##1
#define PI 3.14159265358979323846
vec2 rotate2D (vec2 _st, float _angle) {
    _st -= 0.5;
    _st =  mat2(cos(_angle),-sin(_angle),
                sin(_angle),cos(_angle)) * _st;
    _st += 0.5;
    return _st;
}

vec2 tile (vec2 _st, float _zoom) {
    _st *= _zoom;
    return fract(_st);
}

vec2 rotateTilePattern(vec2 _st){

    //  Scale the coordinate system by 2x2
    _st *= 2.;

    //  Give each cell an index number
    //  according to its position
    float index = 0.0;
    //来制作不同的index
    index += step(1., mod(_st.x,2.0));
    index += step(1., mod(_st.y,2.0))*2.0;

    //      |
    //  2   |   3
    //      |
    //--------------
    //      |
    //  0   |   1
    //      |

    // Make each cell between 0.0 - 1.0
    _st = fract(_st);

    // Rotate each cell according to the index
    if(index == 1.0){
        //  Rotate cell 1 by 90 degrees
        _st = rotate2D(_st,PI*0.5);
    } else if(index == 2.0){
        //  Rotate cell 2 by -90 degrees
        _st = rotate2D(_st,PI*-0.5);
    } else if(index == 3.0){
        //  Rotate cell 3 by 180 degrees
        _st = rotate2D(_st,PI);
    }

    return _st;
}

void main (void) {
    vec2 st = gl_FragCoord.xy/u_resolution.xy;

    st = tile(st,3.);
    st = rotateTilePattern(st);

    // Make more interesting combinations
    // st = tile(st,2.0);
	// st = rotate2D(st,-PI*u_time*0.25);
    // st = rotateTilePattern(st*2.);
    // st = rotate2D(st,PI*u_time*0.25);

    // step(st.x,st.y) just makes a b&w triangles
    // but you can use whatever design you want.
    gl_FragColor = vec4(vec3(step(st.x,st.y)),1.0);
}