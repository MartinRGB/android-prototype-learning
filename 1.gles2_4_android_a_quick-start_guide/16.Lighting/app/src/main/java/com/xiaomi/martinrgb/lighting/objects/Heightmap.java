package com.xiaomi.martinrgb.lighting.objects;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.lighting.data.Constants;
import com.xiaomi.martinrgb.lighting.data.IndexBuffer;
import com.xiaomi.martinrgb.lighting.data.VertexBuffer;
import com.xiaomi.martinrgb.lighting.programs.HeightmapShaderProgram;
import com.xiaomi.martinrgb.lighting.util.Geometry;

/**
 * Created by MartinRGB on 2017/2/21.
 */

public class Heightmap {
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int POSITION_COMPONENT_COUNT =3;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT+NORMAL_COMPONENT_COUNT)* Constants.BYTES_PER_FLOAT;
    private final int width;
    private final int height;
    private final int numElements;
    private final VertexBuffer vertexBuffer;
    private final IndexBuffer indexBuffer;

    public Heightmap(Bitmap bitmap){
        width = bitmap.getWidth();
        height = bitmap.getHeight();

        if(width * height > 65536){
            throw new RuntimeException("Heightmap is too large for the index buffer");
        }

        numElements = calculateNumElements();
        //Android 把 Bitmap 对象穿进去，把数据加载进顶点缓冲区
        vertexBuffer = new VertexBuffer(loadBitmapData(bitmap));
        //建立索引缓冲区
        indexBuffer = new IndexBuffer(createIndexData());

    }

    //拿取整体大 Bitmap的顶点数据
    private float[] loadBitmapData(Bitmap bitmap){
        //计算出pixel 总量
        final int[] pixels = new int[width *height];
        //获取 bitmap 具体的像素
        bitmap.getPixels(pixels,0,width,0,0,width,height);
        //用完就回收
        bitmap.recycle();

        //占用顶点数目，每个像素对应一个顶点
        final float[] heightmapVertices = new float[width*height*TOTAL_COMPONENT_COUNT];
        int offset = 0;

        //左上角映射到(-0.5,-0.5),右下角(0.5,0.5) ,Center(0,0)
        //CPU 按顺序缓存和移动数据时，更有效率
        for (int row = 0; row < height;row++){
            for(int col = 0;col < width;col++){
//                final float xPosition = ((float) col/ (float) (width - 1)) - 0.5f;
//                final float yPosition = (float) Color.red(pixels[(row*height) + col])/(float) 255;
//                final float zPosition = ((float)row / (float)(height - 1)) - 0.5f;

                final Geometry.Point point = getPoint(pixels,row,col);

                heightmapVertices[offset++] = point.x;
                heightmapVertices[offset++] = point.y;
                heightmapVertices[offset++] = point.z;

                final Geometry.Point top = getPoint(pixels, row - 1, col);
                final Geometry.Point left = getPoint(pixels, row, col - 1);
                final Geometry.Point right = getPoint(pixels, row, col + 1);
                final Geometry.Point bottom = getPoint(pixels, row + 1, col);

                //向量叉积
                final Geometry.Vector rightToLeft = Geometry.vectorBetween(right, left);
                final Geometry.Vector topToBottom = Geometry.vectorBetween(top, bottom);
                //归一化得到表面法线
                final Geometry.Vector normal = rightToLeft.crossProduct(topToBottom).normalize();

                heightmapVertices[offset++] = normal.x;
                heightmapVertices[offset++] = normal.y;
                heightmapVertices[offset++] = normal.z;
            }
        }
        return heightmapVertices;
    }

    // 3 x 3的高度图有(3-1)x(3-1) 4 个组，每个组需要2个三角形，每个三角形，需要3个元素
    private int calculateNumElements(){
        return (width - 1) *(height - 1) * 2 *3;
    }

    // 建立 short 类型的数组，通过6顶点构建三角形
    private short[] createIndexData(){
        final short[] indexData = new short[numElements];
        int offset = 0;
        for (int row = 0; row<height -1;row++){
            for (int col = 0;col<width -1;col++){
                short topLeftIndexNum = (short) (row*width + col);
                short topRightIndexNum = (short) (row*width + col + 1);
                short bottomLeftIndexNum = (short) ((row + 1) * width + col);
                short bottomRightIndexNum = (short) ((row + 1) * width + col + 1);

                //写出两个三角形
                indexData[offset++] = topLeftIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = topRightIndexNum;
                indexData[offset++] = topRightIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = bottomRightIndexNum;
            }
        }
        return indexData;
    }

    //法线


    //取到点
    private Geometry.Point getPoint(int[]pixels, int row, int col){
        float x = ((float) col/ (float) (width - 1)) - 0.5f;
        float z = ((float) row / (float)(height - 1)) - 0.5f;

        row = clamp(row,0,width -1);
        col = clamp(col,0,height-1);

        float y = (float) Color.red(pixels[(row*height) + col])/(float) 255;

        return new Geometry.Point(x,y,z);
    }

    //限定值范围
    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public void bindData(HeightmapShaderProgram heightmapShaderProgram){
        vertexBuffer.setVetexAttribPointer(0,heightmapShaderProgram.getaPositionAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);
        vertexBuffer.setVetexAttribPointer(POSITION_COMPONENT_COUNT*Constants.BYTES_PER_FLOAT,heightmapShaderProgram.getaNormalAttributeLocation(),NORMAL_COMPONENT_COUNT,STRIDE);
    }

    public void draw() {
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,indexBuffer.getBufferId());
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,numElements,GLES20.GL_UNSIGNED_SHORT,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

}
