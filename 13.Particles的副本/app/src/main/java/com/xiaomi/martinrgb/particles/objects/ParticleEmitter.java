package com.xiaomi.martinrgb.particles.objects;

import com.xiaomi.martinrgb.particles.util.Geometry;

/**
 * Created by MartinRGB on 2017/2/20.
 */

public class ParticleEmitter {
    private final Geometry.Point position;
    private final Geometry.Vector direction;
    private final int color;

    //定义发射器的位置方向颜色
    public ParticleEmitter(Geometry.Point position, Geometry.Vector direction, int color){
        this.position = position;
        this.direction = direction;
        this.color = color;
    }

    public void addParticles(ParticleSystem particleSystem,float currentTime,int count){
        for (int i = 0; i< count; i++){
            particleSystem.addParticle(position,color,direction,currentTime);
        }
    }

}
