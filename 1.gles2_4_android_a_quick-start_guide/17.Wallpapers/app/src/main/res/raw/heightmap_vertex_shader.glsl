uniform mat4 u_Matrix;
//光线的矢量位置 - 归一化向量
uniform vec3 u_VectorToLight;
//高度图法线
attribute vec3 a_Normal;
attribute vec4 a_Position;

varying vec3 v_Color;

//模型试图矩阵
uniform mat4 u_MVMatrix;
//倒置矩阵的转置
uniform mat4 u_IT_MVMatrix;
//模型试图投影矩阵
uniform mat4 u_MVPMatrix;
uniform vec4 u_PointLightPositions[3];    // In eye space
uniform vec3 u_PointLightColors[3];

vec3 materialColor;
vec4 eyeSpacePosition;
vec3 eyeSpaceNormal;

vec3 getAmbientLighting();
vec3 getDirectionalLighting();
vec3 getPointLighting();

void main()                    
{
    //MIX 平滑颜色差值

    materialColor = mix(vec3(0.180, 0.467, 0.153),    // A dark green
                        vec3(0.660, 0.670, 0.680),    // A stony gray
                        a_Position.y);
    //眼位置
    eyeSpacePosition = u_MVMatrix * a_Position;
    //眼法线
    eyeSpaceNormal = normalize(vec3(u_IT_MVMatrix * vec4(a_Normal,0.0)));

    //每一种光类型，累加 v_Color
    v_Color = getAmbientLighting();
    v_Color += getDirectionalLighting();
    v_Color += getPointLighting();

    gl_Position = u_MVPMatrix * a_Position;

//        v_Color = mix(vec3(0.180, 0.467, 0.153),    // A dark green
//                      vec3(0.660, 0.670, 0.680),    // A stony gray
//                      a_Position.y);

//    //让法线高于宽度的十倍，为了适应drawHeightMap时候的Matrix.scaleM
//    vec3 scaledNormal = a_Normal;
//    scaledNormal.y *= 10.0;
//    scaledNormal = normalize(scaledNormal);
//
//    //光源的向量与表面法线点积，等于之间夹角的cos,这就是表面与光线的cos，介于1-0，黑色与原色
//    float diffuse = max(dot(scaledNormal,u_VectorToLight),0.0);
//
//    float diffuse2 = diffuse*0.3;
//
//    v_Color *= diffuse2;
//
//    //*白天特加
//    //float ambient = 0.2;
//
//    //*夜晚特加
//    float ambient = 0.1;
//
//    v_Color += ambient;

}

//环境光
vec3 getAmbientLighting()
{
    return materialColor * 0.1;
}

//定向光
vec3 getDirectionalLighting()
{
    return materialColor * 0.3
         * max(dot(eyeSpaceNormal, u_VectorToLight), 0.0);
}

//循环计算每个通过的点光，把结果加入lightingSum,用朗伯体反射计算光照亮
vec3 getPointLighting()
{
    vec3 lightingSum = vec3(0.0);

    for (int i = 0; i < 3; i++) {
        vec3 toPointLight = vec3(u_PointLightPositions[i])
                          - vec3(eyeSpacePosition);
        //当前位置倒光源的向量
        float distance = length(toPointLight);
        //归一化
        toPointLight = normalize(toPointLight);

        //材质颜色与点光相乘，用5放大，使其更名了
        float cosine = max(dot(eyeSpaceNormal, toPointLight), 0.0);
        //累加到lightSum,除以距离，光照密度随之减少
        lightingSum += (materialColor * u_PointLightColors[i] * 5.0 * cosine)
                       / distance;
    }

    return lightingSum;
}



