package com.xiaomi.martinrgb.Heightmap.objects;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.Heightmap.data.IndexBuffer;
import com.xiaomi.martinrgb.Heightmap.data.VertexBuffer;
import com.xiaomi.martinrgb.Heightmap.programs.HeightmapShaderProgram;

/**
 * Created by MartinRGB on 2017/2/21.
 */

public class Heightmap {
    private static final int POSITION_COMPONENT_COUNT =3;
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
        final float[] heightmapVertices = new float[width*height*POSITION_COMPONENT_COUNT];
        int offset = 0;

        //左上角映射到(-0.5,-0.5),右下角(0.5,0.5) ,Center(0,0)
        //CPU 按顺序缓存和移动数据时，更有效率
        for (int row = 0; row < height;row++){
            for(int col = 0;col < width;col++){
                final float xPosition = ((float) col/ (float) (width - 1)) - 0.5f;
                final float yPosition = (float) Color.red(pixels[(row*height) + col])/(float) 255;
                final float zPosition = ((float)row / (float)(height - 1)) - 0.5f;

                heightmapVertices[offset++] = xPosition;
                heightmapVertices[offset++] = yPosition;
                heightmapVertices[offset++] = zPosition;
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

    public void bindData(HeightmapShaderProgram heightmapShaderProgram){
        vertexBuffer.setVetexAttribPointer(0,heightmapShaderProgram.getaPositionAttributeLocation(),POSITION_COMPONENT_COUNT,0);
    }

    public void draw() {
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,indexBuffer.getBufferId());
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,numElements,GLES20.GL_UNSIGNED_SHORT,0);
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,0);
    }

}
