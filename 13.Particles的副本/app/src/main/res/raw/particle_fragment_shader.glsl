
precision mediump float;
varying vec3 v_Color;
varying float v_ElapsedTime;

void main() {
    //让年轻的粒子明亮，年老的粒子暗
    gl_FragColor = vec4(v_Color/v_ElapsedTime,1.0);
}
