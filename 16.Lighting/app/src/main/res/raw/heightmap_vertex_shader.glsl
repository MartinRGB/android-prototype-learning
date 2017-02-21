uniform mat4 u_Matrix;
attribute vec3 a_Position;
varying vec3 v_Color;

//光线的矢量位置 - 归一化向量
uniform vec3 u_VectorToLight;
//高度图法线
attribute vec3 a_Normal;

void main()                    
{
    //MIX 平滑颜色差值
    v_Color = mix(vec3(0.180, 0.467, 0.153),    // A dark green 
                  vec3(0.660, 0.670, 0.680),    // A stony gray 
                  a_Position.y);
		
    gl_Position = u_Matrix * vec4(a_Position, 1.0);

    //让法线高于宽度的十倍，为了适应drawHeightMap时候的Matrix.scaleM
    vec3 scaledNormal = a_Normal;
    scaledNormal.y *= 10.0;
    scaledNormal = normalize(scaledNormal);

    //光源的向量与表面法线点积，等于之间夹角的cos,这就是表面与光线的cos，介于1-0，黑色与原色
    float diffuse = max(dot(scaledNormal,u_VectorToLight),0.0);

    float diffuse2 = diffuse*0.3;

    v_Color *= diffuse2;

    //*白天特加
    //float ambient = 0.2;

    //*夜晚特加
    float ambient = 0.1;

    v_Color += ambient;





}
