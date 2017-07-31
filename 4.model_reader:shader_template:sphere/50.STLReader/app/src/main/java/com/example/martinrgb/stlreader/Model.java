package com.example.martinrgb.stlreader;

import java.nio.FloatBuffer;

/**
 * Created by MartinRGB on 2017/4/19.
 */

public class Model {
    //三角面个数
    private int facetCount;
    //顶点坐标数组
    private float[] verts;


    //每个顶点对应的法向量数组
    private float[] vnorms;



    //每个三角面的属性信息
    private short[] remarks;


    //顶点数组转换而来的Buffer
    private FloatBuffer vertBuffer;

    //每个顶点对应的法向量转换而来的Buffer
    private FloatBuffer vnormBuffer;
    //以下分别保存所有点在x,y,z方向上的最大值、最小值
    float maxX;
    float minX;
    float maxY;
    float minY;
    float maxZ;
    float minZ;

    //返回模型的中心点
    //注意，下载的源码中，此函数修改修正如下
    public Point getCentrePoint() {

        float cx = minX + (maxX - minX) / 2;
        float cy = minY + (maxY - minY) / 2;
        float cz = minZ + (maxZ - minZ) / 2;
        return new Point(cx, cy, cz);
    }

    //包裹模型的最大半径
    public float getR() {
        float dx = (maxX - minX);
        float dy = (maxY - minY);
        float dz = (maxZ - minZ);
        float max = dx;
        if (dy > max)
            max = dy;
        if (dz > max)
            max = dz;
        return max;
    }

    //设置顶点数组的同时，设置对应的Buffer
    public void setVerts(float[] verts) {
        this.verts = verts;
        vertBuffer = Util.floatToBuffer(verts);
    }

    //设置顶点数组法向量的同时，设置对应的Buffer
    public void setVnorms(float[] vnorms) {
        this.vnorms = vnorms;
        vnormBuffer = Util.floatToBuffer(vnorms);
    }

    //···
    //其他属性对应的setter、getter函数
    //···


    public void setRemarks(short[] remarks) {
        this.remarks = remarks;
    }


    public int getFacetCount() {
        return facetCount;
    }

    public void setFacetCount(int facetCount) {
        this.facetCount = facetCount;
    }


    public FloatBuffer getVertBuffer() {
        return vertBuffer;
    }

    public FloatBuffer getVnormBuffer() {
        return vnormBuffer;
    }

}