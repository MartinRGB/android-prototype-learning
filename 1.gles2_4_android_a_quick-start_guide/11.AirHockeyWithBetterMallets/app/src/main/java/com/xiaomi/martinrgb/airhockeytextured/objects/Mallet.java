package com.xiaomi.martinrgb.airhockeytextured.objects;

import android.opengl.GLES20;

import com.xiaomi.martinrgb.airhockeytextured.data.Constants;
import com.xiaomi.martinrgb.airhockeytextured.data.VertexArray;
import com.xiaomi.martinrgb.airhockeytextured.programs.ColorShaderProgram;
import com.xiaomi.martinrgb.airhockeytextured.util.Geometry;

import java.util.List;

/**
 * Created by MartinRGB on 2017/2/17.
 */

public class Mallet {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius;
    public final float height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        ObjectBuilder.GeneratedData generatedData =
                ObjectBuilder.createMallet(new Geometry.Point(0f, 0f, 0f), radius, height,
                        numPointsAroundMallet);

        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    //把顶点数据存储在本地缓冲区中
    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    //绘画命令 存储在 drawList 中
    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
