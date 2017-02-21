package com.xiaomi.martinrgb.Heightmap.objects;

import android.graphics.Color;
import android.opengl.Matrix;

import com.xiaomi.martinrgb.Heightmap.util.Geometry;

import java.util.Random;

/**
 * Created by MartinRGB on 2017/2/20.
 */

public class ParticleEmitter {
    private final Geometry.Point position;
    private final Geometry.Vector direction;
    private final int color;

    //粒子的速度和角度方差
    private final float angleVariance;
    private final float speedVariance;

    private final Random random = new Random();

    private float[] rotationMatrix = new float[16];
    private float[] directionVector = new float[4];
    private float[] resultVector = new float[4];

    public int EmitterParticlesCount;

    //定义发射器的位置方向颜色
    public ParticleEmitter(Geometry.Point position, Geometry.Vector direction, int color,float angleVarianceInDegress,float speedVarinace){
        this.position = position;
        this.direction = direction;
        this.color = color;

        this.angleVariance = angleVarianceInDegress;
        this.speedVariance = speedVarinace;

        directionVector[0] = direction.x;
        directionVector[1] = direction.y;
        directionVector[2] = direction.z;
    }

    public void addParticles(ParticleSystem particleSystem,float currentTime,int count){
        for (int i = 0; i< count; i++){
            //随即旋转矩阵
            Matrix.setRotateEulerM(rotationMatrix,0,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance
            );
            //方向矩阵和旋转量矩阵组合
            Matrix.multiplyMV(
                    resultVector,0,
                    rotationMatrix,0,
                    directionVector,0
            );
            //速度方差
            float speedAdjustment = 1f + random.nextFloat() * speedVariance;
            Geometry.Vector thisDirection = new Geometry.Vector(
                    resultVector[0]*speedAdjustment,
                    resultVector[1]*speedAdjustment,
                    resultVector[2]*speedAdjustment
            );

            int randomRed = Math.min(255,Math.max(0,random.nextInt()));
            int randomGreen = Math.min(255,Math.max(0,random.nextInt()));
            int randomBlue =  Math.min(255,Math.max(0,random.nextInt()));
            int randomColor = Color.rgb(randomRed, randomGreen, randomBlue);

            particleSystem.addParticle(position,color,thisDirection,currentTime);

            EmitterParticlesCount = particleSystem.currentParticleCount;

        }
    }


}
