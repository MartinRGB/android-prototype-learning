
precision mediump float;
varying vec3 v_Color;
varying float v_ElapsedTime;

uniform sampler2D u_TextureUnit;

void main() {

    //绘制圆形
//    float xDistance = 0.5 - gl_PointCoord.x;
//    float yDistance = 0.5 - gl_PointCoord.y;
//    float distanceFromCenter = sqrt(xDistance*xDistance + yDistance*yDistance);
//
//    if(distanceFromCenter > 0.5){
//        discard;
//    }
//    else{
//        //让年轻的粒子明亮，年老的粒子暗
//        //单独提出来为正方形
//        gl_FragColor = vec4(v_Color/v_ElapsedTime,1.0);
//    }

    gl_FragColor = vec4(v_Color/v_ElapsedTime,1.0) * texture2D(u_TextureUnit,gl_PointCoord);


}
