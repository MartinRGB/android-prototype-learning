package com.xiaomi.martinrgb.Heightmap.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

/**
 * Created by MartinRGB on 2017/2/21.
 */


public class IndexBuffer {
    private final int bufferId;

    public IndexBuffer(short[] indexData) {
        // Allocate a buffer.
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(buffers.length, buffers, 0);

        if (buffers[0] == 0) {
            throw new RuntimeException("Could not create a new index buffer object.");
        }

        bufferId = buffers[0];

        // Bind to the buffer.
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[0]);

        // Transfer data to native memory.
        ShortBuffer indexArray = ByteBuffer
                .allocateDirect(indexData.length * Constants.BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indexData);
        indexArray.position(0);

        // Transfer data from native memory to the GPU buffer.
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexArray.capacity() * Constants.BYTES_PER_SHORT,
                indexArray, GLES20.GL_STATIC_DRAW);

        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);

        // We let the native buffer go out of scope, but it won't be released
        // until the next time the garbage collector is run.
    }

    public int getBufferId() {
        return bufferId;
    }
}
