package com.xiaomi.martinrgb.particles.objects;

import android.graphics.Color;
import android.opengl.GLES20;

import com.xiaomi.martinrgb.particles.data.Constants;
import com.xiaomi.martinrgb.particles.data.VertexArray;
import com.xiaomi.martinrgb.particles.programs.ParticleShaderProgram;
import com.xiaomi.martinrgb.particles.util.Geometry;

/**
 * Created by MartinRGB on 2017/2/20.
 */

public class ParticleSystem {

        private static final int POSITION_COMPONENT_COUNT = 3;
        private static final int COLOR_COMPONENT_COUNT = 3;
        private static final int VECTOR_COMPONENT_COUNT = 3;
        private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;

        private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT
                + COLOR_COMPONENT_COUNT
                + VECTOR_COMPONENT_COUNT
                + PARTICLE_START_TIME_COMPONENT_COUNT;

        private static final int STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;

        private final float[] particles;
        private final VertexArray vertexArray;
        private final int maxParticleCount;

        private int currentParticleCount;
        private int nextParticle;

        public ParticleSystem(int maxParticleCount) {
            particles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
            vertexArray = new VertexArray(particles);
            this.maxParticleCount = maxParticleCount;

        }

        public void addParticle(Geometry.Point position,int color,Geometry.Vector direction,
                                float particleStartTime){
            //一些列属性占据的浮点值，根据索引号，向下递增偏移量，记住了新粒子从何处开始
            final int particleOffset = nextParticle * TOTAL_COMPONENT_COUNT;

            int currentOffset = particleOffset;
            //添加粒子-到数组结尾处，从0开始护手最旧的粒子
            nextParticle++;


            //记录粒子-我们还要记录多少个粒子被绘制出来
            if(currentOffset < maxParticleCount){
                currentOffset++;
            }

            if(nextParticle == maxParticleCount){
                nextParticle = 0;
            }


            particles[currentOffset++] = position.x;
            particles[currentOffset++] = position.y;
            particles[currentOffset++] = position.z;

            particles[currentOffset++] = Color.red(color) / 255f;
            particles[currentOffset++] = Color.green(color) / 255f;
            particles[currentOffset++] = Color.blue(color) / 255f;

            particles[currentOffset++] = direction.x;
            particles[currentOffset++] = direction.y;
            particles[currentOffset++] = direction.z;

            particles[currentOffset++] = particleStartTime;

            //把新粒子复制到本地缓冲区，以便OpenGL存取新数据
            vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT);
        };

        public void bindData(ParticleShaderProgram particleShaderProgram){
            int dataOffset = 0;

            vertexArray.setVertexAttribPointer(dataOffset,particleShaderProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,STRIDE);
            dataOffset += POSITION_COMPONENT_COUNT;

            vertexArray.setVertexAttribPointer(dataOffset, particleShaderProgram.getColorAttributeLocation(),
                    COLOR_COMPONENT_COUNT, STRIDE);
            dataOffset += COLOR_COMPONENT_COUNT;

            vertexArray.setVertexAttribPointer(dataOffset,
                    particleShaderProgram.getDirectionVectorAttributeLocation(), VECTOR_COMPONENT_COUNT, STRIDE);
            dataOffset += VECTOR_COMPONENT_COUNT;

            vertexArray.setVertexAttribPointer(dataOffset,
                    particleShaderProgram.getParticleStartTimeAttributeLocation(),
                    PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE);

        }

        public void draw(){
            GLES20.glDrawArrays(GLES20.GL_POINTS,0,currentParticleCount);
        }


}
