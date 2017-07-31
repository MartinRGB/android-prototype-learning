precision mediump float;

//纹理数据
uniform sampler2D u_TextureUnit;
//纹理坐标
varying vec2 v_TextureCoordinates;

void main() {

    gl_FragColor = texture2D(u_TextureUnit,v_TextureCoordinates);
}
