attribute vec4 a_Position;

void main(){
    gl_Position = a_Position;
    //指定点的大小
    gl_PointSize = 10.0;
}