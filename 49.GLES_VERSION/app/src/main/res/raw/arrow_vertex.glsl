
attribute vec3 aPosition;
uniform mat4 uMVP;

void main() {
    vec4 vertex = vec4(aPosition[0],aPosition[1],aPosition[2],1.);
    gl_Position = uMVP * vertex;

}

