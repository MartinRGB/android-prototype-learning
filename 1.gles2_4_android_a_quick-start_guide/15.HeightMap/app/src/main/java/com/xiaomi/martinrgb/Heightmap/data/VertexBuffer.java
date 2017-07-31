package com.xiaomi.martinrgb.Heightmap.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by MartinRGB on 2017/2/21.
 */

//对于创建后，就不经常变化的对象，例如高度图，可以使用缓冲区保存，提供更好的性能
public class VertexBuffer {
    private final int bufferId;

    public VertexBuffer(float[] vertexData){
        //给缓冲对象分配内存
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(buffers.length,buffers,0);
        if(buffers[0] == 0){
            throw  new RuntimeException("Could not create a new vertex buffer object");
        }
        bufferId = buffers[0];

        //绑定Buffer到GLES
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,buffers[0]);

        //把数据加载到内存中
        FloatBuffer vertexArray = ByteBuffer
                .allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);

        vertexArray.position(0);

        //从内存中找到数据，加载到GPU缓冲区中
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,vertexArray.capacity()* Constants.BYTES_PER_FLOAT,vertexArray,GLES20.GL_STATIC_DRAW);

        //用完之后，释放缓冲区
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
    }

    public void setVetexAttribPointer(int dataOffset,int attributeLocation,int componentCount,int stride){
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bufferId);
        //指定了渲染时索引值为index的顶点属性数组的属性和位置
        GLES20.glVertexAttribPointer(attributeLocation,componentCount,GLES20.GL_FLOAT,false,stride,dataOffset);
        //启用相应的属性
        GLES20.glEnableVertexAttribArray(attributeLocation);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
    }
}
